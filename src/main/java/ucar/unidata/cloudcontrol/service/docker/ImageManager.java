package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;
import java.util.Map;

import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse;
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
     * Requests a specific _Image object.
     * 
     * @param imageId  The _Image ID.
     * @return  The _Image.   
     */
    public _Image getImage(String imageId);
	
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
	
    /**
     * Returns the reposnse to a request image request.
     *
     * @param imageId  The Image ID.
     * @return  The edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse object.
     */
    public _InspectImageResponse inspectImage(String imageId);
	
    /**
     * Requests a Map of the statuses of all available _Image objects.
     *
     * @return  A Map edu.ucar.unidata.cloudcontrol.domain.docker._Image statuses.
     */
    public Map<String, String> getImageStatusMap();
	
    /**
     * Requests a Map of the statuses of the provided List of _Image objects.
     *
     * @return  A Map edu.ucar.unidata.cloudcontrol.domain.docker._Image statuses.
     */
    public Map<String, String> getImageStatusMap(List<_Image> images);
	
	
}
