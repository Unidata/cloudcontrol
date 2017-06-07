package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing graph driver information.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.command.GraphDriver object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.command.GraphDriver attribute types have
 * been converted to String types to handle: 1) the @CheckForNull getter method annotation in the 
 * com.github.dockerjava.api.command.GraphDriver object; and 2) custom formatting of data.
 */

public class _GraphDriver implements Serializable {

    private String name;
    private String data;  // Changed from GraphData
	
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (Objects.isNull(name)) {
            this.name = "";
        } else {
            this.name = name;
        }
    }
	
    public String getData() {
        return data;
    }

    public void setData(_GraphData data) {
        if (Objects.isNull(data)) {
            this.data = "";
        } else {
            this.data = data.toString();
        }
    }
	
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  
}
