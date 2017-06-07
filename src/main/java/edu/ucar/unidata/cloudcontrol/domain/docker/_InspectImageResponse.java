package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Objects;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing Docker image inspection response information.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.command.InspectImageResponse object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.command.InspectImageResponse attribute types have
 * been converted to String types to handle: 1) the @CheckForNull getter method annotation in the 
 * com.github.dockerjava.api.command.InspectImageResponse object; and 2) custom formatting of data.
 */

public class _InspectImageResponse implements Serializable {

    private String arch;
    private String author;
    private String comment;
   // private ContainerConfig config;           // Changed from ContainerConfig          
    private String container;
   // private ContainerConfig containerConfig;  // Changed from ContainerConfig
    private String created;
    private String dockerVersion;
    private String id;
    private String os;
    private String parent;
    private String size;                      // Changed from Long         
    private String repoTags;                  // Changed from List<String>
    private String repoDigests;               // Changed from List<String>
    private String virtualSize;               // Changed from Long  
    private String graphDriver;               // Changed from GraphDriver
	
	
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
	
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (Objects.isNull(author)) {
            this.author = "";
        } else {
            this.author = author;
        }
    }
	
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        if (Objects.isNull(comment)) {
            this.comment = "";
        } else {
            this.comment = comment;
        }
    }
	
    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        if (Objects.isNull(container)) {
            this.container = "";
        } else {
            this.container = container;
        }
    }
	
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        if (Objects.isNull(created)) {
            this.created = "";
        } else {
            this.created = created;
        }
    }
	
    public String getDockerVersion() {
        return dockerVersion;
    }

    public void setDockerVersion(String dockerVersion) {
        if (Objects.isNull(dockerVersion)) {
            this.dockerVersion = "";
        } else {
            this.dockerVersion = dockerVersion;
        }
    }
	
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (Objects.isNull(id)) {
            this.id = "";
        } else {
            this.id = id.substring(0,12);
        }
    }
	
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        if (Objects.isNull(os)) {
            this.os = "";
        } else {
            this.os = os;
        }
    }
	
    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        if (Objects.isNull(parent)) {
            this.parent = "";
        } else {
            this.parent = parent;
        }
    }
	
    public String getSize() {
        return size;
    }

    public void setSize(Long size) {
        if (Objects.isNull(size)) {
            this.size = "";
        } else {       
            if (size > 1000000) {
                this.size = String.valueOf(size.longValue()/1000000) + " MB";
            } else if (size > 1000) {
                this.size = String.valueOf(size.longValue()/1000) + " KB";
            } else {
                this.size = size.toString() + " B";
            }
        }
    }
	
    public String getVirtualSize() {
        return virtualSize;
    }

    public void setVirtualSize(Long virtualSize) {
        if (Objects.isNull(virtualSize)) {
            this.virtualSize = "";
        } else {
            if (virtualSize > 1000000) {
                this.virtualSize = String.valueOf(virtualSize.longValue()/1000000) + " MB";
            } else if (virtualSize > 1000) {
                this.virtualSize = String.valueOf(virtualSize.longValue()/1000) + " KB";
            } else {
                this.virtualSize = virtualSize.toString() + " B";
            }
        } 
    }

    public String getRepoTags() {
        return repoTags;
    }

    public void setRepoTags(List<String> repoTags) {
        if (Objects.isNull(repoTags)) {
            this.repoTags = "";
        } else {
            if (!repoTags.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String s : repoTags) {
                    if (Objects.isNull(s)) {
                        sb.append("");
                    } else {
                        sb.append(s + ", ");
                    }
                }
                this.repoTags = sb.toString();
            } else {
                this.repoTags = "";
            }
        }
    }
	
    public String getRepoDigests() {
        return repoDigests;
    }

    public void setRepoDigests(List<String> repoDigests) {
        if (Objects.isNull(repoDigests)) {
            this.repoDigests = "";
        } else {
            if (!repoDigests.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String s : repoDigests) {
                    if (Objects.isNull(s)) {
                        sb.append("");
                    } else {
                        sb.append(s + ", ");
                    }
                }
                this.repoDigests = sb.toString();
            } else {
                this.repoDigests = "";
            }
        }
    }
	
    public String getGraphDriver() {
        return graphDriver;
    }

    public void setGraphDriver(_GraphDriver graphDriver) {
        if (Objects.isNull(graphDriver)) {
            this.graphDriver = "";
        } else {
            this.graphDriver = graphDriver.toString();
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  
}
