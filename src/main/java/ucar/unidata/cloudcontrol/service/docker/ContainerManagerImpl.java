package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import edu.ucar.unidata.cloudcontrol.domain.docker.NewContainer;


/**
 * Service for processing com.github.dockerjava.api.model.Container objects.
 */
public class ContainerManagerImpl implements ContainerManager {

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
     * Requests a List of all available Containers.
     *
     * @return  A List of available Containers.
     */
    public List<Container> getContainerList() {
        DockerClient dockerClient = initializeDockerClient();
        return dockerClient.listContainersCmd().withShowAll(true).exec();
    }
    
    /**
     * Requests a single Container.
     * 
     * @param id  The Container ID.
     * @return  The Container.   
     */
    public Container getContainer(String id) {
        DockerClient dockerClient = initializeDockerClient();
        List<Container> containers = getContainerList();   
        Container container = null;
        for (Container c : containers) {
            if (id.equals(c.getId())) {
                container = c; 
                break;
            }
        } 
        return container;
    }

    /**
     * Returns a requested InspectContainerResponse.
     *
     * @param id  The Container ID.
     * @return  The requested InspectContainerResponse.
     */
    public InspectContainerResponse inspectContainer(String id) {
        DockerClient dockerClient = initializeDockerClient();
        return dockerClient.inspectContainerCmd(id).exec();
    }

    /**
     * Returns a requested InspectContainerResponse.
     *
     * @param newContainer  The NewContainer object that has the user-specified values.
     * @return  The requested InspectContainerResponse.
     */
    public CreateContainerResponse createContainer(NewContainer newContainer) {
        DockerClient dockerClient = initializeDockerClient();
        ExposedPort port = ExposedPort.tcp(newContainer.getPortNumber());
        Ports portBindings = new Ports();
        portBindings.bind(port, Ports.Binding(11000 + newContainer.getPortNumber()));

        return dockerClient.createContainerCmd(newContainer.getImageRepository())
           .withTty(true)
           .withPublishAllPorts(true)
           .withName(newContainer.getName())
           .withExposedPorts(port)
           .withPortBindings(portBindings)
           .withHostName(newContainer.getHostName())
           .exec();
    }

    /**
     * Starts a Container.
     *
     * @param container  The Container to start.
     */
    public void startContainer(Container container) {
        DockerClient dockerClient = initializeDockerClient();
        dockerClient.startContainerCmd(container.getId()).exec();
    }

    /**
     * Stops a Container.
     *
     * @param container  The Container to stop.
     */
    public void stopContainer(Container container) {
        DockerClient dockerClient = initializeDockerClient();
        dockerClient.stopContainerCmd(container.getId()).exec();
    }

    /**
     * Stops a Container.
     *
     * @param container  The Container to stop.
     * @param timeout  Timeout in seconds before killing the container. Defaults to 10 seconds.
     */
    public void stopContainer(Container container, int timeout) {
        DockerClient dockerClient = initializeDockerClient();
        dockerClient.stopContainerCmd(container.getId()).withTimeout(timeout).exec();
    }

}
