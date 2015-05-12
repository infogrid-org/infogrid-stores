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

package org.infogrid.store.sql.mysql.test;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.infogrid.store.test.AbstractStoreIteratorTest2;
import org.infogrid.store.sql.mysql.MysqlStore;
import org.infogrid.store.sql.test.AbstractSqlStoreTest;
import org.infogrid.util.logging.Log;
import org.junit.Before;

/**
 * Tests the FilesystemStoreIterator.
 */
public class MysqlStoreIteratorTest2
        extends
            AbstractStoreIteratorTest2
{
    @Before
    public void setup()
    {
        MysqlDataSource theDataSource = new MysqlDataSource();
        theDataSource.setDatabaseName( AbstractSqlStoreTest.test_DATABASE_NAME );

        theTestStore = MysqlStore.create( theDataSource, AbstractSqlStoreTest.test_TABLE_NAME );
    }

    // Our Logger
    private static Log log = Log.getLogInstance(MysqlStoreIteratorTest2.class);
}
