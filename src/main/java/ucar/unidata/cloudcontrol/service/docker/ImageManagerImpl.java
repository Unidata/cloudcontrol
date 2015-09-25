package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;
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
