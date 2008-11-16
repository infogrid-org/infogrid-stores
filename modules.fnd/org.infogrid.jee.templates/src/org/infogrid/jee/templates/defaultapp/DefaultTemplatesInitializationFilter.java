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

package org.infogrid.jee.templates.defaultapp;

import org.infogrid.jee.JeeFormatter;
import org.infogrid.jee.defaultapp.DefaultInitializationFilter;
import org.infogrid.jee.templates.DefaultStructuredResponseTemplateFactory;
import org.infogrid.jee.templates.StructuredResponseTemplateFactory;
import org.infogrid.util.context.Context;
import org.infogrid.util.text.SimpleStringRepresentationDirectory;
import org.infogrid.util.text.StringRepresentationDirectory;

/**
 * Configures the default InfoGridWebApp with log4j logging and the template framework.
 */
public class DefaultTemplatesInitializationFilter
        extends
            DefaultInitializationFilter
{
    /**
     * Public constructor.
     */
    public DefaultTemplatesInitializationFilter()
    {
        // nothing right now
    }

    /**
     * Initialize the context objects. This may be overridden by subclasses.
     * 
     * @param rootContext the root Context
     */
    @Override
    protected void initializeContextObjects(
            Context rootContext )
    {
        // Formatter
        JeeFormatter formatter = JeeFormatter.create();
        rootContext.addContextObject( formatter );

        // StructuredResponseTemplateFactory
        StructuredResponseTemplateFactory tmplFactory = DefaultStructuredResponseTemplateFactory.create( "default" );
        rootContext.addContextObject( tmplFactory );

        StringRepresentationDirectory srepdir = SimpleStringRepresentationDirectory.create();
        rootContext.addContextObject( srepdir );
    }
}