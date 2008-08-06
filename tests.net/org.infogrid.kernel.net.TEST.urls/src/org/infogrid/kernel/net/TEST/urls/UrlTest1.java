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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.kernel.net.TEST.urls;


import org.infogrid.mesh.net.NetMeshObject;

import org.infogrid.meshbase.net.NetMeshBase;
import org.infogrid.meshbase.net.NetMeshBaseIdentifier;
import org.infogrid.meshbase.net.NetMeshBaseLifecycleManager;
import org.infogrid.meshbase.net.m.NetMMeshBase;
import org.infogrid.meshbase.transaction.Transaction;

import org.infogrid.meshbase.net.proxy.m.MPingPongNetMessageEndpointFactory;
import org.infogrid.util.logging.Log;

import java.util.concurrent.ScheduledExecutorService;
import org.infogrid.model.primitives.ModelPrimitivesStringRepresentation;
import org.infogrid.util.text.StringRepresentation;

/**
 * Tests conversion of NetMeshObjectIdentifiers into URLs.
 */
public class UrlTest1
        extends
            AbstractUrlTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    public void run()
        throws
            Exception
    {
        NetMeshBaseLifecycleManager life1 = mb1.getMeshBaseLifecycleManager();
        NetMeshBaseLifecycleManager life2 = mb2.getMeshBaseLifecycleManager();

        log.info( "Setting up entities" );

        Transaction tx1 = mb1.createTransactionAsap();
        NetMeshObject obj1_mb1 = life1.createMeshObject( mb1.getMeshObjectIdentifierFactory().fromExternalForm( "xxx" ));
        tx1.commitTransaction();

        Transaction tx2 = mb2.createTransactionAsap();
        NetMeshObject obj2_mb2 = life2.createMeshObject( mb2.getMeshObjectIdentifierFactory().fromExternalForm( "yyy" ));
        tx2.commitTransaction();

        NetMeshObject obj2_mb1 = mb1.accessLocally( mb2.getIdentifier(), obj2_mb2.getIdentifier() );

        //
        
        log.info( "Checking HTML urls with different context" );

        StringRepresentation rep = ModelPrimitivesStringRepresentation.TEXT_HTML;
        
        String context = "http://example.org/foo";
        
        String obj1_mb1_different_default    = obj1_mb1.toStringRepresentationLinkStart( rep, context, true );
        String obj1_mb1_different_nonDefault = obj1_mb1.toStringRepresentationLinkStart( rep, context, false );
        
        String obj2_mb1_different_default    = obj2_mb1.toStringRepresentationLinkStart( rep, context, true );
        String obj2_mb1_different_nonDefault = obj2_mb1.toStringRepresentationLinkStart( rep, context, false );

        checkEquals(
                obj1_mb1_different_default,
                "<a href=\"" + context + "/%23xxx" + "\">",
                "obj1_mb1_different_default is wrong" );
        checkEquals(
                obj1_mb1_different_nonDefault,
                "<a href=\"" + context + "/[meshbase=" + mb1.getIdentifier().toExternalForm() + "]" + "%23xxx\">",
                "obj1_mb1_different_nonDefault is wrong" );

        checkEquals(
                obj2_mb1_different_default,
                "<a href=\"" + context + "/" + obj2_mb1.getIdentifier().toExternalForm().replaceAll( "#", "%23" ) + "\">",
                "obj2_mb1_different_default is wrong" );
        checkEquals(
                obj2_mb1_different_nonDefault,
                "<a href=\"" + context + "/[meshbase=" + mb1.getIdentifier().toExternalForm() + "]" + obj2_mb1.getIdentifier().toExternalForm().replaceAll( "#", "%23" ) + "\">",
                "obj2_mb1_different_nonDefault is wrong" );
    }

    /*
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        UrlTest1 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new UrlTest1( args );
            test.run();

        } catch( Throwable ex ) {
            log.error( ex );
            System.exit(1);
        }
        if( test != null ) {
            test.cleanup();
        }
        if( errorCount == 0 ) {
            log.info( "PASS" );
        } else {
            log.info( "FAIL (" + errorCount + " errors)" );
        }
        System.exit( errorCount );
    }

    /**
     * Constructor.
     *
     * @param args command-line arguments
     * @throws Exception all kinds of things may happen during tests
     */
    public UrlTest1(
            String [] args )
        throws
            Exception
    {
        super( UrlTest1.class );

        MPingPongNetMessageEndpointFactory endpointFactory = MPingPongNetMessageEndpointFactory.create( exec );
        endpointFactory.setNameServer( theNameServer );

        mb1 = NetMMeshBase.create( net1, endpointFactory, theModelBase, null, rootContext );
        mb2 = NetMMeshBase.create( net2, endpointFactory, theModelBase, null, rootContext );
        
        theNameServer.put( mb1.getIdentifier(), mb1 );
        theNameServer.put( mb2.getIdentifier(), mb2 );
    }

    /**
     * Clean up after the test.
     */
    @Override
    public void cleanup()
    {
        mb1.die();
        mb2.die();
        
        exec.shutdown();
    }

    /**
     * The first NetMeshBaseIdentifier.
     */
    protected NetMeshBaseIdentifier net1 = NetMeshBaseIdentifier.create( "http://example.com/one/two" );

    /**
     * The second NetMeshBaseIdentifier.
     */
    protected NetMeshBaseIdentifier net2 = NetMeshBaseIdentifier.createUnresolvable( "http://example.net/three/four" );

    /**
     * The first NetMeshBase.
     */
    protected NetMeshBase mb1;

    /**
     * The second NetMeshBase.
     */
    protected NetMeshBase mb2;

    /**
     * Our ThreadPool.
     */
    protected ScheduledExecutorService exec = createThreadPool( 1 );

    // Our Logger
    private static Log log = Log.getLogInstance( UrlTest1.class );
}