package edu.ucar.unidata.cloudcontrol.repository.docker;

import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.docker.ClientConfig;

/**
 * The data access object representing a ClientConfig.  
 */

public interface ClientConfigDao {

    /**
     * Looks up and retrieves the ClientConfig from the persistence mechanism using the id.
     * 
     * @param id   The id of the ClientConfig (will be unique for each ClientConfig). 
     * @return  The ClientConfig.   
     */
    public ClientConfig lookupById(int id);
    
    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_HOST value.
     * 
     * @param dockerHost  The DOCKER_HOST value of the ClientConfig. 
     * @return  The List of ClientConfigs.   
     */
    public List<ClientConfig> lookupByDockerHost(String dockerHost);
    
    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_CERT_PATH value.
     * 
     * @param dockerCertPath  The DOCKER_CERT_PATH value of the ClientConfig. 
     * @return  The List of ClientConfigs.  
     */
    public List<ClientConfig> lookupByDockerCertPath(String dockerCertPath);
    
    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_TLS_VERIFY value.
     * 
     * @param dockerTlsVerify  The DOCKER_TLS_VERIFY value of the ClientConfig. 
     * @return  The List of ClientConfigs. 
     */
    public List<ClientConfig> lookupByDockerTlsVerify(int dockerTlsVerify);
    
    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the user who created them.
     * 
     * @param createdBy  The user who created the ClientConfigs. 
     * @return  The List of ClientConfigs.   
     */
    public List<ClientConfig> lookupByCreatedBy(String createdBy);
    
    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the user who last updated them.
     * 
     * @param lastUpdatedBy  The user to last update the ClientConfigs. 
     * @return  The List of ClientConfigs.   
     */
    public List<ClientConfig> lookupByLastUpdatedBy(String lastUpdatedBy);
    
    /**
     * Looks up and retrieves a List of all ClientConfigs from the persistence mechanism.
     * 
     * @return  The List of ClientConfigs.   
     */
    public List<ClientConfig> getAllClientConfigs();
    
    /**
     * Finds and removes the ClientConfig from the persistence mechanism using the id.
     * 
     * @param id  The id of the ClientConfig to delete. 
     */
    public void deleteClientConfig(int id);

    /**
     * Creates a new ClientConfig in the persistence mechanism.
     * 
     * @param clientConfig  The ClientConfig to be created. 
     * @return  The id of the newly created ClientConfig object. 
     */
    public int createClientConfig(ClientConfig clientConfig);

    /**
     * Saves changes made to an existing ClientConfig in the persistence mechanism.
     * 
     * @param clientConfig  The existing ClientConfig with changes that needs to be saved. 
     */
    public void updateClientConfig(ClientConfig clientConfig);
}
