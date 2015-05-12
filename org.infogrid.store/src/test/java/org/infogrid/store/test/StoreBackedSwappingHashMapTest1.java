//
// This file is part of InfoGrid(tm). You may not use this file except in
// compliance with the InfoGrid license. The InfoGrid license and important
// disclaimers are contained in the file LICENSE.InfoGrid.txt that you should
// have received with InfoGrid. If you have not received LICENSE.InfoGrid.txt
// or you do not consent to all aspects of the license and the disclaimers,
// no license is granted; do not use this file.
// 
// For more information about InfoGrid go to http://infogrid.org/
//
// Copyright 1998-2015 by Johannes Ernst
// All rights reserved.
//

package org.infogrid.store.test;

import java.util.ArrayList;
import org.infogrid.store.SerializingStoreEntryMapper;
import org.infogrid.store.Store;
import org.infogrid.store.m.MStore;
import org.infogrid.store.util.StoreBackedSwappingHashMap;
import org.infogrid.util.SwappingHashMap;
import org.infogrid.util.SwappingHashMapListener;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests the StoreBackedSwappingHashMap's persistence.
 */
public class StoreBackedSwappingHashMapTest1
        extends
            AbstractStoreTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    @Test
    public void run()
        throws
            Exception
    {
        //
        
        Store store = MStore.create();
        
        SerializingStoreEntryMapper<String,Integer> mapper = new SerializingStoreEntryMapper<String,Integer>() {
            public String stringToKey(
                    String s )
            {
                return s;
            }
        };
        
        MyListener<String,Integer> listener = new MyListener<String,Integer>();
        
        StoreBackedSwappingHashMap<String,Integer> map = StoreBackedSwappingHashMap.createWeak( mapper, store );
        map.addSoftSwappingHashMapListener( listener );
        
        
        String [] keys = {
            "a",
            "d",
            "c",
            "e",
            "b",
        };
        
        log.info( "Inserting" );
        
        for( int i=0 ; i<keys.length ; ++i ) {
            map.put( keys[i], new Integer( i ));
        }
        
        checkEquals( map.size(), keys.length, "Wrong size map" );
        
        //
        
        log.info( "Waiting and checking that they are still there" );
        
        Thread.sleep( 2000L );
        collectGarbage();
        
        checkEquals( listener.loadedEvents.size(),  0,           "wrong number of loadedEvents" );
        checkEquals( listener.savedEvents.size(),   keys.length, "wrong number of savedEvents" );
        checkEquals( listener.removedEvents.size(), 0,           "wrong number of removedEvents" );

        listener.reset();
        
        checkEquals( map.size(), keys.length, "Wrong size map" );

        for( int i=0 ; i<keys.length ; ++i ) {
            Integer found = map.get( keys[i] );
            checkObject( found, "Value not found at index " + i );
            checkEquals( i, found.intValue(), "Wrong value found at index " + i );
        }
        
        checkEquals( listener.loadedEvents.size(),  keys.length, "wrong number of loadedEvents" );
        checkEquals( listener.savedEvents.size(),   0,           "wrong number of savedEvents" );
        checkEquals( listener.removedEvents.size(), 0,           "wrong number of removedEvents" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreBackedSwappingHashMapTest1.class  );

    /**
     * Listener implementation for this test.
     * 
     * @param <K> key parameter
     * @param <V> value parameter
     */
    static class MyListener<K,V>
            implements
                SwappingHashMapListener<K,V>
    {
        /**
         * A key-value pair has been loaded from storage.
         *
         * @param source the SwappingHashMap that emitted this event
         * @param key the key of the key-value pair that was loaded
         */
        public void loadedFromStorage(
                SwappingHashMap<K,V> source,
                K                    key,
                V                    value )
        {
            loadedEvents.add( key );
        }

        /**
         * A key-value pair has been saved to storage.
         *
         * @param source the SwappingHashMap that emitted this event
         * @param key the key of the key-value pair that was saved
         */
        public void savedToStorage(
                SwappingHashMap<K,V> source,
                K                    key,
                V                    value )
        {
            savedEvents.add( key );
        }

        /**
         * A key-value pair has been removed from storage.
         *
         * @param source the SwappingHashMap that emitted this event
         * @param key the key of the key-value pair that was saved
         */
        public void removedFromStorage(
                SwappingHashMap<K,V> source,
                K                    key )
        {
            removedEvents.add( key );
        }
        
        /**
         * Reset the event stores.
         */
        public void reset()
        {
            loadedEvents.clear();
            savedEvents.clear();
            removedEvents.clear();
        }
        
        public ArrayList<K> loadedEvents  = new ArrayList<K>();
        public ArrayList<K> savedEvents   = new ArrayList<K>();
        public ArrayList<K> removedEvents = new ArrayList<K>();
    }
}
