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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.probe.test.forwardreference;

import java.util.concurrent.ScheduledExecutorService;
import org.infogrid.meshbase.net.CoherenceSpecification;
import org.infogrid.meshbase.net.DefaultNetMeshBaseIdentifierFactory;
import org.infogrid.meshbase.net.NetMeshBaseIdentifier;
import org.infogrid.meshbase.net.NetMeshBaseIdentifierFactory;
import org.infogrid.meshbase.net.local.m.LocalNetMMeshBase;
import org.infogrid.meshbase.net.xpriso.logging.LogXprisoMessageLogger;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.probe.m.MProbeDirectory;
import org.infogrid.testharness.AbstractTest;
import org.infogrid.util.context.Context;
import org.infogrid.util.context.SimpleContext;

/**
 * Provides functionality useful for ForwardReferenceTests.
 */
public abstract class AbstractForwardReferenceTest
        extends
            AbstractTest
{
//
//    /**
//     * Constructor.
//     *
//     * @param testClass the Class to be tested
//     */
//    protected AbstractProbeTest(
//            Class testClass )
//    {
//        super( localFileName( testClass, "/ResourceHelper" ));
//    }
//
//    /**
//     * Helper method to count MeshObjects.
//     *
//     * @param base the MeshBase containing th MeshObjects
//     * @param mylog if given, log the found objects there
//     * @return the number of MeshObjects in the MeshBase
//     */
//    protected static int countMeshObjects(
//            IterableNetMeshBase base,
//            Log                 mylog )
//    {
//        int    ret = countFromIterator( base.iterator(), mylog );
//        return ret;
//    }
//
//    /**
//     * Count the number of Objects found by iterating over an Iterator.
//     *
//     * @param iter the Iterator
//     * @param mylog if given, log the found objects there
//     * @return the number of Objects found
//     */
//    public static int countFromIterator(
//            Iterator<?> iter,
//            Log         mylog )
//    {
//        int ret = 0;
//        StringBuilder buf = new StringBuilder(); // do this instead of logging directly, that way we don't changing the threading behavior
//        while( iter.hasNext() ) {
//            Object current = iter.next();
//
//            ++ret;
//            buf.append( "found " + current );
//        }
//
//        if( mylog != null ) {
//            if( buf.length() > 0 ) {
//                mylog.info( "found " + ret + " elements:\n" + buf );
//            } else {
//                mylog.info( "found " + ret + " elements" );
//            }
//        }
//        return ret;
//    }
//
//    /**
//     * Dump the content of a MeshBase to the traceMethodCallEntry channel of a Log.
//     *
//     * @param mb the MeshBase whose content we want to dump
//     * @param prefix a string to prepend
//     * @param mylog the Log to dump to
//     * @throws Exception catch-all Exception
//     */
//    protected final void dumpMeshBase(
//            IterableMeshBase mb,
//            String           prefix,
//            Log              mylog )
//        throws
//            Exception
//    {
//        if( mylog.isDebugEnabled() ) {
//            StringBuilder buf = new StringBuilder( prefix );
//            for( MeshObject current : mb ) {
//                buf.append( "\n  " );
//                buf.append( current.getIdentifier() );
//                buf.append( " (created: " );
//                buf.append( current.getTimeCreated() );
//                buf.append( " updated: " );
//                buf.append( current.getTimeUpdated() );
//                buf.append( " read: " );
//                buf.append( current.getTimeRead() );
//                buf.append( ")" );
//
//                if( true ) {
//                    buf.append( "\n    Types:" );
//                    for( EntityType et : current.getTypes() ) {
//                        buf.append( "\n        " );
//                        buf.append( et.getName().value() );
//                    }
//                }
//                if( true ) {
//                    buf.append( "\n    Properties:" );
//                    for( PropertyType pt : current.getAllPropertyTypes() ) {
//                        buf.append( "\n        " );
//                        buf.append( pt.getName().value() );
//                        buf.append( ": " );
//                        buf.append( current.getPropertyValue( pt ));
//                    }
//                }
//            }
//            mylog.debug( buf.toString() );
//        }
//    }
//
//    /**
//     * Dump a ChangeSet to the traceMethodCallEntry channel of a Log.
//     *
//     * @param changes the ChangeSet to dump
//     * @param mylog the Log
//     */
//    protected final void dumpChangeSet(
//            ChangeSet changes,
//            Log       mylog )
//    {
//        if( mylog.isDebugEnabled() && changes.size() > 0 ) {
//            int i=0;
//            for( Change current : changes ) {
//                mylog.debug( "Change " + i++ + ": " + current );
//            }
//        }
//    }
//
//    /**
//     * Check the position of the Proxies.
//     *
//     * @param obj the NetMeshObject whose proxies are checked
//     * @param proxiesTowards the NetMeshBases to which the proxies are supposed to be pointing
//     * @param proxyTowardHome the NetMeshBase towards which the proxyTowardHome is supposed to be pointing, or null
//     * @param proxyTowardLock the NetMeshBase towards which the proxyTowardsLock is supposed to be pointing, or null
//     * @param msg the message to print when the proxies are not correct
//     * @return true if check passed
//     */
//    protected boolean checkProxies(
//            NetMeshObject  obj,
//            NetMeshBase [] proxiesTowards,
//            NetMeshBase    proxyTowardHome,
//            NetMeshBase    proxyTowardLock,
//            String         msg )
//    {
//        boolean ret = true;
//
//        if( obj == null ) {
//            reportError( "Cannot check proxies of null object" );
//            return false;
//        }
//
//        Proxy [] proxies = obj.getAllProxies();
//
//        if( proxies == null || proxies.length == 0 ) {
//            if( !( proxiesTowards == null || proxiesTowards.length == 0 )) {
//                reportError( msg + ": object has no proxies, should have", proxiesTowards, obj.getIdentifier() );
//                return false;
//            } else {
//                return true; // no proxies is correct
//            }
//        } else if( proxiesTowards == null || proxiesTowards.length == 0 ) {
//            reportError( msg + ": object has proxies, should have none", proxies, obj.getIdentifier() );
//            return false;
//        }
//        if( proxies.length != proxiesTowards.length ) {
//            reportError( msg + ": object has wrong number of proxies.", proxiesTowards, proxies.length );
//            ret = false;
//        }
//
//        NetMeshBaseIdentifier [] proxiesIdentifiers        = new NetMeshBaseIdentifier[ proxies.length ];
//        NetMeshBaseIdentifier [] proxiesTowardsIdentifiers = new NetMeshBaseIdentifier[ proxiesTowards.length ];
//        for( int i=0 ; i<proxies.length ; ++i ) {
//            proxiesIdentifiers[i] = proxies[i].getPartnerMeshBaseIdentifier();
//        }
//        for( int i=0 ; i<proxiesTowards.length ; ++i ) {
//            proxiesTowardsIdentifiers[i] = proxiesTowards[i].getIdentifier();
//        }
//        if( !checkEqualsOutOfSequence( proxiesIdentifiers, proxiesTowardsIdentifiers, null )) {
//            reportError( msg + ": not the same content: ", proxiesIdentifiers, proxiesTowardsIdentifiers );
//        }
//
//        if( proxyTowardLock == null ) {
//            if( obj.getProxyTowardsLockReplica() != null ) {
//                reportError( msg + ": has proxyTowardsLock but should not", obj.getIdentifier() );
//                ret = false;
//            }
//
//        } else if( obj.getProxyTowardsLockReplica() == null ) {
//            reportError( msg + ": does not have proxyTowardsLock but should", obj.getIdentifier() );
//            ret = false;
//
//        } else {
//            ret &= checkEquals( proxyTowardLock.getIdentifier(), obj.getProxyTowardsLockReplica().getPartnerMeshBaseIdentifier(), msg + ": wrong proxyTowardLock" );
//        }
//        if( proxyTowardHome == null ) {
//            if( obj.getProxyTowardsHomeReplica() != null ) {
//                reportError( msg + ": has proxyTowardHome but should not", obj.getIdentifier() );
//                ret = false;
//            }
//
//        } else if( obj.getProxyTowardsHomeReplica() == null ) {
//            reportError( msg + ": does not have proxyTowardHome but should", obj.getIdentifier() );
//            ret = false;
//
//        } else {
//            ret &= checkEquals( proxyTowardHome.getIdentifier(), obj.getProxyTowardsHomeReplica().getPartnerMeshBaseIdentifier(), msg + ": wrong proxyTowardLock" );
//        }
//        return ret;
//    }
//

    /**
     * Constructor.
     *
     * @param testClass the Class to be tested
     * @param fastOrSlow command-line argument indicating whether to run this test with a fast or slow CoherenceSpecification
     * @throws Exception all sorts of things may go wrong in a test
     */
    protected AbstractForwardReferenceTest(
            Class  testClass,
            String fastOrSlow )
        throws
            Exception
    {
        super( localFileName( testClass, "/ResourceHelper" ));

        here = theMeshBaseIdentifierFactory.fromExternalForm( "http://here.local/" ); // this is not going to work for communications
        base = LocalNetMMeshBase.create( here, theModelBase, null, theProbeDirectory, exec, rootContext );

        if( getLog().isDebugEnabled() ) {
            theXprisoMessageLogger = LogXprisoMessageLogger.create( getLog() );
            base.setXprisoMessageLogger( theXprisoMessageLogger );
        }

        if( "slow".equals( fastOrSlow )) {
            theCoherence = CoherenceSpecification.ONE_TIME_ONLY;
            theWait      = false;
        } else {
            theCoherence = CoherenceSpecification.ONE_TIME_ONLY_FAST;
            theWait      = true;
        }
    }

    /**
     * Clean up after the test.
     */
    @Override
    public void cleanup()
    {
        base.die();
    }

    /**
     * Our ThreadPool.
     */
    protected ScheduledExecutorService exec = createThreadPool( 1 );

    /**
     * The ProbeDirectory.
     */
    protected MProbeDirectory theProbeDirectory = MProbeDirectory.create();

    /**
     * Identifier of the main NetMeshBase.
     */
    protected NetMeshBaseIdentifier here;

    /**
     * The main NetMeshBase.
     */
    protected LocalNetMMeshBase base;

    /**
     * The XprisoMessageLogger to use.
     */
    protected LogXprisoMessageLogger theXprisoMessageLogger;

    /**
     * The CoherenceSpecification to use.
     */
    protected CoherenceSpecification theCoherence;

    /**
     * Whether to make the calling thread wait for a while, or not.
     */
    protected boolean theWait;

    /**
     * The root context for these tests.
     */
    protected static final Context rootContext = SimpleContext.createRoot( "root-context" );

    /**
     * The ModelBase.
     */
    protected static ModelBase theModelBase = ModelBaseSingleton.getSingleton();

    /**
     * The test protocol. In the real world this would be something like "jdbc".
     */
    protected static final String PROTOCOL_NAME = "test";

    /**
     * Factory for NetMeshBaseIdentifiers.
     */
    protected static NetMeshBaseIdentifierFactory theMeshBaseIdentifierFactory = DefaultNetMeshBaseIdentifierFactory.create(
            new String[] { "http", "file" },
            new String[] { PROTOCOL_NAME } );

    /**
     * Expected duration within which at least one ping-pong round trip can be completed.
     * Milliseconds.
     */
    protected static final long PINGPONG_ROUNDTRIP_DURATION = 100L;
}
