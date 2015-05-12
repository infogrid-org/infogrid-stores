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

package org.infogrid.store.sql.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.infogrid.store.StoreValue;
import org.infogrid.store.keystore.KeyStoreWrapper;
import org.infogrid.util.StreamUtils;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests the Keystore.
 */
public abstract class SqlKeyStoreTest1
        extends
            AbstractSqlStoreTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    @Test
    @Override
    public void run()
        throws
            Exception
    {
        //
        
        log.info( "Deleting old database and creating new database" );

        theSqlStore.initializeHard();

        // 
        
        log.info( "Creating the KeyStoreWrapper" );
        
        KeyStoreWrapper wrapper = KeyStoreWrapper.create( theTestStore, KEY_INTO_STORE, theKeyStorePassword );
        
        InputStream inStream = SqlKeyStoreTest1.class.getResourceAsStream( theKeyStoreFile );
        wrapper.load( inStream );

        wrapper = null;
        collectGarbage();

        //
        
        log.info( "checking that the content is in the Store" );
        
        StoreValue v = theTestStore.get( KEY_INTO_STORE );
        checkObject( v, "No StoreValue found" );
        checkCondition( v.getData().length > 0, "empty data in store" );
        
        //
        
        log.info( "trying to recover" );
        
        wrapper = KeyStoreWrapper.create( theTestStore, KEY_INTO_STORE, theKeyStorePassword );

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        wrapper.store( outStream );
        
        byte [] dataFromStore = outStream.toByteArray();
        byte [] dataFromFile  = StreamUtils.slurp( SqlKeyStoreTest1.class.getResourceAsStream( theKeyStoreFile ));
        
        checkEqualByteArrays( dataFromFile, dataFromStore, "different content" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( SqlKeyStoreTest1.class );
    
    /**
     * Key into to the store for the key Store content.
     */
    public static final String KEY_INTO_STORE = "key-store-test-key";
    
    /**
     * THe test file.
     */
    protected String theKeyStoreFile;
    
    /**
     * The password on the key store.
     */
    protected String theKeyStorePassword;
}
