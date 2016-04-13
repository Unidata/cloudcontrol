package edu.ucar.unidata.cloudcontrol.service.docker;

import java.net.URI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

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
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerHostConfig;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetwork;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetworkSettings;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerPort;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Service for processing Docker container-related api requests.
 */
public class ContainerManagerImpl implements ContainerManager {
	
	protected static Logger logger = Logger.getLogger(ContainerManagerImpl.class);

    /**
     * Initializes a com.github.dockerjava.api.DockerClient.
     *
     * @return  A DockerClient object.
     */
    public DockerClient initializeDockerClient() {
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder().build();
        return DockerClientBuilder.getInstance(config).build();
    }


    /**
     * Requests a List of all available containers.
     *
     * @return  A a List edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getContainerList() {
        DockerClient dockerClient = initializeDockerClient();
        List<Container> containers = dockerClient.listContainersCmd().withShowAll(false).exec();
		Function<Container, _Container> mapContainerTo_Container = new Function<Container, _Container>() {
		    public _Container apply(Container c) {
		        _Container _container = new _Container();
                _container.setCommand(c.getCommand());
                _container.setCreated(c.getCreated());
				_container.setId(c.getId());
				_container.setImage(c.getImage());
				_container.setImageId(c.getImageId());
				_container.setNames(c.getNames());
				_container.setPorts(getContainerPort(c.getPorts()));
				_container.setLabels(c.getLabels());
				_container.setStatus(c.getStatus());
				_container.setSizeRw(c.getSizeRw());
				_container.setSizeRootFs(c.getSizeRootFs());
                _container.setHostConfig(getContainerHostConfig(c.getHostConfig()));
				_container.setNetworkSettings(getContainerNetworkSettings(c.getNetworkSettings()));
		        return _container;
		    }
		};
		List<_Container> _containers = new ArrayList<_Container>(containers.size());
		for (Container container : containers) {
			_Container _container = mapContainerTo_Container.apply(container);
			_containers.add(_container);
		}
		return _containers;
    }
	
    /**
     * Requests Docker Container Port information.
     *
	 * @param containerPort  The com.github.dockerjava.api.model.ContainerPort object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._ContainerPort object.
     */
    public _ContainerPort[] getContainerPort(ContainerPort[] containerPort) {
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
		_ContainerPort[] _containerPorts = new _ContainerPort[containerPort.length];
        for(int i=0; i<containerPort.length; i++){
		    _containerPorts[i] = mapContainerPortTo_ContainerPort.apply(containerPort[i]);
		}
        return _containerPorts;
	}
	
    /**
     * Requests Docker Container Host Configuration information.
     *
	 * @param containerHostConfig  The com.github.dockerjava.api.model.ContainerHostConfig object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._ContainerHostConfig object.
     */
    public _ContainerHostConfig getContainerHostConfig(ContainerHostConfig containerHostConfig) {
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
     * Requests Docker Container Network Settings information.
     *
	 * @param containerNetworkSettings  The com.github.dockerjava.api.model.ContainerNetworkSettings object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetworkSettings object.
     */
    public _ContainerNetworkSettings getContainerNetworkSettings(ContainerNetworkSettings containerNetworkSettings) {
        Function<ContainerNetworkSettings, _ContainerNetworkSettings> mapContainerNetworkSettingsTo_ContainerNetworkSettings = new Function<ContainerNetworkSettings, _ContainerNetworkSettings>() {
            public _ContainerNetworkSettings apply(ContainerNetworkSettings c) {
                _ContainerNetworkSettings _containerNetworkSettings = new _ContainerNetworkSettings();
				_containerNetworkSettings.setNetworks(getContainerNetwork(c.getNetworks()));
                return _containerNetworkSettings;
            }
        };
		_ContainerNetworkSettings _containerNetworkSettings = mapContainerNetworkSettingsTo_ContainerNetworkSettings.apply(containerNetworkSettings);
        return _containerNetworkSettings;
	}
	
    /**
     * Requests Docker Container Network information.
     *
	 * @param containerNetwork  The com.github.dockerjava.api.model.ContainerNetwork object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetwork object.
     */
    public Map<String, _ContainerNetwork> getContainerNetwork(Map<String, ContainerNetwork> networks) {
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
		Map<String, _ContainerNetwork> _containerNetworkMap = new HashMap <String, _ContainerNetwork>(networks.size());
		for (Map.Entry<String, ContainerNetwork> entry : networks.entrySet()) {
			_containerNetworkMap.put(entry.getKey(), mapContainerNetworkTo_ContainerNetwork.apply(entry.getValue()));
		}
        return _containerNetworkMap;
	}
	
    /**
     * Requests a List of all available containers corresponding to an image.
     *
     * @param image  The container's image.
     * @return  A List of edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getContainerListByImage(String image) {
        List<_Container> _containers = getContainerList();   
        _Container _container = null;
		ArrayList<_Container> _containerList = new ArrayList<_Container>();
        for (_Container c : _containers) {
            if (image.equals(c.getImage())) {
                _container = c; 
				_containerList.add(_container);
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
        List<_Container> _containers = getContainerList();   
        _Container _container = null;
		ArrayList<_Container> _containerList = new ArrayList<_Container>();
        for (_Container c : _containers) {
            if (image.equals(c.getImage())) {
				if (!StringUtils.contains(c.getStatus(), "Exited")) {
                    _container = c; 
				    _containerList.add(_container);
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
        List<_Container> _containers = getContainerList();   
		HashMap<String, String> _containerStatusMap = new HashMap<String, String>();
        for (_Container c : _containers) {
            if (!StringUtils.contains(c.getStatus(), "Exited")) {
                _containerStatusMap.put(c.getImage(), c.getStatus());
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
	
	
/*

    public InspectContainerResponse inspectContainer(String id) {
        DockerClient dockerClient = initializeDockerClient();
        return dockerClient.inspectContainerCmd(id).exec();
    }



    public CreateContainerResponse createContainer(NewContainer newContainer) {
        DockerClient dockerClient = initializeDockerClient();
        return dockerClient.createContainerCmd(newContainer.getImageRepository())
           .withTty(true)
           .withPublishAllPorts(true)
           .withName(newContainer.getName())
           .withHostName(newContainer.getHostName())
           .exec();
    }


    public CreateContainerResponse createContainer(String image) {
        DockerClient dockerClient = initializeDockerClient();
        return dockerClient.createContainerCmd(image)
           .withTty(true)
           .exec();
    }
	
	

    public void removeContainer(Container container) {
        DockerClient dockerClient = initializeDockerClient();
		dockerClient.removeContainerCmd(container.getId()).exec();
	}


    public void startContainer(Container container) {
        DockerClient dockerClient = initializeDockerClient();
        dockerClient.startContainerCmd(container.getId()).exec();
    }


    public void stopContainer(Container container) {
        DockerClient dockerClient = initializeDockerClient();
        dockerClient.stopContainerCmd(container.getId()).exec();
    }


    public void stopContainer(Container container, int timeout) {
        DockerClient dockerClient = initializeDockerClient();
        dockerClient.stopContainerCmd(container.getId()).withTimeout(timeout).exec();
    }
*/
		
}
