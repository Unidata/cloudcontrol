package edu.ucar.unidata.cloudcontrol.repository.docker;

import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.docker.ImageMapping;

/**
 * The data access object representing a ImageMapping.  
 */

public interface ImageMappingDao {

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
     * @param imageId  The ID of the Image. 
     */
    public void deleteImageMapping(String imageId);

    /**
     * Creates a new ImageMapping in the persistence mechanism.
     * 
     * @param imageMapping  The ImageMapping to be created. 
     */
    public void createImageMapping(ImageMapping imageMapping);

}
