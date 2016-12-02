package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.ArrayList;
import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.docker.DisplayImage;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;

/**
 * Service for processing requests associated with DisplayImages.
 */
public interface DisplayImageManager {
    
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
     * @param imageId  The Image ID. 
     */
    public void deleteDisplayImage(String imageId);

    /**
     * Creates a new DisplayImage in the persistence mechanism.
     * 
     * @param displayImage  The DisplayImage to be created. 
     */
    public void createDisplayImage(DisplayImage displayImage);
    
    /**
     * Requests a List of all available _Image objects that the user is allowed to see.
     *
	 * @param _images  The list of _Images to filter.
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Image objects.
     */
    public List<_Image> filterByDisplayImage(List<_Image> _images) ;
    
    /**
     * Determines if Docker Image is flagged for displaying to the user in the interface (e.g., a DisplayImage).
     * 
     * @param imageId   The ID of the Image (will be unique for each DisplayImage). 
     * @return  Whether the Image is a DisplayImage or not.
     */
    public Boolean isDisplayImage(String imageId);
}
