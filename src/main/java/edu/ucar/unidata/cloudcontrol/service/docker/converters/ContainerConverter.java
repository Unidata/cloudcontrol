package edu.ucar.unidata.cloudcontrol.service.docker.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.log4j.Logger;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ContainerHostConfig;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.ContainerNetworkSettings;
import com.github.dockerjava.api.model.ContainerPort;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Volume;

import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerHostConfig;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetwork;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerNetworkSettings;
import edu.ucar.unidata.cloudcontrol.domain.docker._ContainerPort;
import edu.ucar.unidata.cloudcontrol.domain.docker._InspectContainerResponse;
import edu.ucar.unidata.cloudcontrol.domain.docker._Volume;

/**
 * Service for converting com.github.dockerjava.* Container-related objects to a corresponding edu.ucar.unidata.* objects.
 */
public class ContainerConverter {
    
    protected static Logger logger = Logger.getLogger(ContainerConverter.class);
        
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
     * Converts a com.github.dockerjava.api.command.InspectContainerResponse object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._InspectContainerResponse object.
     *
     * @param inspectContainerResponse  The com.github.dockerjava.api.command.InspectContainerResponse object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._InspectContainerResponse object.
     */
    public _InspectContainerResponse convertInspectContainerResponse(InspectContainerResponse inspectContainerResponse) {
        Function<InspectContainerResponse, _InspectContainerResponse> mapInspectContainerResponseTo_InspectContainerResponse = new Function<InspectContainerResponse, _InspectContainerResponse>() {
            public _InspectContainerResponse apply(InspectContainerResponse i) {
                _InspectContainerResponse _inspectContainerResponse = new _InspectContainerResponse();
                _inspectContainerResponse.setArgs(i.getArgs());
                //_inspectContainerResponse.setConfig(i.getConfig()));
                _inspectContainerResponse.setCreated(i.getCreated());
                _inspectContainerResponse.setDriver(i.getDriver());
                _inspectContainerResponse.setExecDriver(i.getExecDriver());
                //_inspectContainerResponse.setHostConfig(i.getHostConfig());
                _inspectContainerResponse.setHostnamePath(i.getHostnamePath());
                _inspectContainerResponse.setHostsPath(i.getHostsPath());
                _inspectContainerResponse.setId(i.getId());
            //    _inspectContainerResponse.setSizeRootFs(i.getSizeRootFs()); // newer version of docker-java
                _inspectContainerResponse.setImageId(i.getImageId());
                _inspectContainerResponse.setMountLabel(i.getMountLabel());
                _inspectContainerResponse.setName(i.getName());
                _inspectContainerResponse.setRestartCount(i.getRestartCount());
                //_inspectContainerResponse.setNetworkSettings(i.getNetworkSettings());
                _inspectContainerResponse.setPath(i.getPath());
                _inspectContainerResponse.setProcessLabel(i.getProcessLabel());
                _inspectContainerResponse.setResolvConfPath(i.getResolvConfPath());
                _inspectContainerResponse.setExecIds(i.getExecIds());
                //_inspectContainerResponse.setState(convertContainerState(_inspectContainerResponse, i.getState()));
                //_inspectContainerResponse.setVolumes(i.getVolumes());
                //_inspectContainerResponse.setVolumesRW(i.getVolumesRW());
                _inspectContainerResponse.setMounts(processMountList(i.getMounts()));
                return _inspectContainerResponse;
            }
        };
        _InspectContainerResponse _inspectContainerResponse = mapInspectContainerResponseTo_InspectContainerResponse.apply(inspectContainerResponse);
        return _inspectContainerResponse;
    }
    
    /**
     * Converts a com.github.dockerjava.api.command.InspectContainerResponse.ContainerState object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._InspectContainerResponse._ContainerState object.
     *
     * @param containerState  A com.github.dockerjava.api.command.InspectContainerResponse.ContainerState object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._ContainerState object.
     */
/*
    public _InspectContainerResponse._ContainerState convertContainerState(_InspectContainerResponse _inspectContainerResponse, InspectContainerResponse.ContainerState containerState) {
        Function<InspectContainerResponse.ContainerState, _InspectContainerResponse._ContainerState> mapContainerStateTo_ContainerState = new Function<InspectContainerResponse.ContainerState, _InspectContainerResponse._ContainerState>() {
            public _InspectContainerResponse._ContainerState apply(InspectContainerResponse.ContainerState c) {
                _InspectContainerResponse._ContainerState _containerState = _inspectContainerResponse.new _ContainerState();
                _containerState.setStatus(c.getStatus());
                _containerState.setRunning(c.getRunning());
                _containerState.setPaused(c.getPaused());
                _containerState.setRestarting(c.getRestarting());
                _containerState.setOomKilled(c.getOOMKilled());
                _containerState.setDead(c.getDead());
                _containerState.setPid(c.getPid());
                _containerState.setExitCode(c.getExitCode());
                _containerState.setError(c.getError());
                _containerState.setStartedAt(c.getStartedAt());
                _containerState.setFinishedAt(c.getFinishedAt());
                return _containerState;
            }
        };
        _InspectContainerResponse._ContainerState _containerState = mapContainerStateTo_ContainerState.apply(containerState);
        return _containerState;
    }
*/
    
    /**
     * Utility method to process a List of com.github.dockerjava.api.command.InspectContainerResponse.Mount objects
     * to a corresponding List of edu.ucar.unidata.cloudcontrol.domain.docker._InspectContainerResponse._Mount objects.
     *
     * @param mounts  The com.github.dockerjava.api.command.InspectContainerResponse.Mount List.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._InspectContainerResponse._Mount List.
     */
    public List<_InspectContainerResponse._Mount> processMountList(List<InspectContainerResponse.Mount> mounts) {
        List<_InspectContainerResponse._Mount> _mounts = new ArrayList<_InspectContainerResponse._Mount>(mounts.size());
        for (InspectContainerResponse.Mount mount : mounts) {
            _InspectContainerResponse._Mount _mount = convertMount(mount);
            _mounts.add(_mount);
        }
        return _mounts;
    }
    
    /**
     * Converts a com.github.dockerjava.api.command.InspectContainerResponse.Mount object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._InspectContainerResponse._Mount object.
     *
     * @param mount  A com.github.dockerjava.api.command.InspectContainerResponse.Mount object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Mount object.
     */
    public _InspectContainerResponse._Mount convertMount(InspectContainerResponse.Mount mount) {
        Function<InspectContainerResponse.Mount, _InspectContainerResponse._Mount> mapMountTo_Mount = new Function<InspectContainerResponse.Mount, _InspectContainerResponse._Mount>() {
            public _InspectContainerResponse._Mount apply(InspectContainerResponse.Mount m) {
                _InspectContainerResponse._Mount _mount = new _InspectContainerResponse._Mount();
                _mount.setName(m.getName());
                _mount.setSource(m.getSource());
                _mount.setDestination(convertVolume(m.getDestination()));
                _mount.setDriver(m.getDriver());
                _mount.setRw(m.getRW());
                return _mount;
            }
        };
        _InspectContainerResponse._Mount _mount = mapMountTo_Mount.apply(mount);
        return _mount;
    }
    
    /**
     * Converts a com.github.dockerjava.api.model.Volume object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._Volume object.
     *
     * @param volume  The com.github.dockerjava.api.model.Volume object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Volume object.
     */
    public _Volume convertVolume(Volume volume) {
        Function<Volume, _Volume> mapVolumeTo_Volume = new Function<Volume, _Volume>() {
            public _Volume apply(Volume v) {
                _Volume _volume = new _Volume();
                _volume.setPath(v.getPath());
                return _volume;
            }
        };
        _Volume _volume = mapVolumeTo_Volume.apply(volume);
        return _volume;
    }
}
