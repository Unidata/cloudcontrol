package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;

/**
 * Object representing a new Docker Container.  
 *
 * A NewContainer is an object collecting the User-specified attributes 
 * that will be used to create a new com.github.dockerjava.api.model.Container;
 */

public class NewContainer implements Serializable {

    private String name;
    private int portNumber;
    private String hostName;

    /**
     * Returns the User-specified name of the Container.
     * 
     * @return  The Container's name.  
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the User-specified name of the Container.
     * 
     * @param name  The Container's name. 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the User-specified port number of the Container.
     * 
     * @return  The Container's port number. 
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * Sets the User-specified port number of the ontainer.
     * 
     * @param portNumber  The Container's port number. 
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * Returns the User-specified host name of the Container.
     * 
     * @return  The Container's host name.  
     */ 
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the User-specified host name of the Container.
     * 
     * @param hostName  The Container's host name. 
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
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
        return buffer.toString();
    }


}
