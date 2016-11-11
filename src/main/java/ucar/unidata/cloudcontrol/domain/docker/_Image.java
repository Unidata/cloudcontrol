package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing a Docker image.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.model.Image object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.model.Image attribute types have been
 * converted to String types to handle: 1) checking for null values passed from the 
 * com.github.dockerjava.api.model.Info object; and 2) custom formatting of data.
 */
public class _Image implements Serializable {

    private String created;                  // Changed from Long
    private String id;
    private String parentId;
    private String repoTags;                 // Changed from String[]
    private String size;                     // Changed from Long
    private String virtualSize;              // Changed from Long
    private String status = "Inactive";      // CloudControl attribute
    private boolean isDisplayImage = false;  // CloudControl attribute

    public String getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        if (Objects.isNull(created)) {
            this.created = "";
        } else {
            Date d = new Date();
            d.setTime(created.longValue() * 1000);
            SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
            this.created = format.format(d);
        }        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (Objects.isNull(id)) {
            this.id = "";
        } else {
            this.id = id.substring(0,11);
        }        
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        if (Objects.isNull(parentId)) {
            this.parentId = "";
        } else {
            this.parentId = parentId;
        }        
    }

    public String getRepoTags() {
        return repoTags;
    }

    public void setRepoTags(String[] repoTags) {
        if (Objects.isNull(repoTags)) {
            this.repoTags = "";
        } else {
            if (repoTags.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (Iterator<String> it = Arrays.asList(repoTags).iterator(); it.hasNext(); ) {
                    String s = it.next();
                    if (Objects.isNull(s)) {
                        sb.append("");
                    } else {
                        if (it.hasNext()) {
                            sb.append(s + ", ");
                        } else {
                            sb.append(s);                            
                        }
                    }
                }
                this.repoTags = sb.toString();
            } else {
                this.repoTags = "";
            }
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        if (Objects.isNull(status)) {
            this.status = "";
        } else {
            this.status = status;
        }        
    }
    
    public boolean getIsDisplayImage() {
        return isDisplayImage;
    }
    
    public void setIsDisplayImage(boolean isDisplayImage) {
        this.isDisplayImage = isDisplayImage;      
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  
}
