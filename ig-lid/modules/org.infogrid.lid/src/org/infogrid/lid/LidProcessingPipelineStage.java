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

package org.infogrid.lid;

/**
 * A stage in the LidProcessingPipeline.
 */
public interface LidProcessingPipelineStage
{
    /**
     * Name of the lid-meta parameter.
     */
    public static final String LID_META_PARAMETER_NAME = "lid-meta";

    /**
     * Possible value of the lid-meta parameter.
     */
    public static final String LID_META_CAPABILITIES_PARAMETER_VALUE = "capabilities";
}
