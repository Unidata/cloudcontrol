package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing Docker container host configuration information.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.model.ContainerHostConfig object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.model.ContainerHostConfig attribute types have 
 * been converted to String types to handle: 1) checking for null values passed from the 
 * com.github.dockerjava.api.model.ContainerHostConfig object; and 2) custom formatting of data.
 */
public class _ContainerHostConfig implements Serializable {

    private String networkMode;
    
    public String getNetworkMode() {
        return networkMode;
    }
 
    public void setNetworkMode(String networkMode) {
        if (Objects.isNull(networkMode)) {
            this.networkMode = "";
        } else {
            this.networkMode = networkMode;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  

}
