package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.domain.docker.DisplayImage;

/**
 * Service for processing Docker image-related api requests.
 */
public interface ImageManager {

    /**
     * Requests a List of all available _Image objects.
     *
     * @return  A a List edu.ucar.unidata.cloudcontrol.domain.docker._Info objects.
     */
    public List<_Image> getImageList();
	
    /**
     * Requests a List of all available _Image objects that the user is allowed to see.
     *
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Info objects.
     */
    public List<_Image> filterByDisplayImage();
	
    /**
     * Determines if Docker Image is flagged for displaying to the user in the interface (e.g., a DisplayImage).
     * 
     * @param imageId   The ID of the Image (will be unique for each DisplayImage). 
     * @return  Whether the Image is a DisplayImage or not.
     */
    public Boolean isDisplayImage(String imageId);
	
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
