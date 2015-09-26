package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;


/**
 * Service for processing com.github.dockerjava.api.model.Image objects.
 */
public class ImageManagerImpl implements ImageManager {

    /**
     * Initializes a com.github.dockerjava.api.DockerClient.
     *
     * @return  A DockerClient object.
     */
    public DockerClient initializeDockerClient() {
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
        .withVersion("1.17")
        .build();
    return DockerClientBuilder.getInstance(config).build();
    }


    /**
     * Requests a List of all available Images.
     *
     * @return  A List of available Images.
     */
    public List<Image> getImageList() {
        DockerClient dockerClient = initializeDockerClient();
        return dockerClient.listImagesCmd().withShowAll(false).exec();
    }
    
    /**
     * Requests a single Image.
     * 
     * @param id  The Image ID.
     * @return  The Image.   
     */
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
     
    /**
     * Requests an Image's repository.
     * 
     * @param image  The Image.
     * @return  The Image repository (String).  
     */
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
     
    /**
     * Requests a Map of the Image's repositories (for form dropdown selection).
     * 
     * @return  The repository Map<String,String>.
     */
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
    
    /**
     * Requests a search of the available Images with a given query.
     * 
     * @param query  The query upon which to search.
     * @return  A List of resulting SearchItems.
     */
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
    public InspectImageResponse inspectImage(String id) {
        DockerClient dockerClient = initializeDockerClient();
        return dockerClient.inspectImageCmd(id).exec();
    }
}
