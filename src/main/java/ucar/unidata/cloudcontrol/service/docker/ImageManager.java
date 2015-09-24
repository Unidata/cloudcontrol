package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;

import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;


/**
 * Service for processing com.github.dockerjava.api.model.Image objects. 
 */
public interface ImageManager {

    /**
     * Requests a List of all available Images.
     * 
     * @return  A List of available Images.   
     */
    public List<Image> getImageList();

    /**
     * Returns a requested InspectImageResponse.
     * 
     * @param id  The Image ID.
     * @return  The requested InspectImageResponse.  
     */
    public InspectImageResponse inspectImage(String id);

}
