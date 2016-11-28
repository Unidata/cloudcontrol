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
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.GraphData;
import com.github.dockerjava.api.command.GraphDriver;
import com.github.dockerjava.api.model.Image;

import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker.DisplayImage;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse;
import edu.ucar.unidata.cloudcontrol.domain.docker._GraphData;
import edu.ucar.unidata.cloudcontrol.domain.docker._GraphDriver;
import edu.ucar.unidata.cloudcontrol.repository.docker.DisplayImageDao;
import edu.ucar.unidata.cloudcontrol.service.docker.ClientManager;


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
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Image objects.
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
        // populate other vaiables not in the original com.github.dockerjava.api.model.Image object
        _Image _image = null;
        // find the status info of the container to see if the image is running.
        Map<String, String> _containerStatusMap = containerManager.getContainerStatusMap();
        if (_containerStatusMap != null) { //coverity 
            _image = mapImageTo_Image.apply(image);
            if (!Objects.isNull(_image)) {
                if (_containerStatusMap.containsKey(_image.getId())) {
                    _image.setStatus(_containerStatusMap.get(_image.getId())); 
                }
            }
        }
        // query the db and see if is DisplayImage
        _image.setIsDisplayImage(isDisplayImage(_image.getId()));    
        return _image;
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
     * Requests a List of all available _Image objects that the user is allowed to see.
     *
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Image objects.
     */
    public List<_Image> filterByDisplayImage() {
        List<_Image> displayImages = new ArrayList<_Image>();
        List<_Image> _images = getImageList();
        if (!Objects.isNull(_images)) {
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
        if (!Objects.isNull(lookupDisplayImage(imageId))) {
            return true;
        } else {
            return false;
        }
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
            _inspectImageResponse = convertInspectImageResponse(inspectImageResponse);
        } catch (Exception e) {
            logger.error("Unable to inspect Image with ID " + imageId + " : " + e);
            _inspectImageResponse = null;
        }
        return _inspectImageResponse;
    }
    
    /**
     * Converts a com.github.dockerjava.api.command.InspectImageResponse object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse object.
     *
     * @param inspectImageResponse  The com.github.dockerjava.api.command.InspectImageResponse object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse object.
     */
    public _InspectImageResponse convertInspectImageResponse(InspectImageResponse inspectImageResponse) {
        Function<InspectImageResponse, _InspectImageResponse> mapInspectImageResponseTo_InspectImageResponse = new Function<InspectImageResponse, _InspectImageResponse>() {
		    public _InspectImageResponse apply(InspectImageResponse i) {
              _InspectImageResponse _inspectImageResponse = new _InspectImageResponse();
              _inspectImageResponse.setArch(i.getArch());
              _inspectImageResponse.setAuthor(i.getAuthor());
              _inspectImageResponse.setComment(i.getComment());
              //_inspectImageResponse.setConfig(i.getConfig());
              _inspectImageResponse.setContainer(i.getContainer());
              //_inspectImageResponse.setContainerConfig(i.getContainerConfig());
              _inspectImageResponse.setCreated(i.getCreated());
              _inspectImageResponse.setDockerVersion(i.getDockerVersion());
              _inspectImageResponse.setId(i.getId());
              _inspectImageResponse.setOs(i.getOs());
              _inspectImageResponse.setParent(i.getParent());
              _inspectImageResponse.setSize(i.getSize());
              _inspectImageResponse.setRepoTags(i.getRepoTags());
              _inspectImageResponse.setRepoDigests(i.getRepoDigests());
              _inspectImageResponse.setVirtualSize(i.getVirtualSize());
             // _inspectImageResponse.setGraphDriver(convertGraphDriver(i.getGraphDriver()));
              return _inspectImageResponse;
            }
        };
        _InspectImageResponse _inspectImageResponse = mapInspectImageResponseTo_InspectImageResponse.apply(inspectImageResponse);
        return _inspectImageResponse;
    }
	
    /**
     * Converts a com.github.dockerjava.api.command.GraphDriver object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._GraphDriver object.
     *
     * @param graphDriver  The com.github.dockerjava.api.command.GraphDriver object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._GraphDriver object.
     */
    public _GraphDriver convertGraphDriver(GraphDriver graphDriver) {
        Function<GraphDriver, _GraphDriver> mapGraphDriverTo_GraphDriver = new Function<GraphDriver, _GraphDriver>() {
            public _GraphDriver apply(GraphDriver g) {
				_GraphDriver _graphDriver = new _GraphDriver();
                _graphDriver.setName(g.getName());
				_graphDriver.setData(convertGraphData(g.getData()));
                return _graphDriver;
            }
        };
        _GraphDriver _graphDriver = mapGraphDriverTo_GraphDriver.apply(graphDriver);
        return _graphDriver;
    }
	
    /**
     * Converts a com.github.dockerjava.api.command.GraphData object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._GraphData object.
     *
     * @param graphData  The com.github.dockerjava.api.command.GraphData object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._GraphData object.
     */
    public _GraphData convertGraphData(GraphData graphData) {
        Function<GraphData, _GraphData> mapGraphDataTo_GraphData = new Function<GraphData, _GraphData>() {
            public _GraphData apply(GraphData g) {
                _GraphData _graphData = new _GraphData();
                _graphData.setRootDir(g.getRootDir());
                _graphData.setDeviceId(g.getDeviceId());
                _graphData.setDeviceName(g.getDeviceName());
                _graphData.setDeviceSize(g.getDeviceSize());
                return _graphData;
            }
        };
        _GraphData _graphData = mapGraphDataTo_GraphData.apply(graphData);
        return _graphData;
    }
	
    /**
     * Requests a Map of the statuses of all available _Image objects.
     *
     * @return  A Map edu.ucar.unidata.cloudcontrol.domain.docker._Image statuses.
     */
    public Map<String, String> getImageStatusMap() {
		Map<String, String> idStatusMap = new HashMap<String, String>();
        List<_Image> _images = getImageList();
        for (_Image _image : _images) {
            if (!Objects.isNull(_image)) {
                idStatusMap.put(_image.getId(), _image.getStatus());
            }
        }
        if (idStatusMap.isEmpty()) {
            idStatusMap = null;
        }
        return idStatusMap;
    }
	
    /**
     * Requests a Map of the statuses of the provided List of _Image objects.
     *
     * @return  A Map edu.ucar.unidata.cloudcontrol.domain.docker._Image statuses.
     */
    public Map<String, String> getImageStatusMap(List<_Image> _images) {
		Map<String, String> idStatusMap = new HashMap<String, String>();
        for (_Image _image : _images) {
            if (!Objects.isNull(_image)) {
                idStatusMap.put(_image.getId(), _image.getStatus());
            }
        }
        if (idStatusMap.isEmpty()) {
            idStatusMap = null;
        }
        return idStatusMap;
    }
}
