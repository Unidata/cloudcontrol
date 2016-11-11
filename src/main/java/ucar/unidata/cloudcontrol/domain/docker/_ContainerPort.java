package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing Docker container port information.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.model.ContainerPort object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.model.ContainerPort attribute types have 
 * been converted to String types to handle: 1) the @CheckForNull getter annotation in the 
 * com.github.dockerjava.api.model.ContainerPort object; and 2) custom formatting of data.
 */
public class _ContainerPort implements Serializable {

    private String ip;
    private String privatePort;  // Changed from Integer
    private String publicPort;   // Changed from Integer
    private String type;
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        if (Objects.isNull(ip)) {
            this.ip = "";
        } else {
            this.ip = ip;
        }
    }

    public String getPrivatePort() {
        return privatePort;
    }

    public void setPrivatePort(Integer privatePort) {
        if (Objects.isNull(privatePort)) {
            this.privatePort = "";
        } else {
            this.privatePort = String.valueOf(privatePort);
        }
    }

    public String getPublicPort() {
        return publicPort;
    }

    public void setPublicPort(Integer publicPort) {
        if (Objects.isNull(publicPort)) {
            this.publicPort = "";
        } else {
            this.publicPort = String.valueOf(publicPort);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (Objects.isNull(type)) {
            this.type = "";
        } else {
            this.type = type;
        }
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  

}
