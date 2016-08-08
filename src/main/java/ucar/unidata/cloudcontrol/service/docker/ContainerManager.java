package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;
import java.util.Map;

// importing these for ability to start and stop images.  Will convert them 
// to their _Obj instances and remove them.
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
     * @param image  The container's image.
     * @return  A List of edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getContainerListByImage(String image);
    
    /**
     * Requests a List of all available RUNNING containers corresponding to an image.
     *
     * @param image  The container's image.
     * @return  A List of edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getRunningContainerListByImage(String image);
    
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
     * Starts a edu.ucar.unidata.cloudcontrol.domain.docker._Container object.
     *
     * @param imageId  The ID of the Image in which to start the _Container.
	 * @return  The whether the container has been started or not. 
     */
    public boolean startContainer(String imageId);
    
    /**
     * Stops a edu.ucar.unidata.cloudcontrol.domain.docker._Container object.
     *
     * @param imageId  The ID of the Image in which resides the _Container to stop.
	 * @return  The whether the container has been started or not. 
     */
    public boolean stopContainer(String imageId);
}
