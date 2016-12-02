package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ucar.unidata.cloudcontrol.domain.docker.DisplayImage;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.repository.docker.DisplayImageDao;


/**
 * Service for processing requests associated with DisplayImages.
 */
public class DisplayImageManagerImpl implements DisplayImageManager {
    protected static Logger logger = Logger.getLogger(DisplayImageManagerImpl.class);
    
    private DisplayImageDao displayImageDao;

    /**
     * Sets the data access object which will acquire and persist the data 
     * passed to it via the methods of this ImageManager. 
     * 
     * @param displayImageDao  The service mechanism data access object representing a DisplayImage. 
     */
    public void setDisplayImageDao(DisplayImageDao displayImageDao) {
        this.displayImageDao = displayImageDao;
    }
    
    /**
     * Looks up and retrieves the DisplayImage from the persistence mechanism using the Image ID.
     * 
     * @param imageId   The ID of the Image (will be unique for each DisplayImage). 
     * @return  The DisplayImage.   
     */
    public DisplayImage lookupDisplayImage(String imageId){
        return displayImageDao.lookupDisplayImage(imageId);
    }
            
    /**
     * Looks up and retrieves a List of all DisplayImages from the persistence mechanism.
     * 
     * @return  The List of DisplayImages.   
     */
    public List<DisplayImage> getAllDisplayImages(){
        return displayImageDao.getAllDisplayImages();
    }
    
    /**
     * Finds and removes the DisplayImage from the persistence mechanism using the Image ID.
     * 
     * @param imageId  The Image ID. 
     */
    public void deleteDisplayImage(String imageId) {
        displayImageDao.deleteDisplayImage(imageId);
    }

    /**
     * Creates a new DisplayImage in the persistence mechanism.
     * 
     * @param displayImage  The DisplayImage to be created. 
     */
    public void createDisplayImage(DisplayImage displayImage){
        displayImageDao.createDisplayImage(displayImage);
    }
	
    /**
     * Requests a List of all available _Image objects that the user is allowed to see.
     *
	 * @param _images  The list of _Images to filter.
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Image objects.
     */
    public List<_Image> filterByDisplayImage(List<_Image> _images) {
        List<_Image> displayImages = new ArrayList<_Image>();
        if (_images != null) {
            for (_Image _image : _images) {
                if (_image.getIsDisplayImage()) {
                    displayImages.add(_image);
                }
            }
        }
        return displayImages;
    }
    
    /**
     * Determines if Docker Image is flagged for displaying to the user in the interface (e.g., a DisplayImage).
     * 
     * @param imageId   The ID of the Image (will be unique for each DisplayImage). 
     * @return  Whether the Image is a DisplayImage or not.
     */
    public Boolean isDisplayImage(String imageId){
        if (lookupDisplayImage(imageId) != null) {
            return true;
        } else {    
            return false;
        }
    }
}
