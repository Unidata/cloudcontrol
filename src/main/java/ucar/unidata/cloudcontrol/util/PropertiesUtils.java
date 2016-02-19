package edu.ucar.unidata.cloudcontrol.util;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


/**
 * Utility methods for accessing key/value pairs in the application.properties file. 
 */
public class PropertiesUtils extends PropertyPlaceholderConfigurer {
    private static Map<String,String> propertiesMap = new HashMap<String,String>();
 
    @Override
    protected void loadProperties(final Properties props) throws IOException {
        super.loadProperties(props);
        for (final Object key : props.keySet()) {
            propertiesMap.put(String.valueOf(key), props.getProperty(String.valueOf(key)));
        }
    }

 
    /**
     * Property value accessor method.
     *
     * @param name  The property name (key).
     * @return  The property value.
     */
    public static String getProperty(String name) {
        return String.valueOf(propertiesMap.get(name));
    }
}
