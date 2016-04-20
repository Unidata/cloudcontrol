package edu.ucar.unidata.cloudcontrol.service.docker;

import java.net.URI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerHostConfig;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.ContainerNetworkSettings;
import com.github.dockerjava.api.model.ContainerPort;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Ports;

import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerHostConfig;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetwork;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetworkSettings;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerPort;
import edu.ucar.unidata.cloudcontrol.service.docker.ClientManager;

/**
 * Service for processing Docker container-related api requests.
 */
public class ContainerManagerImpl implements ContainerManager {
    
    protected static Logger logger = Logger.getLogger(ContainerManagerImpl.class);

    @Resource(name = "clientManager")
    private ClientManager clientManager;
    
    /**
     * Requests a List of all available containers.
     *
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getContainerList() {
        List<_Container> _containers;
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            List<Container> containers = dockerClient.listContainersCmd().withShowAll(false).exec();
            _containers = processContainerList(containers);
        } catch (Exception e) {
            logger.error("Unable to get list of Containers: " + e);
            _containers = null;
        }           
        return _containers;
    }
    
    /**
     * Utility method to process a List of com.github.dockerjava.api.model.Container objects
     * to a corresponding List of edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     *
     * @param containers  The com.github.dockerjava.api.model.Container List.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Container List.
     */
    public List<_Container> processContainerList(List<Container> containers) {
        List<_Container> _containers = new ArrayList<_Container>(containers.size());
        for (Container container : containers) {
            _Container _container = convertContainer(container);
            _containers.add(_container);
        }
        return _containers;
    }
    
    /**
     * Converts a com.github.dockerjava.api.model.Container object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._Container object.
     *
     * @param container  The com.github.dockerjava.api.model.Container object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Container object.
     */
    public _Container convertContainer(Container container) {
        Function<Container, _Container> mapContainerTo_Container = new Function<Container, _Container>() {
            public _Container apply(Container c) {
                _Container _container = new _Container();
                _container.setCommand(c.getCommand());
                _container.setCreated(c.getCreated());
                _container.setId(c.getId());
                _container.setImage(c.getImage());
                _container.setImageId(c.getImageId());
                _container.setNames(c.getNames());
                _container.setPorts(processContainerPortArray(c.getPorts()));
                _container.setLabels(c.getLabels());
                _container.setStatus(c.getStatus());
                _container.setSizeRw(c.getSizeRw());
                _container.setSizeRootFs(c.getSizeRootFs());
                _container.setHostConfig(convertContainerHostConfig(c.getHostConfig()));
                _container.setNetworkSettings(convertContainerNetworkSettings(c.getNetworkSettings()));
                return _container;
            }
        };
        _Container _container = mapContainerTo_Container.apply(container);
        return _container;
    }
    
    /**
     * Utility method to process an array of com.github.dockerjava.api.model.ContainerPort objects
     * to a corresponding array of edu.ucar.unidata.cloudcontrol.domain.docker._ContainerPort objects.
     *
     * @param containerPorts  The com.github.dockerjava.api.model.ContainerPort array.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._ContainerPort array.
     */
    public _ContainerPort[] processContainerPortArray(ContainerPort[] containerPorts) {
        _ContainerPort[] _containerPorts = new _ContainerPort[containerPorts.length];
        for(int i=0; i<containerPorts.length; i++){
            _containerPorts[i] = convertContainerPort(containerPorts[i]);
        }
        return _containerPorts;
    }
    
    /**
     * Converts a com.github.dockerjava.api.model.ContainerPort object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._ContainerPort object.
     *
     * @param containerPort  The com.github.dockerjava.api.model.ContainerPort object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._ContainerPort object.
     */
    public _ContainerPort convertContainerPort(ContainerPort containerPort) {
        Function<ContainerPort, _ContainerPort> mapContainerPortTo_ContainerPort = new Function<ContainerPort, _ContainerPort>() {
            public _ContainerPort apply(ContainerPort c) {
                _ContainerPort _containerPort = new _ContainerPort();
                _containerPort.setIp(c.getIp());
                _containerPort.setPrivatePort(c.getPrivatePort());
                _containerPort.setPublicPort(c.getPublicPort());
                _containerPort.setType(c.getType());
                return _containerPort;
            }
        };
        _ContainerPort _containerPort = mapContainerPortTo_ContainerPort.apply(containerPort);
        return _containerPort;
    }
    
    
    
    /**
     * Converts a com.github.dockerjava.api.model.ContainerHostConfig object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._ContainerHostConfig object.
     *
     * @param containerHostConfig  The com.github.dockerjava.api.model.ContainerHostConfig object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._ContainerHostConfig object.
     */
    public _ContainerHostConfig convertContainerHostConfig(ContainerHostConfig containerHostConfig) {
        Function<ContainerHostConfig, _ContainerHostConfig> mapContainerHostConfigTo_ContainerHostConfig = new Function<ContainerHostConfig, _ContainerHostConfig>() {
            public _ContainerHostConfig apply(ContainerHostConfig c) {
                _ContainerHostConfig _containerHostConfig = new _ContainerHostConfig();
                _containerHostConfig.setNetworkMode(c.getNetworkMode());
                return _containerHostConfig;
            }
        };
        _ContainerHostConfig _containerHostConfig = mapContainerHostConfigTo_ContainerHostConfig.apply(containerHostConfig);
        return _containerHostConfig;
    }
    
    /**
     * Converts a com.github.dockerjava.api.model.ContainerNetworkSettings object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetworkSettings object.
     *
     * @param containerNetworkSettings  The com.github.dockerjava.api.model.ContainerNetworkSettings object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetworkSettings object.
     */
    public _ContainerNetworkSettings convertContainerNetworkSettings(ContainerNetworkSettings containerNetworkSettings) {
        Function<ContainerNetworkSettings, _ContainerNetworkSettings> mapContainerNetworkSettingsTo_ContainerNetworkSettings = new Function<ContainerNetworkSettings, _ContainerNetworkSettings>() {
            public _ContainerNetworkSettings apply(ContainerNetworkSettings c) {
                _ContainerNetworkSettings _containerNetworkSettings = new _ContainerNetworkSettings();
                _containerNetworkSettings.setNetworks(processContainerNetwork(c.getNetworks()));
                return _containerNetworkSettings;
            }
        };
        _ContainerNetworkSettings _containerNetworkSettings = mapContainerNetworkSettingsTo_ContainerNetworkSettings.apply(containerNetworkSettings);
        return _containerNetworkSettings;
    }
    
    /**
     * Utility method to process a Map of com.github.dockerjava.api.model.ContainerNetwork objects
     * to a corresponding Map of edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetwork objects.
     *
     * @param containerNetwork  The com.github.dockerjava.api.model.ContainerNetwork Map.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetwork Map.
     */
    public Map<String, _ContainerNetwork> processContainerNetwork(Map<String, ContainerNetwork> containerNetwork) {
        Map<String, _ContainerNetwork> _containerNetworkMap = new HashMap <String, _ContainerNetwork>(containerNetwork.size());
        for (Map.Entry<String, ContainerNetwork> entry : containerNetwork.entrySet()) {
            _containerNetworkMap.put(entry.getKey(), convertContainerNetwork(entry.getValue()));
        }
        return _containerNetworkMap;
    }
    
    /**
     * Converts a com.github.dockerjava.api.model.ContainerNetwork object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetwork object.
     *
     * @param containerNetwork  The com.github.dockerjava.api.model.ContainerNetwork object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetwork object.
     */
    public _ContainerNetwork convertContainerNetwork(ContainerNetwork network) {
        Function<ContainerNetwork, _ContainerNetwork> mapContainerNetworkTo_ContainerNetwork = new Function<ContainerNetwork, _ContainerNetwork>() {
            public _ContainerNetwork apply(ContainerNetwork c) {
                _ContainerNetwork _containerNetwork = new _ContainerNetwork();
                _containerNetwork.setAliases(c.getAliases());
                _containerNetwork.setNetworkID(c.getNetworkID());
                _containerNetwork.setEndpointId(c.getEndpointId());
                _containerNetwork.setGateway(c.getGateway());
                _containerNetwork.setIpAddress(c.getIpAddress());
                _containerNetwork.setIpPrefixLen(c.getIpPrefixLen());
                _containerNetwork.setIpV6Gateway(c.getIpV6Gateway());
                _containerNetwork.setGlobalIPv6Address(c.getGlobalIPv6Address());
                _containerNetwork.setGlobalIPv6PrefixLen(c.getGlobalIPv6PrefixLen());
                _containerNetwork.setMacAddress(c.getMacAddress());
                return _containerNetwork;
            }
        };
        _ContainerNetwork _network = mapContainerNetworkTo_ContainerNetwork.apply(network);
        return _network;
    }
    
    
    
    
    /**
     * Requests a List of all available containers corresponding to an image.
     *
     * @param image  The container's image.
     * @return  A List of edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getContainerListByImage(String image) {
        List<_Container> _containerList = null;
        List<_Container> _containers = getContainerList();
        if (!Objects.isNull(_containers)) {       
            _Container _container = null;
            _containerList = new ArrayList<_Container>();
            for (_Container c : _containers) {
                if (image.equals(c.getImage())) {
                    _container = c; 
                    _containerList.add(_container);
                }
            } 
        }
        return _containerList;
    }

    /**
     * Requests a List of all available RUNNING containers corresponding to an image.
     *
     * @param image  The container's image.
     * @return  A List of edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getRunningContainerListByImage(String image) {
        List<_Container> _containerList = null;
        List<_Container> _containers = getContainerList();   
        if (!Objects.isNull(_containers)) { 
            _Container _container = null; 
            _containerList = new ArrayList<_Container>();
            for (_Container c : _containers) {
                if (image.equals(c.getImage())) {
                    if (!StringUtils.contains(c.getStatus(), "Exited")) {
                        _container = c; 
                        _containerList.add(_container);
                    }
                }
            } 
        }
        return _containerList;
    }
    
    /**
     * Requests a Map of container statuses.
     *
     * @return  A Map of container statuses.
     */
    public Map<String, String> getContainerStatusMap() {
        Map<String, String> _containerStatusMap = null;
        List<_Container> _containers = getContainerList();   
        if (!Objects.isNull(_containers)) { 
            _containerStatusMap = new HashMap<String, String>();
            for (_Container c : _containers) {
                if (!StringUtils.contains(c.getStatus(), "Exited")) {
                    _containerStatusMap.put(c.getImage(), c.getStatus());
                } 
            }
        } 
        return _containerStatusMap;
    }

    /**
     * Requests a single _Container object.
     * 
     * @param id  The container ID.
     * @return  The edu.ucar.unidata.cloudcontrol.domain.docker._Container object.   
     */
    public _Container getContainer(String id) {
        List<_Container> _containers = getContainerList();   
        _Container _container = null;
        for (_Container c : _containers) {
            if (id.equals(c.getId())) {
                _container = c; 
                break;
            }
        } 
        return _container;
    }  
	
    /**
     * Starts a _Container.
     *
     * @param _container  The _Container to start.
     */
    public void startContainer(_Container _container) {
        try {
           DockerClient dockerClient = clientManager.initializeDockerClient();
           dockerClient.startContainerCmd(_container.getId()).exec();
       } catch (Exception e) {
           logger.error("Unable to start Container: " + e);
       }           
    }
    
    /**
     * Stops a _Container.
     *
     * @param _container  The _Container to stop.
     */
    public void stopContainer(_Container _container) {
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            dockerClient.stopContainerCmd(_container.getId()).exec();
        } catch (Exception e) {
            logger.error("Unable to stop Container: " + e);
        }           
    }

    /**
     * Stops a _Container.
     *
     * @param _container  The _Container to stop.
     * @param timeout  Timeout in seconds before killing the container. Defaults to 10 seconds.
     */
    public void stopContainer(_Container _container, int timeout) {
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            dockerClient.stopContainerCmd(_container.getId()).withTimeout(timeout).exec();
        } catch (Exception e) {
            logger.error("Unable to stop Container: " + e);
        }    
    }
    
    /**
     * Returns a CreateContainerResponse object.
     *
     * @param imageId  The ID of the image we want container-ize.
     * @return  The CreateContainerResponse.
     */
	/*
     public CreateContainerResponse createContainer(String imageId) {
         try {
             DockerClient dockerClient = clientManager.initializeDockerClient();
             return dockerClient.createContainerCmd(imageId).exec();
         } catch (Exception e) {
             logger.error("Unable to create a Container: " + e);
             return null;
         }    
     }
	 */
    
     /**
      * Returns a requested InspectContainerResponse.
      *
      * @param containerId  The Container ID to inspect.
      * @return  The InspectContainerResponse.
      */
     /*
     public InspectContainerResponse inspectContainer(String containerId) {
		 try{
             DockerClient dockerClient = clientManager.initializeDockerClient();
             return dockerClient.inspectContainerCmd(containerId).exec();
         } catch (Exception e) {
             logger.error("Unable to inspect Container: " + e);
             return null;
         }    
     }
     */
        
}
