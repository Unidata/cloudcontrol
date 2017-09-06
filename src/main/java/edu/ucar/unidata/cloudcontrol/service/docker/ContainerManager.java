package edu.ucar.unidata.cloudcontrol.service.docker;

import edu.ucar.unidata.cloudcontrol.domain.docker._Container;

import java.util.List;
import java.util.Map;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;

/**
 * Service for processing Docker container-related api requests.
 */
public interface ContainerManager {

    /**
     * Requests a List of all available containers.
     *
     * @return  A a List edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getContainerList();

    /**
     * Requests a List of all available containers corresponding to an image.
     *
     * @param imageId  The container's Image ID.
     * @return  A List of edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getContainerListByImage(String imageId);

    /**
     * Requests a List of all available RUNNING containers corresponding to an image.
     *
     * @param imageId  The container's Image ID.
     * @return  A List of running edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getRunningContainerListByImage(String imageId);

    /**
     * Requests a Map of container statuses.
     *
     * @return  A Map of container statuses.
     */
    public Map<String, String> getContainerStatusMap();

    /**
     * Requests a single _Container object.
     *
     * @param id  The container ID.
     * @return  The edu.ucar.unidata.cloudcontrol.domain.docker._Container object.
     */
    public _Container getContainer(String id);

    /**
     * Starts a Docker container.
     *
     * @param imageId  The ID of the image in which to start the container.
     * @param authenticatedUser  The user that the started container.
     * @return  The ID of the container that has been started (or null if unsuccessful).
     */
    public String startContainer(String imageId, String authenticatedUser);

    /**
     * Stops a Docker container.
     *
     * @param containerId  The ID of the container to stop.
     */
    public void stopContainer(String containerId);

    /**
     * Requests whether the edu.ucar.unidata.cloudcontrol.domain.docker._Container is running or not.
     *
     * @param id  The ID of the _Container to check.
     * @param dockerClient  The initialized DockerClient to use.
     * @return  The whether the container is running or not.
     */
    public boolean containerIsRunning(DockerClient dockerClient, String id);

    /**
     * Returns a requested InspectContainerResponse.
     *
     * @param dockerClient  The DockerClient object to use.
     * @param containerId  The Container ID to inspect.
     * @return  The InspectContainerResponse.
     */
    public InspectContainerResponse inspectContainer(DockerClient dockerClient, String containerId);

    /**
     * Removes all Containers from an Image.
     *
     * @param imageId  The ID of the Image whose Containers need to be removed.
     */
    public void removeContainersFromImage(String imageId);

    /**
     * Removes a single Container from an Image.
     *
     * @param imageId  The ID of the Image corresponding to the Container.
     * @param containerId  The ID of the Container to remove.
     */
    public void removeSingleContainerFromImage(String imageId, String containerId);
}
