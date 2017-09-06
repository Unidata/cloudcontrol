package edu.ucar.unidata.cloudcontrol.service.docker;

import edu.ucar.unidata.cloudcontrol.domain.docker.ClientConfig;
import edu.ucar.unidata.cloudcontrol.repository.docker.ClientConfigDao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DefaultDockerClientConfig;

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
        logger.debug("Setting client configuration data access object.");
        this.clientConfigDao = clientConfigDao;
    }

    /**
     * Initializes a com.github.dockerjava.api.DockerClient with configuraion information.
     *
     * @return  A DockerClient object.
     */
    public DockerClient initializeDockerClient() {
        logger.debug("Initializing docker client.");
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
            String message = "Unable to initialize Docker client.  No client configuration settings are available.";
            logger.error(message);
            throw new DockerClientException(message);
        }
    }

    /**
     * Looks up and retrieves the ClientConfig from the persistence mechanism using the id.
     *
     * @param id   The id of the ClientConfig (will be unique for each ClientConfig).
     * @return  The ClientConfig.
     */
    public ClientConfig lookupById(int id) {
        logger.debug("Using DAO to look up client configuration by id " + new Integer(id).toString());
        return clientConfigDao.lookupById(id);
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_HOST value.
     *
     * @param dockerHost  The DOCKER_HOST value of the ClientConfig.
     * @return  The List of ClientConfigs.
     */
    public List<ClientConfig> lookupByDockerHost(String dockerHost) {
        logger.debug("Using DAO to look up client configurations by docker host " + dockerHost);
        return clientConfigDao.lookupByDockerHost(dockerHost);
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_CERT_PATH value.
     *
     * @param dockerCertPath  The DOCKER_CERT_PATH value of the ClientConfig.
     * @return  The List of ClientConfigs.
     */
    public List<ClientConfig> lookupByDockerCertPath(String dockerCertPath) {
        logger.debug("Using DAO to look up client configurations by docker certificate path " + dockerCertPath);
        return clientConfigDao.lookupByDockerCertPath(dockerCertPath);
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the DOCKER_TLS_VERIFY value.
     *
     * @param dockerTlsVerify  The DOCKER_TLS_VERIFY value of the ClientConfig.
     * @return  The List of ClientConfigs.
     */
    public List<ClientConfig> lookupByDockerTlsVerify(int dockerTlsVerify) {
        logger.debug("Using DAO to look up client configurations by docker TLS verify " + dockerTlsVerify);
        return clientConfigDao.lookupByDockerTlsVerify(dockerTlsVerify);
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the user who created them.
     *
     * @param createdBy  The user who created the ClientConfigs.
     * @return  The List of ClientConfigs.
     */
    public List<ClientConfig> lookupByCreatedBy(String createdBy) {
        logger.debug("Using DAO to look up client configurations created by user " + createdBy);
        return clientConfigDao.lookupByCreatedBy(createdBy);
    }

    /**
     * Looks up and retrieves a List of ClientConfigs from the persistence mechanism using the user who last updated them.
     *
     * @param lastUpdatedBy  The user to last update the ClientConfigs.
     * @return  The List of ClientConfigs.
     */
    public List<ClientConfig> lookupByLastUpdatedBy(String lastUpdatedBy) {
        logger.debug("Using DAO to look up client configurations last updated by user " + lastUpdatedBy);
        return clientConfigDao.lookupByLastUpdatedBy(lastUpdatedBy);
    }

    /**
     * Looks up and retrieves a List of all ClientConfigs from the persistence mechanism.
     *
     * @return  The List of ClientConfigs.
     */
    public List<ClientConfig> getAllClientConfigs() {
        logger.debug("Using DAO to look up all client configurations.");
        return clientConfigDao.getAllClientConfigs();
    }

    /**
     * Finds and removes the ClientConfig from the persistence mechanism using the id.
     *
     * @param id  The id of the ClientConfig to delete.
     */
    public void deleteClientConfig(int id) {
        logger.debug("Using DAO to delete client configuration with id " + new Integer(id).toString());
        clientConfigDao.deleteClientConfig(id);
    }

    /**
     * Creates a new ClientConfig in the persistence mechanism.
     *
     * @param clientConfig  The ClientConfig to be created.
     * @return  The id of the newly created ClientConfig object.
     */
    public int createClientConfig(ClientConfig clientConfig) {
        logger.debug("Using DAO to create new client configuration with docker host " + clientConfig.getDockerHost() + " and docker certificate path " + clientConfig.getDockerCertPath());
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
        logger.debug("Using DAO to update client configuration with docker host " + clientConfig.getDockerHost() + " and docker certificate path " + clientConfig.getDockerCertPath());
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
        logger.debug("Get client confirguration information from Docker system environment variables.");
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
