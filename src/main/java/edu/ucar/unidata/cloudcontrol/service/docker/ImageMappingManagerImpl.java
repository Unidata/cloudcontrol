package edu.ucar.unidata.cloudcontrol.service.docker;

import edu.ucar.unidata.cloudcontrol.domain.docker.ImageMapping;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.repository.docker.ImageMappingDao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataRetrievalFailureException;

/**
 * Service for processing requests associated with ImageMappings.
 */
public class ImageMappingManagerImpl implements ImageMappingManager {

    private ImageMappingDao imageMappingDao;

    /**
     * Sets the data access object which will acquire and persist the data
     * passed to it via the methods of this ImageManager.
     *
     * @param imageMappingDao  The service mechanism data access object representing a ImageMapping.
     */
    public void setImageMappingDao(ImageMappingDao imageMappingDao) {
        this.imageMappingDao = imageMappingDao;
    }

    /**
     * Looks up and retrieves the ImageMapping from the persistence mechanism using the Image ID.
     *
     * @param imageId   The ID of the Image (will be unique for each ImageMapping).
     * @return  The ImageMapping.
     */
    public ImageMapping lookupImageMapping(String imageId){
        return imageMappingDao.lookupImageMapping(imageId);
    }

    /**
     * Looks up and retrieves a List of all ImageMappings from the persistence mechanism.
     *
     * @return  The List of ImageMappings.
     */
    public List<ImageMapping> getAllImageMappings(){
        return imageMappingDao.getAllImageMappings();
    }

    /**
     * Finds and removes the ImageMapping from the persistence mechanism using the Image ID.
     *
     * @param imageId  The Image ID.
     */
    public void deleteImageMapping(String imageId) {
        imageMappingDao.deleteImageMapping(imageId);
    }

    /**
     * Creates a new ImageMapping in the persistence mechanism.
     *
     * @param imageMapping  The ImageMapping to be created.
     */
    public void createImageMapping(ImageMapping imageMapping){
        imageMappingDao.createImageMapping(imageMapping);
    }

    /**
     * Requests a List of all available _Image objects that the user is allowed to see.
     *
     * @param _images  The list of _Images to filter.
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Image objects.
     */
    public List<_Image> filterByImageMapping(List<_Image> _images) {
        List<_Image> imageMappings = new ArrayList<_Image>();
        if (!_images.isEmpty()) {
            imageMappings = new ArrayList<_Image>();
            for (_Image _image : _images) {
                if (_image.getIsVisibleToUsers()) {
                    imageMappings.add(_image);
                }
            }
        }
        return imageMappings;
    }

    /**
     * Determines if Docker Image is flagged for displaying to the user in the interface (e.g., a ImageMapping).
     *
     * @param imageId   The ID of the Image (will be unique for each ImageMapping).
     * @return  Whether the Image is a ImageMapping or not.
     */
    public boolean isVisibleToUser(String imageId){
        try {
            lookupImageMapping(imageId);
            return true;
        } catch (DataRetrievalFailureException e) {
            return false;
        }
    }
}
