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

package org.infogrid.model.primitives;

import java.io.ObjectStreamException;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringifierException;

/**
  * This is an string DataType for PropertyValues. While this DataType does not limit
  * the length of a string, it is typically only used for "short" strings. For multi-line strings,
  * use BlobDataType with a text MIME type.
  */
public final class StringDataType
        extends
            DataType
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * This is the default instance of this class.
      */
    public static final StringDataType theDefault = new StringDataType();

    /**
     * Factory method. Always returns the same instance.
     *
     * @return the default instance of this class
     */
    public static StringDataType create()
    {
        return theDefault;
    }

    /**
      * Private constructor, there is no reason to instatiate this more than once.
      */
    private StringDataType()
    {
        super( null );
    }

    /**
      * Test for equality.
      *
      * @param other object to test against
      * @return true if the two objects are equal
      */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof StringDataType ) {
            return true;
        }
        return false;
    }

    /**
     * Determine whether this PropertyValue conforms to this DataType.
     *
     * @param value the candidate PropertyValue
     * @return true if the candidate PropertyValue conforms to this type
     */
    public boolean conforms(
            PropertyValue value )
    {
        if( value instanceof StringValue ) {
            return true;
        }
        return false;
    }

    /**
      * Obtain the Java class that can hold values of this data type.
      *
      * @return the Java class that can hold values of this data type
      */
    public Class getCorrespondingJavaClass()
    {
        return StringValue.class;
    }

    /**
      * Instantiate this data type into a PropertyValue with a
      * reasonable default value.
      *
      * @return a PropertyValue with a reasonable default value that is an instance of this DataType
      */
    public PropertyValue instantiate()
    {
        return theDefaultValue;
    }

    /**
     * Obtain the default value of this DataType.
     *
     * @return the default value of this DataType
     */
    public PropertyValue getDefaultValue()
    {
        return theDefaultValue;
    }

    /**
     * Correctly deserialize a static instance.
     *
     * @return the static instance if appropriate
     * @throws ObjectStreamException thrown if reading from the stream failed
     */
    public Object readResolve()
        throws
            ObjectStreamException
    {
        if( this.equals( theDefault )) {
            return theDefault;
        } else {
            return this;
        }
    }

    /**
     * Obtain a value expression in the Java language that invokes the constructor
     * of factory method of the underlying concrete class, thereby creating or
     * reusing an instance of the underlying concrete class that is identical
     * to the instance on which this method was invoked.
     *
     * This is used primarily for code-generation purposes.
     *
     * @param classLoaderVar name of a variable containing the class loader to be used to initialize this value
     * @return the Java language expression
     */
    public String getJavaConstructorString(
            String classLoaderVar )
    {
        final String className = getClass().getName();

        return className + DEFAULT_STRING;
    }

    /**
     * Convert this PropertyValue to its String representation, using the representation scheme.
     *
     * @param representation the representation scheme
     * @return the String representation
     */
    public String toStringRepresentation(
            StringRepresentation representation )
    {
        return representation.formatEntry(
                RESOURCEHELPER,
                DEFAULT_ENTRY,
                PropertyValue.toStringRepresentation( theDefaultValue, representation ),
                theSupertype );
    }

    /**
     * Obtain a PropertyValue that corresponds to this PropertyType, based on the String representation
     * of the PropertyValue.
     * 
     * @param representation the StringRepresentation in which the String s is given
     * @param s the String
     * @return the PropertyValue
     * @throws PropertyValueParsingException thrown if the String representation could not be parsed successfully
     */
    public StringValue fromStringRepresentation(
            StringRepresentation representation,
            String                      s )
        throws
            PropertyValueParsingException
    {
        try {
            Object [] found = representation.parseEntry( StringValue.RESOURCEHELPER, StringValue.DEFAULT_ENTRY, s );

            StringValue ret;
            switch( found.length ) {
                case 1:
                    ret = StringValue.create( (String) found[0] );
                    break;

                default:
                    throw new PropertyValueParsingException( this, representation, s );
            }

            return ret;

        } catch( StringifierException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex );

        } catch( ClassCastException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex );
        }
    }

    /**
     * The default value.
     */
    private static final StringValue theDefaultValue = StringValue.create( "" );
    
    /**
     * Our ResourceHelper.
     */
    static final ResourceHelper RESOURCEHELPER = StringValue.RESOURCEHELPER;
}