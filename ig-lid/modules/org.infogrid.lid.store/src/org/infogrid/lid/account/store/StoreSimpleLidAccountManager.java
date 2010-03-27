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

package org.infogrid.lid.account.store;

import java.util.ArrayList;
import java.util.Map;
import org.infogrid.lid.account.AbstractLidAccountManager;
import org.infogrid.lid.credential.LidCredentialType;
import org.infogrid.lid.account.LidAccountExistsAlreadyException;
import org.infogrid.lid.account.LidAccount;
import org.infogrid.lid.account.SimpleLidAccount;
import org.infogrid.store.Store;
import org.infogrid.store.prefixing.PrefixingStore;
import org.infogrid.store.util.StoreBackedSwappingHashMap;
import org.infogrid.util.AbstractFactory;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.CannotFindHasIdentifierException;
import org.infogrid.util.Factory;
import org.infogrid.util.FactoryException;
import org.infogrid.util.HasIdentifier;
import org.infogrid.util.Identifier;
import org.infogrid.util.InvalidIdentifierException;
import org.infogrid.util.ObjectExistsAlreadyFactoryException;
import org.infogrid.util.PatientSmartFactory;
import org.infogrid.util.SmartFactory;

/**
 * Store implementation of LidAccountManager.
 */
public class StoreSimpleLidAccountManager
        extends
            AbstractLidAccountManager
{
    /**
     * Factory method.
     *
     * @param store the Store to store to
     * @param availableCredentialTypes the credential types known to the application
     * @return the created StoreSimpleLidAccountManager
     */
    public static StoreSimpleLidAccountManager create(
            LidCredentialType [] availableCredentialTypes,
            Store                store )
    {
        PrefixingStore accountStore       = PrefixingStore.create( "local", store );
        PrefixingStore remotePersonaStore = PrefixingStore.create( "remote", store );
        return create( availableCredentialTypes, accountStore , remotePersonaStore );
    }

    /**
     * Factory method.
     *
     * @param accountStore the Store to store LidAccounts in
     * @param remotePersonaStore the Store to map remote personas to LidAccounts in
     * @param availableCredentialTypes the credential types known to the application
     * @return the created StoreSimpleLidAccountManager
     */
    public static StoreSimpleLidAccountManager create(
            LidCredentialType [] availableCredentialTypes,
            Store                accountStore,
            Store                remotePersonaStore )
    {
        SimpleLidAccountMapper accountManager = new SimpleLidAccountMapper( availableCredentialTypes );
        
        Factory<Identifier,SimpleLidAccount,AccountData> accountFactory
                = new AbstractFactory<Identifier,SimpleLidAccount,AccountData>() {
                    public SimpleLidAccount obtainFor(
                            Identifier  identifier,
                            AccountData arg )
                    {
                        SimpleLidAccount ret = SimpleLidAccount.create(
                                identifier,
                                ArrayHelper.copyIntoNewArray( arg.getRemoteIdentifiers(), Identifier.class ),
                                arg.getAttributes(),
                                arg.getCredentialTypes(),
                                arg.getCredentialValues());
                        return ret;
                    }
                };

        StoreBackedSwappingHashMap<Identifier,SimpleLidAccount> accountStorage = StoreBackedSwappingHashMap.createWeak( accountManager, accountStore );
        
        SmartFactory<Identifier,SimpleLidAccount,AccountData> smartAccountFactory
                = new PatientSmartFactory<Identifier,SimpleLidAccount,AccountData>( accountFactory, accountStorage );

        IdentifierMapper identifierMapper = new IdentifierMapper();

        Map<Identifier,Identifier> remoteLocalMap = StoreBackedSwappingHashMap.createWeak( identifierMapper, remotePersonaStore );

        StoreSimpleLidAccountManager ret = new StoreSimpleLidAccountManager(
                smartAccountFactory,
                remoteLocalMap );
        return ret;
    }

    /**
     * Constructor, use factory method.
     * 
     * @param delegateFactory the underlying SmartFactory
     * @param remoteLocalMap maps remote identifiers to local identifiers
     */
    protected StoreSimpleLidAccountManager(
            SmartFactory<Identifier,SimpleLidAccount,AccountData> delegateFactory,
            Map<Identifier,Identifier>                            remoteLocalMap )
    {
        theDelegateFactory = delegateFactory;
        theRemoteLocalMap  = remoteLocalMap;
    }

    /**
     * Provision a LidAccount.
     *
     * @param localIdentifier the Identifier for the to-be-created LidAccount. This may be null, in which case
     *        the LidAccountManager assigns a local Identifier
     * @param remotePersonas the remote personas to be associated with the locally provisioned LidAccount
     * @param attributes the attributes for the to-be-created LidAccount
     * @param credentials the credentials for the to-be-created LidAccount
     * @return the LidAccount that was created
     * @throws LidAccountExistsAlreadyException thrown if a LidAccount with this Identifier exists already
     */
    @Override
    public LidAccount provisionAccount(
            Identifier                    localIdentifier,
            HasIdentifier []              remotePersonas,
            Map<String,String>            attributes,
            Map<LidCredentialType,String> credentials )
        throws
            LidAccountExistsAlreadyException
    {
        ArrayList<Identifier> remoteIdentifiers = new ArrayList<Identifier>( remotePersonas != null ? remotePersonas.length : 0 );
        if( remotePersonas != null ) {
            for( HasIdentifier remote : remotePersonas ) {
                remoteIdentifiers.add( remote.getIdentifier() );
            }
        }
        AccountData attCred = new AccountData( remoteIdentifiers, attributes, credentials );
        
        try {
            LidAccount ret = theDelegateFactory.obtainNewFor( localIdentifier, attCred );
            localIdentifier = ret.getIdentifier();

            if( remotePersonas != null ) {
                for( HasIdentifier remote : remotePersonas ) {
                    theRemoteLocalMap.put( remote.getIdentifier(), localIdentifier );
                }
            }

            return ret;

        } catch( ObjectExistsAlreadyFactoryException ex ) {
            throw new LidAccountExistsAlreadyException( (LidAccount) ex.getExisting(), ex );

        } catch( FactoryException ex ) {
            throw new RuntimeException( ex );
        }
    }

    /**
     * Obtain a HasIdentifier, given its Identifier. This will either return a LidAccount
     * or not. If it returns a LidAccount, the identifier referred to that locally provisioned
     * LidAccount. If it returns something other than a LidAccount, it refers to a remote
     * persona. To determine the LidAccount that may be associated with the remote persona,
     * call determineLidAccountFromRemoteIdentifier.
     *
     * @param identifier the Identifier for which the HasIdentifier will be retrieved
     * @return the found HasIdentifier
     * @throws CannotFindHasIdentifierException thrown if the HasIdentifier cannot be found
     * @throws InvalidIdentifierException thrown if the provided Identifier was invalid for this HasIdentifierFinder
     */
    public HasIdentifier find(
            Identifier identifier )
        throws
            CannotFindHasIdentifierException,
            InvalidIdentifierException
    {
        LidAccount ret = theDelegateFactory.get( identifier );
        if( ret == null ) {
            throw new CannotFindHasIdentifierException( identifier );
        }
        return ret;
    }

    /**
     * Given a remote persona, determine the locally provisioned corresponding
     * LidAccount. May return null if none has been provisioned.
     *
     * @param remote the remote persona
     * @return the found LidAccount, or null
     */
    public LidAccount determineLidAccountFromRemotePersona(
            HasIdentifier remote )
    {
        Identifier local = theRemoteLocalMap.get( remote.getIdentifier() );
        if( local == null ) {
            return null;
        }
        LidAccount ret = theDelegateFactory.get( local );
        return ret;
    }

    /**
     * Delete a LidAccount.
     */
    @Override
    public void delete(
            LidAccount toDelete )
    {
        LidAccount found = theDelegateFactory.remove( toDelete.getIdentifier() );

        Identifier [] remoteIdentifiers = found.getRemoteIdentifiers();
        if( remoteIdentifiers != null ) {
            for( Identifier remote : remoteIdentifiers ) {
                theRemoteLocalMap.remove( remote );
            }
        }
    }

    /**
     * The underlying SmartFactory. This is hidden so we can do access control and
     * expose the API we want.
     */
    protected SmartFactory<Identifier,SimpleLidAccount,AccountData> theDelegateFactory;

    /**
     * The map from remote identifier to local identifier.
     */
    protected Map<Identifier,Identifier> theRemoteLocalMap;
}