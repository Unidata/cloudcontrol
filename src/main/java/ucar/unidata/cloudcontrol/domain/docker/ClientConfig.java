package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing information for Docker client configuration.
 */

public class ClientConfig implements Serializable {

    private int id;
    private String dockerHost;
    private String dockerCertPath;
    private int dockerTlsVerify = 1;
    private String createdBy;
    private String lastUpdatedBy;
    private Date dateCreated;
    private Date dateModified;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getDockerHost() {
        return dockerHost;
    }

    public void setDockerHost(String dockerHost) {
        this.dockerHost = dockerHost;
    }

    public String getDockerCertPath() {
        return dockerCertPath;
    }

    public void setDockerCertPath(String dockerCertPath) {
       this.dockerCertPath = dockerCertPath;
    }

    public int getDockerTlsVerify() {
        return dockerTlsVerify;
    }

    public void setDockerTlsVerify(int dockerTlsVerify) {
        this.dockerTlsVerify = dockerTlsVerify;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getDateCreated() {
        return dateCreated;
    }
       
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }
       
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  
}
