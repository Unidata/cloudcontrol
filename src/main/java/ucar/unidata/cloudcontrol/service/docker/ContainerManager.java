package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;
import java.util.Map;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;

import edu.ucar.unidata.cloudcontrol.domain.docker._Container;


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
     * @param imageId  The ID of the Image in which to start the container.
	 * @return  The whether the container has been started or not. 
     */
    public boolean startContainer(String imageId);
	
    /**
     * Requests whether the edu.ucar.unidata.cloudcontrol.domain.docker._Container is running or not.
     *
     * @param id  The ID of the _Container to check.
     * @param dockerClient  The initialized DockerClient to use.
     * @return  The whether the container is running or not. 
     */
    public boolean containerIsRunning(DockerClient dockerClient, String id);
    
    /**
     * Stops a edu.ucar.unidata.cloudcontrol.domain.docker._Container object.
     *
     * @param imageId  The ID of the Image in which resides the _Container to stop.
	 * @return  The whether the container has been started or not. 
     */
    public boolean stopContainer(String imageId);
	
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
     * @return  The whether the Containers were successfully removed or not. 
     */
    public boolean removeContainersFromImage(String imageId);
	
    /**
     * Removes a single Container from an Image.
     *
     * @param imageId  The ID of the Image corresponding to the Container.
     * @param containerId  The ID of the Container to remove.
     * @return  The whether the Container was successfully removed or not. 
     */
    public boolean removeSingleContainerFromImage(String imageId, String containerId);
}
