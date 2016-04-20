package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing an image the admin has flaged for display in the user interface.
 */

public class DisplayImage implements Serializable {

    private int id;
    private String imageId;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
	
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  
}
