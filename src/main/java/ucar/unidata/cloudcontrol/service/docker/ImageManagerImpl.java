package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Image;

import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker.DisplayImage;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.repository.docker.DisplayImageDao;
import edu.ucar.unidata.cloudcontrol.service.docker.ClientManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerManager;


/**
 * Service for processing Docker image-related api requests.
 */
public class ImageManagerImpl implements ImageManager {
    protected static Logger logger = Logger.getLogger(ImageManagerImpl.class);
    
    @Resource(name="containerManager")
    private ContainerManager containerManager;
    
    @Resource(name = "clientManager")
    private ClientManager clientManager;
	
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
     * Requests a List of all available _Image objects.
     *
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Info objects.
     */
    public List<_Image> getImageList() {
        List<_Image> _images; 
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            List<Image> images = dockerClient.listImagesCmd().withShowAll(false).exec();
            _images = processImageList(images); 
        } catch (Exception e) {
            logger.error("Unable to get list of Images: " + e);
            _images = null;
        }            
        return _images;
    }
	
    /**
     * Requests a List of all available _Image objects that the user is allowed to see.
     *
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Info objects.
     */
    public List<_Image> filterByDisplayImage() {
		List<_Image> displayImages = new ArrayList<_Image>();
        List<_Image> _images = getImageList();
        if (!Objects.isNull(_images)) {
	        for (_Image _image : _images) {
	            if (!_image.getIsDisplayImage()) {
	            	displayImages.add(_image);
	            }
	        }
        }
        return displayImages;
    }

    /**
     * Utility method to process a List of com.github.dockerjava.api.model.Image objects
     * to a corresponding List of edu.ucar.unidata.cloudcontrol.domain.docker._Image objects.
     *
     * @param images  The com.github.dockerjava.api.model.Image List.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Image List.
     */
    public List<_Image> processImageList(List<Image> images) {
        List<_Image> _images = new ArrayList<_Image>(images.size());
        for (Image image : images) {
            _Image _image = convertImage(image);
            if (!Objects.isNull(_image)) {
                _images.add(_image);
            }
        }
        if (_images.isEmpty()) {
            _images = null;
        }
        return _images;
    }
    
    /**
     * Converts a com.github.dockerjava.api.model.Image object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._Image object.
     *
     * @param image  The com.github.dockerjava.api.model.Image object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Image object.
     */
    public _Image convertImage(Image image) {
        Function<Image, _Image> mapImageTo_Image = new Function<Image, _Image>() {
            public _Image apply(Image i) {
                _Image _image = new _Image();
                _image.setCreated(i.getCreated());
                _image.setId(i.getId());
                _image.setParentId(i.getParentId());
                _image.setRepoTags(i.getRepoTags());
                _image.setSize(i.getSize());
                _image.setVirtualSize(i.getVirtualSize());
                return _image;
            }
        };
        _Image _image = null;
        Map<String, String> _containerStatusMap = containerManager.getContainerStatusMap();
        if (!Objects.isNull(_containerStatusMap)) {
            _image = mapImageTo_Image.apply(image);
            if (_containerStatusMap.containsKey(_image.getRepoTags())) {
                _image.setStatus(_containerStatusMap.get(_image.getRepoTags()));          
            }
            DisplayImage displayImage = lookupDisplayImage(_image.getId());
			if (!Objects.isNull(displayImage)) {
				_image.setIsDisplayImage(true);
			}
        }
        return _image;
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
     * @param imageId  The ID of the Image. 
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
}
