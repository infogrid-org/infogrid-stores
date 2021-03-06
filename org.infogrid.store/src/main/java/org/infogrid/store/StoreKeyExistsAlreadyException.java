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

package org.infogrid.store;

/**
 * Thrown to indicate that a key exists already in a {@link Store}, for an operation
 * that requires that the key does not exist already.
 */
public class StoreKeyExistsAlreadyException
        extends
            StoreException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param store the Store in which the key did exist already
     * @param key the key that already exists in the Store
     */
    public StoreKeyExistsAlreadyException(
            Store  store,
            String key )
    {
        super( store, key, "Store key exists already: " + key, null );
    }
    
    /**
     * Constructor.
     *
     * @param store the Store in which the key did exist already
     * @param key the key that already exists in the Store
     * @param cause the underlying cause
     */
    public StoreKeyExistsAlreadyException(
            Store     store,
            String    key,
            Throwable cause )
    {
        super( store, key, "Store key exists already: " + key, cause );
    }
}
