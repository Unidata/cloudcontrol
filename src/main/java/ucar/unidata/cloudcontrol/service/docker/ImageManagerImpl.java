package edu.ucar.unidata.cloudcontrol.service.docker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;

import edu.ucar.unidata.cloudcontrol.domain.docker.ImageMapping;
import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse;
import edu.ucar.unidata.cloudcontrol.service.docker.ClientManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerMappingManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ImageMappingManager;
import edu.ucar.unidata.cloudcontrol.service.docker.converters.ImageConverter;

/**
 * Service for processing Docker image-related api requests.
 */
public class ImageManagerImpl implements ImageManager {
    protected static Logger logger = Logger.getLogger(ImageManagerImpl.class);
    
    @Resource(name="containerManager")
    private ContainerManager containerManager;
    
    @Resource(name = "clientManager")
    private ClientManager clientManager;
    
    @Resource(name = "imageMappingManager")
    private ImageMappingManager imageMappingManager;


    /**
     * Requests a List of all available _Image objects.
     *
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Image objects.
     */
    public List<_Image> getImageList() {
		List<ImageMapping> imageMappings = imageMappingManager.getAllImageMappings();
        List<_Image> _images = null;
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            List<Image> images = dockerClient.listImagesCmd().withShowAll(false).exec();
            ImageConverter imageConverter = new ImageConverter();
            List<_Image> _convertedImages = imageConverter.processImageList(images); 
			if (_convertedImages != null) {
				Map<String, String> _containerStatusMap = containerManager.getContainerStatusMap();
				_images = new ArrayList<_Image>(_convertedImages.size());
		        for (_Image i : _convertedImages) {
					// check db and see if is a visible to users
                    i.setIsVisibleToUsers(imageMappingManager.isVisibleToUsers(i.getId())); 
					// find the status info of the container to see if the image is running.
	                if (_containerStatusMap.containsKey(i.getId())) {
	                    i.setStatus(_containerStatusMap.get(i.getId())); 
	                }
					_images.add(i); 
		        }
			}
        } catch (Exception e) {
            logger.error("Unable to get list of Images: " + e);
        }            
        return _images;
    }
    
    /**
     * Requests a specific _Image object.
     * 
     * @param imageId  The _Image ID.
     * @return  The _Image.   
     */
    public _Image getImage(String imageId) {
        List<_Image> _images = getImageList(); 
        _Image _image = null;
        for (_Image i : _images) {
            if (imageId.equals(i.getId())) {
                _image = i; 
                break;
            }
        } 
        return _image;
    }
    
    /**
     * Returns the reposnse to a request image request.
     *
     * @param imageId  The Image ID.
     * @return  The edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse object.
     */
    public _InspectImageResponse inspectImage(String imageId) {
        _InspectImageResponse _inspectImageResponse;
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();  
            ImageConverter imageConverter = new ImageConverter();
            _inspectImageResponse = imageConverter.convertInspectImageResponse(inspectImageResponse);
        } catch (Exception e) {
            logger.error("Unable to inspect Image with ID " + imageId + " : " + e);
            _inspectImageResponse = null;
        }
        return _inspectImageResponse;
    }
    
    /**
     * Requests a Map of the statuses of all available _Image objects.
     *
     * @return  A Map edu.ucar.unidata.cloudcontrol.domain.docker._Image statuses.
     */
    public Map<String, String> getImageStatusMap() {
        Map<String, String> idStatusMap = null;
        List<_Image> _images = getImageList();
        if (!_images.isEmpty()) {
            for (_Image _image : _images) {
                if (_image != null) {
                    idStatusMap = new HashMap<String, String>();
                    idStatusMap.put(_image.getId(), _image.getStatus());
                }
            }
            if (idStatusMap.isEmpty()) {
                idStatusMap = null;
            }
        }
        return idStatusMap;
    }
    
    /**
     * Requests a Map of the statuses of the provided List of _Image objects.
     *
     * @param _images  The List of _Images to use when getting the status information.
     * @return  A Map edu.ucar.unidata.cloudcontrol.domain.docker._Image statuses.
     */
    public Map<String, String> getImageStatusMap(List<_Image> _images) {
        Map<String, String> idStatusMap = null;
        if (!_images.isEmpty()) {
            for (_Image _image : _images) {
                if (_image != null) {
                    idStatusMap = new HashMap<String, String>();
                    idStatusMap.put(_image.getId(), _image.getStatus());
                }
            }
            if (idStatusMap.isEmpty()) {
                idStatusMap = null;
            }
        }
        return idStatusMap;
    }
    
    /**
     * Removes an Image from the Docker instance.
     *
     * @param imageId  The ID of the Image to remove.
     * @param userName  The userName of person who is removing the Image (for logging purposes)
     * @return  The whether the Image was successfully removed or not. 
     */
    public boolean removeImage(String imageId, String userName) {
        boolean imageRemoved = true;
        _Image _image = getImage(imageId);
        String repoTags = _image.getRepoTags();
        if (!containerManager.removeContainersFromImage(imageId)) {
            logger.error("Unable to remove containers for Image: " + imageId + ". Will attempt to force the image removal.");
        }    
        if (imageMappingManager.isVisibleToUsers(imageId)) {
            imageMappingManager.deleteImageMapping(imageId);
        }
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            dockerClient.removeImageCmd(imageId).withForce(true).exec();
            _image = getImage(imageId);
            if (_image != null) {
                imageRemoved = false;
            } else {
                Date d = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                logger.info("Image \"" + repoTags + "\" with ID of " + imageId + "has been removed on " + format.format(d) + " by user: " + userName );
            }
        } catch (Exception e) {
            logger.error("Unable to remove Image: " + e);
        }            
        return imageRemoved;
    }
}
