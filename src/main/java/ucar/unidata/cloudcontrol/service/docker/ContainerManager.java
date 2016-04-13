package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;
import java.util.Map;


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


		

 //   public InspectContainerResponse inspectContainer(String id);
    
    /**
     * Returns a requested InspectContainerResponse.
     * 
     * @param newContainer  The NewContainer object that has the user-specified values.
     * @return  The requested InspectContainerResponse.  
     */
//    public CreateContainerResponse createContainer(NewContainer newContainer) ;
    
//    public CreateContainerResponse createContainer(String image);
    
    /**
     * Removes a Container.
     *
     * @param container  The Container object to remove.
     */
//    public void removeContainer(Container container);
    
    /**
     * Starts a Container.
     * 
     * @param container  The Container to start.
     */
//    public void startContainer(Container container);

    /**
     * Stops a Container.
     * 
     * @param container  The Container to stop.
     */
//    public void stopContainer(Container container); 
    
    /**
     * Stops a Container.
     * 
     * @param container  The Container to stop.
     * @param timeout  Timeout in seconds before killing the container. Defaults to 10 seconds.
     */
//    public void stopContainer(Container container, int timeout);
    

}
