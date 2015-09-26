package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;

/**
 * Object representing a new Docker Container.  
 *
 * A NewContainer is an object collecting the user-specified attributes 
 * that will be used to create a new com.github.dockerjava.api.model.Container;
 */

public class NewContainer implements Serializable {

    private String name;
    private int portNumber;
    private String hostName;
	private String imageRepository;

    /**
     * Returns the user-specified name of the Container.
     * 
     * @return  The Container's name.  
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user-specified name of the Container.
     * 
     * @param name  The Container's name. 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the user-specified port number of the Container.
     * 
     * @return  The Container's port number. 
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * Sets the user-specified port number of the ontainer.
     * 
     * @param portNumber  The Container's port number. 
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * Returns the user-specified host name of the Container.
     * 
     * @return  The Container's host name.  
     */ 
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the user-specified host name of the Container.
     * 
     * @param hostName  The Container's host name. 
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
	
    /**
     * Returns the user-specified Image ID of the Container.
     * 
     * @return  The Container's Image ID.  
     */ 
    public String getImageRepository() {
        return imageRepository;
    }

    /**
     * Sets the user-specified Image ID of the Container.
     * 
     * @param imageRepository  The Container's Image ID. 
     */
    public void setImageRepository(String imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * String representation of a NewContainer object.
     * A NewContainer is person with an account in the cloudcontrol web app. 
     * The NewContainer attributes correspond to database columns.
     */ 
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("name: " + name + "\n ");
        buffer.append("portNumber: " + String.valueOf(portNumber) + "\n ");
        buffer.append("hostName: " + hostName + "\n ");      
		buffer.append("imageRepository: " + imageRepository + "\n ");    
        return buffer.toString();
    }


}
