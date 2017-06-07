package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DefaultDockerClientConfig;

import edu.ucar.unidata.cloudcontrol.domain.docker.ClientConfig;
import edu.ucar.unidata.cloudcontrol.repository.docker.ClientConfigDao;

/**
 * Service for initializing and configuring the com.github.dockerjava.api.DockerClient.
 */
public class ClientManagerImpl implements ClientManager {
    protected static Logger logger = Logger.getLogger(ClientManagerImpl.class);
       
    private ClientConfigDao clientConfigDao;

    /**
     * Sets the data access object which will acquire and persist the data 
     * passed to it via the methods of this ClientManager. 
     * 
     * @param clientConfigDao  The service mechanism data access object representing a ClientConfig. 
     */
    public void setClientConfigDao(ClientConfigDao clientConfigDao) {
        this.clientConfigDao = clientConfigDao;
    }
            
    /**
     * Initializes a com.github.dockerjava.api.DockerClient with configuraion information.
     *
     * @return  A DockerClient object.
     */
    public DockerClient initializeDockerClient() {
        List<ClientConfig> clientConfigs = getAllClientConfigs();
        if (clientConfigs.size() > 0) {
			ClientConfig clientConfig = clientConfigs.get(0);       
            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(clientConfig.getDockerHost())
                .withDockerTlsVerify(true)
                .withDockerCertPath(clientConfig.getDockerCertPath())
                .build();
            return DockerClientBuilder.getInstance(config).build();
        } else {
			throw new DockerClientException("Unable to initialize Docker client.  No client configuration settings are available.");
        }        
    }
    
    /**
     * Looks up and retrieves the ClientConfig from the persistence mechanism using the id.
     * 
     * @param id   The id of the ClientConfig (will be unique for each ClientConfig). 
     * @return  The ClientConfig.   
     */
    public ClientConfig lookupById(int id) {
        return clientConfigDao.lookupById(id);
    }
    
    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_HOST value.
     * 
     * @param dockerHost  The DOCKER_HOST value of the ClientConfig. 
     * @return  The List of ClientConfigs.   
     */
    public List<ClientConfig> lookupByDockerHost(String dockerHost) {
        return clientConfigDao.lookupByDockerHost(dockerHost);
    }
    
    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_CERT_PATH value.
     * 
     * @param dockerCertPath  The DOCKER_CERT_PATH value of the ClientConfig. 
     * @return  The List of ClientConfigs.  
     */
    public List<ClientConfig> lookupByDockerCertPath(String dockerCertPath) {
        return clientConfigDao.lookupByDockerCertPath(dockerCertPath);
    }
    
    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_TLS_VERIFY value.
     * 
     * @param dockerTlsVerify  The DOCKER_TLS_VERIFY value of the ClientConfig. 
     * @return  The List of ClientConfigs. 
     */
    public List<ClientConfig> lookupByDockerTlsVerify(int dockerTlsVerify) {
        return clientConfigDao.lookupByDockerTlsVerify(dockerTlsVerify);
    }
    
    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the user who created them.
     * 
     * @param createdBy  The user who created the ClientConfigs. 
     * @return  The List of ClientConfigs.   
     */
    public List<ClientConfig> lookupByCreatedBy(String createdBy) {
        return clientConfigDao.lookupByCreatedBy(createdBy);
    }
    
    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the user who last updated them.
     * 
     * @param lastUpdatedBy  The user to last update the ClientConfigs. 
     * @return  The List of ClientConfigs.   
     */
    public List<ClientConfig> lookupByLastUpdatedBy(String lastUpdatedBy) {
        return clientConfigDao.lookupByLastUpdatedBy(lastUpdatedBy);
    }
    
    /**
     * Looks up and retrieves a List of all ClientConfigs from the persistence mechanism.
     * 
     * @return  The List of ClientConfigs.   
     */
    public List<ClientConfig> getAllClientConfigs() {
        return clientConfigDao.getAllClientConfigs();
    }
    
    /**
     * Finds and removes the ClientConfig from the persistence mechanism using the id.
     * 
     * @param id  The id of the ClientConfig to delete. 
     */
    public void deleteClientConfig(int id) {
        clientConfigDao.deleteClientConfig(id);
    }

    /**
     * Creates a new ClientConfig in the persistence mechanism.
     * 
     * @param clientConfig  The ClientConfig to be created. 
     * @return  The id of the newly created ClientConfig object. 
     */
    public int createClientConfig(ClientConfig clientConfig) {
        Date now = new Date(System.currentTimeMillis());
        clientConfig.setDateCreated(now);
        clientConfig.setDateModified(now);
        return clientConfigDao.createClientConfig(clientConfig);
    }
    
    /**
     * Saves changes made to an existing ClientConfig in the persistence mechanism.
     * 
     * @param clientConfig  The existing ClientConfig with changes that needs to be saved. 
     */
    public void updateClientConfig(ClientConfig clientConfig) {
        Date now = new Date(System.currentTimeMillis());
        clientConfig.setDateModified(now);
        clientConfigDao.updateClientConfig(clientConfig);
    }

    /**
     * Determines if the minimum required Docker-related system environment variables exist.
     * The minimum required environment variables needed by the DockerClient are DOCKER_HOST 
     * and DOCKER_CERT_PATH.  If DOCKER_TLS_VERIFY is present, it must not be false or empty.
     *
     * @return  The ClientConfig object.
     */
    public ClientConfig getClientConfigFromSystemEnvars() {
        logger.info("Checking for Docker system environment variables...");
        Map<String, String> envars = System.getenv();
        ClientConfig clientConfig = new ClientConfig();
        if (envars.containsKey("DOCKER_HOST")) {
             if (containsPropertyValue(envars.get("DOCKER_HOST"))) {
                clientConfig.setDockerHost(envars.get("DOCKER_HOST"));
            }
        }
        if (envars.containsKey("DOCKER_CERT_PATH")) {
            if (containsPropertyValue(envars.get("DOCKER_CERT_PATH"))) {
                clientConfig.setDockerCertPath(envars.get("DOCKER_CERT_PATH"));
            }
        }
        if (envars.containsKey("DOCKER_TLS_VERIFY")) {
            if (containsPropertyValue(envars.get("DOCKER_TLS_VERIFY"))) {
                 if (envars.get("DOCKER_TLS_VERIFY").equals("1")) {
                     clientConfig.setDockerTlsVerify(new Integer(envars.get("DOCKER_TLS_VERIFY")).intValue());
                 }
            }
        }
        return clientConfig;
    }

    /**
     * Utility for determining if a String contains actual data.
     *
     * @param s  The string to check.
     * @return  Whether the string contains actual data or not.
     */
    public boolean containsData(String s) {
        if (StringUtils.isNotBlank(StringUtils.stripToNull(s))) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Utility for determining if a property value contains a data.
     *
     * @param p  The property to check.
     * @return  Whether the property value contains a data or not.
     */
    public boolean containsPropertyValue(String p) {
        if (StringUtils.isNotBlank(StringUtils.removePattern(p, "^[a-zA-Z_]+="))) {
            return true;
        } else {
            return false;
        }
    }
    
}
