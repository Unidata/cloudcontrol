package edu.ucar.unidata.cloudcontrol.service.docker;

import edu.ucar.unidata.cloudcontrol.domain.docker._Info;
import edu.ucar.unidata.cloudcontrol.domain.docker._Version;
import edu.ucar.unidata.cloudcontrol.service.docker.ClientManager;
import edu.ucar.unidata.cloudcontrol.service.docker.converters.InfoConverter;
import edu.ucar.unidata.cloudcontrol.service.docker.converters.VersionConverter;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.Version;

/**
 * Service for processing Docker server and system-related api requests.
 */
public class ServerManagerImpl implements ServerManager {
    protected static Logger logger = Logger.getLogger(ServerManagerImpl.class);

    @Resource(name = "clientManager")
    private ClientManager clientManager;

    /**
     * Requests Docker server information.
     *
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Info object.
     */
    public _Info getInfo() {
        logger.debug("Getting docker server information.");
        DockerClient dockerClient = clientManager.initializeDockerClient();
        Info info = dockerClient.infoCmd().exec();
        InfoConverter infoConverter = new InfoConverter();
        _Info _info = infoConverter.convertInfo(info);
        return _info;
    }

    /**
     * Requests the Docker server version.
     *
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Version object.
     */
    public _Version getVersion() {
        logger.debug("Getting docker server version.");
        DockerClient dockerClient = clientManager.initializeDockerClient();
        Version version = dockerClient.versionCmd().exec();
        VersionConverter versionConverter = new VersionConverter();
        _Version _version = versionConverter.convertVersion(version);
        return _version;
    }
}
