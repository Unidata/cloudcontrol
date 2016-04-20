package edu.ucar.unidata.cloudcontrol.repository.docker;

import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.docker.DisplayImage;

/**
 * The data access object representing a DisplayImage.  
 */

public interface DisplayImageDao {

    /**
     * Looks up and retrieves the DisplayImage from the persistence mechanism using the Image ID.
     * 
     * @param imageId   The ID of the Image (will be unique for each DisplayImage). 
     * @return  The DisplayImage.   
     */
    public DisplayImage lookupDisplayImage(String imageId);
            
    /**
     * Looks up and retrieves a List of all DisplayImages from the persistence mechanism.
     * 
     * @return  The List of DisplayImages.   
     */
    public List<DisplayImage> getAllDisplayImages();
    
    /**
     * Finds and removes the DisplayImage from the persistence mechanism using the Image ID.
     * 
     * @param imageId  The ID of the Image. 
     */
    public void deleteDisplayImage(String imageId);

    /**
     * Creates a new DisplayImage in the persistence mechanism.
     * 
     * @param displayImage  The DisplayImage to be created. 
     */
    public void createDisplayImage(DisplayImage displayImage);

}
