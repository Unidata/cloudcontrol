package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;

import edu.ucar.unidata.cloudcontrol.domain.docker.NewContainer;


/**
 * Service for processing com.github.dockerjava.api.model.Container objects. 
 */
public interface ContainerManager {

    /**
     * Requests a List of all available Containers.
     * 
     * @return  A List of available Containers.   
     */
    public List<Container> getContainerList();
	
    /**
     * Requests a single Container.
     * 
	 * @param id  The Container ID.
     * @return  The Container.   
     */
	public Container getContainer(String id);

    /**
     * Returns a requested InspectContainerResponse.
     * 
     * @param id  The Container ID.
     * @return  The requested InspectContainerResponse.  
     */
    public InspectContainerResponse inspectContainer(String id);
	
    /**
     * Returns a requested InspectContainerResponse.
     * 
     * @param newContainer  The NewContainer object that has the user-specified values.
     * @return  The requested InspectContainerResponse.  
     */
    public CreateContainerResponse createContainer(NewContainer newContainer) ;
	
    /**
     * Starts a Container.
     * 
     * @param container  The Container to start.
     */
	public void startContainer(Container container);

    /**
     * Stops a Container.
     * 
     * @param container  The Container to stop.
     */
	public void stopContainer(Container container); 
	
    /**
     * Stops a Container.
     * 
     * @param container  The Container to stop.
	 * @param timeout  Timeout in seconds before killing the container. Defaults to 10 seconds.
     */
	public void stopContainer(Container container, int timeout);
	

}
