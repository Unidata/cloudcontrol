package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing graph data information.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.command.GraphData object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.command.GraphData attribute types have
 * been converted to String types to handle: 1) the @CheckForNull getter method annotation in the 
 * com.github.dockerjava.api.command.GraphData object; and 2) custom formatting of data.
 */

public class _GraphData implements Serializable {

    private String rootDir;
    private String deviceId;
    private String deviceName;
    private String deviceSize;
    
    
    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        if (Objects.isNull(rootDir)) {
            this.rootDir = "";
        } else {
            this.rootDir = rootDir;
        }
    }
	
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        if (Objects.isNull(deviceId)) {
            this.deviceId = "";
        } else {
            this.deviceId = deviceId;
        }
    }
	
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        if (Objects.isNull(deviceName)) {
            this.deviceName = "";
        } else {
            this.deviceName = deviceName;
        }
    }
	
    public String getDeviceSize() {
        return deviceSize;
    }

    public void setDeviceSize(String deviceSize) {
        if (Objects.isNull(deviceSize)) {
            this.deviceSize = "";
        } else {
            this.deviceSize = deviceSize;
        }
    }
	
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  
}
