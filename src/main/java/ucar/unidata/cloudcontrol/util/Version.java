package edu.ucar.unidata.cloudcontrol.util;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing the version of the software.  
 */
public class Version {

    public final static String CURRENT_VERSION =  Version.class.getPackage().getImplementationVersion();
	
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    } 

}
