package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerManager;


/**
 * Service for processing Docker image-related api requests.
 */
public class ImageManagerImpl implements ImageManager {
    protected static Logger logger = Logger.getLogger(ImageManagerImpl.class);
    
    @Resource(name="containerManager")
    private ContainerManager containerManager;

    /**
     * Initializes a com.github.dockerjava.api.DockerClient.
     *
     * @return  A DockerClient object.
     */
    public DockerClient initializeDockerClient() {
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder().build();
        return DockerClientBuilder.getInstance(config).build();
    }


    /**
     * Requests a List of all available _Image objects.
     *
     * @return  A a List edu.ucar.unidata.cloudcontrol.domain.docker._Info objects.
     */
    public List<_Image> getImageList() {
        DockerClient dockerClient = initializeDockerClient();
        List<Image> images = dockerClient.listImagesCmd().withShowAll(false).exec();
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
        Map<String, String> _containerStatusMap = containerManager.getContainerStatusMap();
        List<_Image> _images = new ArrayList<_Image>(images.size());
        for (Image image : images) {
            _Image _image = mapImageTo_Image.apply(image);
            if (_containerStatusMap.containsKey(_image.getRepoTags())) {
                _image.setStatus(_containerStatusMap.get(_image.getRepoTags()));          
            }
            _images.add(_image);
        }
        return _images;
    }
    
    /**
     * Requests a single Image.
     * 
     * @param id  The Image ID.
     * @return  The Image.   
     */
/*
    public Image getImage(String id) {
        DockerClient dockerClient = initializeDockerClient();
        List<Image> images = getImageList();   
        Image image = null;
        for (Image i : images) {
            if (id.equals(i.getId())) {
                image = i; 
                break;
            }
        } 
        return image;
    }
*/
     
    /**
     * Requests an Image's repository.
     * 
     * @param image  The Image.
     * @return  The Image repository (String).  
     */
/*
    public String getImageRepository(Image image) {
        DockerClient dockerClient = initializeDockerClient();
        String id = image.getId();
        String repository = null;
        List<Image> images = getImageList();   
        for (Image i : images) {
            if (id.equals(i.getId())) {
                String[] repoTags = i.getRepoTags();
                String[] splitString = repoTags[0].split(":");     
                repository = splitString[0];
                break;
            }
        } 
        return repository;
    }
*/
    
    /**
     * Requests an Image's Container ID.
     * 
     * @param image  The Image.
     * @return  The Image Container ID (String).  
     */
/*
    public String getImageContainerId(Image image) {
        InspectImageResponse inspectImageResponse = inspectImage(image.getId());
        return inspectImageResponse.getContainer();
    }
*/
    
     
    /**
     * Requests a Map of the Image's repositories (for form dropdown selection).
     * 
     * @return  The repository Map<String,String>.
     */
/*
    public Map<String,String> getRepositoryMap() {
        DockerClient dockerClient = initializeDockerClient();
        Map<String,String> imageMap = new LinkedHashMap<String,String>();
        List<Image> imageList = getImageList();
        for (Image i : imageList) {
            String repo = getImageRepository(i);
            imageMap.put(repo, repo);
        }
        return imageMap;
    }   
*/
    
    /**
     * Requests a search of the available Images with a given query.
     * 
     * @param query  The query upon which to search.
     * @return  A List of resulting SearchItems.
     */
/*
    public List<SearchItem> searchImages(String query) {
        DockerClient dockerClient = initializeDockerClient();
        return dockerClient.searchImagesCmd(query).exec();
    }
    
   
    /**
     * Returns a requested InspectImageResponse.
     *
     * @param id  The Image ID.
     * @return  The requested InspectImageResponse.
     */
/*
    public InspectImageResponse inspectImage(String id) {
        DockerClient dockerClient = initializeDockerClient();
        return dockerClient.inspectImageCmd(id).exec();
    }
*/
}
