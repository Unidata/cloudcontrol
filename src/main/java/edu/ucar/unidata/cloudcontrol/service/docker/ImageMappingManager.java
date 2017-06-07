package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.ArrayList;
import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.docker.ImageMapping;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;

/**
 * Service for processing requests associated with ImageMappings.
 */
public interface ImageMappingManager {
    
    /**
     * Looks up and retrieves the ImageMapping from the persistence mechanism using the Image ID.
     * 
     * @param imageId   The ID of the Image (will be unique for each ImageMapping). 
     * @return  The ImageMapping.   
     */
    public ImageMapping lookupImageMapping(String imageId);
            
    /**
     * Looks up and retrieves a List of all ImageMappings from the persistence mechanism.
     * 
     * @return  The List of ImageMappings.   
     */
    public List<ImageMapping> getAllImageMappings();
    
    /**
     * Finds and removes the ImageMapping from the persistence mechanism using the Image ID.
     * 
     * @param imageId  The Image ID. 
     */
    public void deleteImageMapping(String imageId);

    /**
     * Creates a new ImageMapping in the persistence mechanism.
     * 
     * @param imageMapping  The ImageMapping to be created. 
     */
    public void createImageMapping(ImageMapping imageMapping);
    
    /**
     * Requests a List of all available _Image objects that the user is allowed to see.
     *
	 * @param _images  The list of _Images to filter.
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Image objects.
     */
    public List<_Image> filterByImageMapping(List<_Image> _images) ;
    
    /**
     * Determines if Docker Image is flagged for displaying to the user in the interface (e.g., a ImageMapping).
     * 
     * @param imageId   The ID of the Image (will be unique for each ImageMapping). 
     * @return  Whether the Image is a ImageMapping or not.
     */
    public boolean isVisibleToUser(String imageId);
}
