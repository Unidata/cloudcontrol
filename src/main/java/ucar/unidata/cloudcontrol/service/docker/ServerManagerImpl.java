package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.InfoRegistryConfig;
import com.github.dockerjava.api.model.Version;

import edu.ucar.unidata.cloudcontrol.domain.docker._Info;
import edu.ucar.unidata.cloudcontrol.domain.docker._InfoRegistryConfig;
import edu.ucar.unidata.cloudcontrol.domain.docker._Version;
import edu.ucar.unidata.cloudcontrol.service.docker.ClientManager;


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
        _Info _info;  
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            Info info = dockerClient.infoCmd().exec();
            _info = convertInfo(info); 
        } catch (Exception e) {
            logger.error("Unable to get Docker server info: " + e);
			_info = null;
        }    
        return _info;    
    }
    
    
    /**
     * Converts a com.github.dockerjava.api.model.Info object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._Info object.
     *
     * @param info  The com.github.dockerjava.api.model.Info object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Info object.
     */
    public _Info convertInfo(Info info) {
        Function<Info, _Info> mapInfoTo_Info = new Function<Info, _Info>() {
            public _Info apply(Info i) {
                _Info _info = new _Info();
                _info.setArchitecture(i.getArchitecture());
                _info.setContainers(i.getContainers());
                _info.setContainersStopped(i.getContainersStopped());
                _info.setContainersPaused(i.getContainersPaused());
                _info.setContainersRunning(i.getContainersRunning());
                _info.setCpuCfsPeriod(i.getCpuCfsPeriod());
                _info.setCpuCfsQuota(i.getCpuCfsQuota());
                _info.setCpuShares(i.getCpuShares());
                _info.setCpuSet(i.getCpuSet());
                _info.setDebug(i.getDebug());
                _info.setDiscoveryBackend(i.getDiscoveryBackend());
                _info.setDockerRootDir(i.getDockerRootDir());
                _info.setDriver(i.getDriver());
                _info.setDriverStatuses(i.getDriverStatuses());
                _info.setSystemStatus(i.getSystemStatus());
                _info.setPlugins(i.getPlugins());
                _info.setExecutionDriver(i.getExecutionDriver());
                _info.setLoggingDriver(i.getLoggingDriver());
                _info.setExperimentalBuild(i.getExperimentalBuild());
                _info.setHttpProxy(i.getHttpProxy());
                _info.setHttpsProxy(i.getHttpsProxy());
                _info.setId(i.getId());
                _info.setIpv4Forwarding(i.getIPv4Forwarding());
                _info.setBridgeNfIptables(i.getBridgeNfIptables());
                _info.setBridgeNfIp6tables(i.getBridgeNfIp6tables());
                _info.setImages(i.getImages());
                _info.setIndexServerAddress(i.getIndexServerAddress());
                _info.setInitPath(i.getInitPath());
                _info.setInitSha1(i.getInitSha1());
                _info.setKernelVersion(i.getKernelVersion());
                _info.setLabels(i.getLabels());
                _info.setMemoryLimit(i.getMemoryLimit());
                _info.setMemTotal(i.getMemTotal());
                _info.setName(i.getName());
                _info.setNcpu(i.getNCPU());
                _info.setNEventsListener(i.getNEventsListener());
                _info.setNfd(i.getNFd());
                _info.setNGoroutines(i.getNGoroutines());
                _info.setNoProxy(i.getNoProxy());
                _info.setOomKillDisable(i.getOomKillDisable());
                _info.setOsType(i.getOsType());
                _info.setOomScoreAdj(i.getOomScoreAdj());
                _info.setOperatingSystem(i.getOperatingSystem());
                _info.setRegistryConfig(convertInfoRegistryConfig(i.getRegistryConfig()));
                _info.setSockets(i.getSockets());
                _info.setSwapLimit(i.getSwapLimit());
                _info.setSystemTime(i.getSystemTime());
                _info.setServerVersion(i.getServerVersion());
                _info.setClusterStore(i.getClusterStore());
                _info.setClusterAdvertise(i.getClusterAdvertise());
                return _info;
            }
        };
        _Info _info = mapInfoTo_Info.apply(info);
        return _info;
    }
    
    /**
     * Converts a com.github.dockerjava.api.model.InfoRegistryConfig object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._InfoRegistryConfig object.
     *
     * @param infoRegistryConfig  The com.github.dockerjava.api.model.InfoRegistryConfig object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._InfoRegistryConfig object.
     */
    public _InfoRegistryConfig convertInfoRegistryConfig(InfoRegistryConfig infoRegistryConfig) {
        Function<InfoRegistryConfig, _InfoRegistryConfig> mapInfoRegistryConfigTo_InfoRegistryConfig = new Function<InfoRegistryConfig, _InfoRegistryConfig>() {
            public _InfoRegistryConfig apply(InfoRegistryConfig i) {
                _InfoRegistryConfig _infoRegistryConfig = new _InfoRegistryConfig();
                _infoRegistryConfig.setIndexConfigs(processIndexConfig(i.getIndexConfigs()));
                _infoRegistryConfig.setInsecureRegistryCIDRs(i.getInsecureRegistryCIDRs());
                _infoRegistryConfig.setMirrors(i.getMirrors());
                return _infoRegistryConfig;
            }
        };
        _InfoRegistryConfig _infoRegistryConfig = mapInfoRegistryConfigTo_InfoRegistryConfig.apply(infoRegistryConfig);
        return _infoRegistryConfig;
    }
    

    /**
     * Utility method to process a Map of com.github.dockerjava.api.model.InfoRegistryConfig.IndexConfig objects.
     * to a corresponding Map of du.ucar.unidata.cloudcontrol.domain.docker._InfoRegistryConfig._IndexConfig objects.
     *
     * @param indexConfigMap  The com.github.dockerjava.api.model.InfoRegistryConfig.IndexConfig Map.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._InfoRegistryConfig._IndexConfig Map.
     */
    public Map<String, _InfoRegistryConfig._IndexConfig> processIndexConfig(Map<String, InfoRegistryConfig.IndexConfig> indexConfigMap) {
        Map<String, _InfoRegistryConfig._IndexConfig> _indexConfigMap = new HashMap <String, _InfoRegistryConfig._IndexConfig>(indexConfigMap.size());
        for (Map.Entry<String, InfoRegistryConfig.IndexConfig> entry : indexConfigMap.entrySet()) {
            _indexConfigMap.put(entry.getKey(), convertIndexConfig(entry.getValue()));
        }
        return _indexConfigMap;
    }
    
    
    /**
     * Converts a com.github.dockerjava.api.model.InfoRegistryConfig.IndexConfig object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._InfoRegistryConfig._IndexConfig object.
     *
     * @param indexConfig  A com.github.dockerjava.api.model.InfoRegistryConfig.IndexConfig object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._IndexConfig object.
     */
    public _InfoRegistryConfig._IndexConfig convertIndexConfig(InfoRegistryConfig.IndexConfig indexConfig) {
        Function<InfoRegistryConfig.IndexConfig, _InfoRegistryConfig._IndexConfig> mapIndexConfigTo_IndexConfig = new Function<InfoRegistryConfig.IndexConfig, _InfoRegistryConfig._IndexConfig>() {
            public _InfoRegistryConfig._IndexConfig apply(InfoRegistryConfig.IndexConfig i) {
                _InfoRegistryConfig._IndexConfig _indexConfig = new _InfoRegistryConfig._IndexConfig();
                _indexConfig.setMirrors(i.getMirrors());
                _indexConfig.setName(i.getName());
                _indexConfig.setOfficial(i.getOfficial());
                _indexConfig.setSecure(i.getSecure());
                return _indexConfig;
            }
        };
        _InfoRegistryConfig._IndexConfig _indexConfig = mapIndexConfigTo_IndexConfig.apply(indexConfig);
        return _indexConfig;
    }
    

    /**
     * Requests the Docker server version.
     *
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Version object.
     */
    public _Version getVersion() {
        _Version _version;
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            Version version = dockerClient.versionCmd().exec();
            _version = convertVersion(version);
        } catch (Exception e) {
            logger.error("Unable to get Docker server version: " + e);
			_version = null;
        }    
        return _version;
    }
    
    /**
     * Converts a com.github.dockerjava.api.model.Version object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._Version object.
     *
     * @param info  The com.github.dockerjava.api.model.Version object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Version object.
     */
    public _Version convertVersion(Version version) {
        Function<Version, _Version> mapVersionTo_Version = new Function<Version, _Version>() {
            public _Version apply(Version v) {
                _Version _version = new _Version();
                _version.setApiVersion(v.getApiVersion());
                _version.setArch(v.getArch());
                _version.setGitCommit(v.getGitCommit());
                _version.setGoVersion(v.getGoVersion());
                _version.setKernelVersion(v.getKernelVersion());
                _version.setOperatingSystem(v.getOperatingSystem());
                _version.setVersion(v.getVersion());
                _version.setBuildTime(v.getBuildTime());
                _version.setExperimental(v.getExperimental());
                return _version;
            }
        };
        _Version _version = mapVersionTo_Version.apply(version);
        return _version;
    }
}
