package edu.ucar.unidata.cloudcontrol.domain;

import java.io.Serializable;

/**
 * Object representing a rudimentary DockerImage.  
 *
 * The DockerImage attributes correspond with
 * results from the commandline 'DockerImages' 
 */

public class DockerImage implements Serializable {

    private String repository;
    private String tag;
	private String imageId;
    private String created;
	private String virtualSize;

    /**
     * Returns the originating repository of the DockerImage.
     * 
     * @return  The originating repository. 
     */
    public String getRepository() {
        return repository;
    }

    /**
     * Sets the originating repository of the DockerImage.
     * 
     * @param repository  The originating repository. 
     */
    public void setRepository(String repository) {
        this.repository = repository;
    }

    /**
     * Returns the DockerImage tag.
     * 
     * @return  The tag.  
     */
    public String getTag() {
        return tag;
    }

    /**
     * Sets the DockerImage tag.
     * 
     * @param tag  The tag. 
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Returns the DockerImage ID.
     * 
     * @return  The imageId.  
     */
    public String getImageId() {
        return imageId;
    }

    /**
     * Sets the DockerImage ID.
     * 
     * @param imageId  The imageId. 
     */
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    /**
     * Returns when the DockerImage was created.
     * 
     * @return  When the DockerImage was created.
     */
    public String getCreated() {
        return created;
    }

    /**
     * Sets the time when the DockerImage was created.
     * 
     * @param created  When the DockerImage was created.
     */
    public void setCreated(String created) {
        this.created = created;
    }
	
    /**
     * Returns the virtualSize of the DockerImage.
     * 
     * @return  The virtualSize. 
     */
    public String getVirtualSize() {
        return virtualSize;
    }

    /**
     * Sets the virtualSize of the DockerImage.
     * 
     * @param virtualSize  The virtualSize. 
     */
    public void setVirtualSize(String virtualSize) {
        this.virtualSize = virtualSize;
    }

    /**
     * String representation of a DockerImage object.
     * The DockerImage attributes correspond with
     * results from the commandline 'DockerImages' 
     */ 
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("repository: " + repository + "\n ");
        buffer.append("tag: " + tag + "\n ");
        buffer.append("imageId: " + imageId + "\n ");
        buffer.append("created: " + created + "\n ");     
		buffer.append("virtualSize: " + virtualSize + "\n ");    
        return buffer.toString();
    }


}
