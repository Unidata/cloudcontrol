package edu.ucar.unidata.cloudcontrol.service;

import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.DockerImage;


/**
 * Service for processing DockerImage objects. 
 */
public interface DockerImageManager {

    /**
     * Requests a List of all available DockerImages.
     * 
     * @return  A List of available DockerImages.   
     */
    public List<DockerImage> getDockerImageList();

    /**
     * Returns the number of available DockerImages.
     * 
     * @return  The total number of available DockerImages.   
     */
    public int getDockerImageCount();

}
