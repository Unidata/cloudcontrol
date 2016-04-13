package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing the version information of a Docker instance.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.model.Version 
 * object and the JSON result of a GET /version request to the Docker Remote API.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.model.Version attribute types have been
 * converted to String types to handle: 1) the @CheckForNull getter method annotation
 * in the com.github.dockerjava.api.model.Version object; and 2) custom formatting of data.
 */

public class _Version implements Serializable {

    private String apiVersion;
    private String arch;
    private String gitCommit;
    private String goVersion;
    private String kernelVersion;
    private String operatingSystem;
    private String version;
    private String buildTime;
    private String experimental;


    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        if (Objects.isNull(apiVersion)) {
            this.apiVersion = "";
        } else {
            this.apiVersion = apiVersion;
        } 
    }
    
    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        if (Objects.isNull(arch)) {
            this.arch = "";
        } else {
            this.arch = arch;
        }        
    }
    
    public String getGitCommit() {
        return gitCommit;
    }

    public void setGitCommit(String gitCommit) {
        if (Objects.isNull(gitCommit)) {
            this.gitCommit = "";
        } else {
            this.gitCommit = gitCommit;
        }   
    }
    
    public String getGoVersion() {
        return goVersion;
    }

    public void setGoVersion(String goVersion) {
        if (Objects.isNull(goVersion)) {
            this.goVersion = "";
        } else {
            this.goVersion = goVersion;
        }   
    }
    
    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        if (Objects.isNull(kernelVersion)) {
            this.kernelVersion = "";
        } else {
            this.kernelVersion = kernelVersion;
        }        
    }
    
    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        if (Objects.isNull(operatingSystem)) {
            this.operatingSystem = "";
        } else {
            this.operatingSystem = operatingSystem;
        }        
    }
    
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        if (Objects.isNull(version)) {
            this.version = "";
        } else {
            this.version = version;
        }    
    }
    
    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        if (Objects.isNull(buildTime)) {
            this.buildTime = "";
        } else {
            this.buildTime = buildTime;
        }        
    }
    
    public String getExperimental() {
        return experimental;
    }

    public void setExperimental(Boolean experimental) {
        if (Objects.isNull(experimental)) {
            this.experimental = "";
        } else {
            this.experimental = String.valueOf(experimental);
        }        
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  
}
