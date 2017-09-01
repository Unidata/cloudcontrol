package edu.ucar.unidata.cloudcontrol.service.docker;

import edu.ucar.unidata.cloudcontrol.domain.docker.ContainerMapping;
import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.service.docker.ClientManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerMappingManager;
import edu.ucar.unidata.cloudcontrol.service.docker.converters.ContainerConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;

import org.springframework.dao.DataRetrievalFailureException;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;

/**
 * Service for processing Docker container-related api requests.
 */
public class ContainerManagerImpl implements ContainerManager {

    protected static Logger logger = Logger.getLogger(ContainerManagerImpl.class);

    @Resource(name = "clientManager")
    private ClientManager clientManager;

    @Resource(name = "containerMappingManager")
    private ContainerMappingManager containerMappingManager;

    /**
     * Requests a List of all available containers.
     *
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getContainerList() {
        List<_Container> _containers;
        DockerClient dockerClient = clientManager.initializeDockerClient();
        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
        ContainerConverter containerConverter = new ContainerConverter();
        _containers = containerConverter.processContainerList(containers);
        return _containers;
    }

    /**
     * Requests a List of all available containers corresponding to an image.
     *
     * @param imageId  The container's Image ID.
     * @return  A List of edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getContainerListByImage(String imageId) {
        List<_Container> _containerList = new ArrayList<_Container>();
        List<_Container> _containers = getContainerList();
        if (!_containers.isEmpty()) {
            _Container _container;
            _containerList = new ArrayList<_Container>();
            for (_Container c : _containers) {
                if (imageId.equals(c.getImageId())) {
                    _container = c;
                    _containerList.add(_container);
                }
            }
        }
        return _containerList;
    }

    /**
     * Requests a List of all available RUNNING containers corresponding to an image.
     * possible status types: created|restarting|running|paused|exited
     *
     * @param imageId  The container's Image ID.
     * @return  A List of running edu.ucar.unidata.cloudcontrol.domain.docker._Container objects.
     */
    public List<_Container> getRunningContainerListByImage(String imageId) {
        List<_Container> _containerList = new ArrayList<_Container>();
        List<_Container> _containers = getContainerList();
        if (!_containers.isEmpty()) {
            _Container _container;
            _containerList = new ArrayList<_Container>();
            for (_Container c : _containers) {
                if (imageId.equals(c.getImageId())) {
                    if (!StringUtils.contains(c.getStatus(), "Exited")) {
                        if (!StringUtils.contains(c.getStatus(), "Created")) {
                            if (!StringUtils.contains(c.getStatus(), "Paused")) {
                                _container = c;
                                _containerList.add(_container);
                            }
                        }
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
        Map<String, String> _containerStatusMap = new HashMap<String, String>();
        List<_Container> _containers = getContainerList();
        if (!_containers.isEmpty()) {
            _containerStatusMap = new HashMap<String, String>();
            for (_Container c : _containers) {
                _containerStatusMap.put(c.getImage(), c.getStatus());
            }
        }
        return _containerStatusMap;
    }

    /**
     * Requests a single _Container object.
     *
     * @param containerId  The container ID.
     * @return  The edu.ucar.unidata.cloudcontrol.domain.docker._Container object.
     * @throws NotFoundException  If unable to find the container.
     */
    public _Container getContainer(String containerId) {
        List<_Container> _containers = getContainerList();
        _Container _container = null;
        for (_Container c : _containers) {
            if (containerId.equals(c.getId())) {
                _container = c;
                break;
            }
        }
        if (Objects.isNull(_container)) {
            String message = "Unable to find Container " + containerId;
            logger.error(message);
            throw new NotFoundException(message);
        }
        return _container;
    }

    /**
     * Starts a Docker container.
     *
     * @param imageId  The ID of the image in which to start the container.
     * @param authenticatedUser  The user that the started container.
     * @return  The ID of the container that has been started.
     * @throws NotModifiedException  If container is already running or if its status is detected as anything but running.
     * @throws NotFoundException  If unable to find the container to start it.
     */
    public String startContainer(String imageId, String authenticatedUser) {
        String containerId;
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            CreateContainerResponse createContainerResponse = createContainer(dockerClient, imageId);
            containerId = createContainerResponse.getId().substring(0,12);
            dockerClient.startContainerCmd(containerId).exec();

            // further checking to see if the container is running
            if (!containerIsRunning(dockerClient, containerId)) {
                String message = "Container " + containerId + " is not running when it should be.";
                logger.error(message);
                throw new NotModifiedException(message);
            } else {
                try {
                    // add mapping
                    ContainerMapping containerMapping = new ContainerMapping();
                    containerMapping.setContainerId(containerId);
                    containerMapping.setImageId(imageId);
                    containerMapping.setUserName(authenticatedUser);
                    containerMapping.setDatePerformed(new Date());
                    containerMappingManager.createContainerMapping(containerMapping);
                } catch (DataRetrievalFailureException e) {
                    // can't add mapping, stop the container
                    logger.info("Stopping container " + containerId + ": " + e);
                    stopContainer(containerId);
                    throw e;
                }
            }
        } catch (NotModifiedException e) {
            logger.error("Container is already running: " + e);
            throw e;
        } catch (NotFoundException e) {
            logger.error("Unable to find and start container: " + e);
            throw e;
        }
        return containerId;
    }

    /**
     * Stops a Docker container.
     *
     * @param containerId  The ID of the container to stop.
     * @throws NotModifiedException  If container has already been stopped or if its status is detected as running.
     * @throws NotFoundException  If unable to find the container to stop it.
     */
    public void stopContainer(String containerId) {
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            dockerClient.stopContainerCmd(containerId).exec();

            // further checking to confirm the container has stopped running
            if (containerIsRunning(dockerClient, containerId)) {
                String message = "Container " + containerId + " is still running when it should not be.";
                logger.error(message);
                throw new NotModifiedException(message);
            } else {
                // remove the container completely
                removeSingleContainerFromImage(getContainer(containerId).getImageId(), containerId);
            }
        } catch (NotModifiedException e) {
            logger.error("Container " + containerId + " has been already stopped: " + e);
            throw e;
        } catch (NotFoundException e) {
            logger.error("Unable to find and stop container " + containerId + ": " + e);
            throw e;
        }
    }

    /**
     * Requests whether the edu.ucar.unidata.cloudcontrol.domain.docker._Container is running or not.
     *
     * @param id  The ID of the _Container to check.
     * @param dockerClient  The initialized DockerClient to use.
     * @return  The whether the container is running or not.
     */
    public boolean containerIsRunning(DockerClient dockerClient, String id){
        InspectContainerResponse inspectContainerResponse = inspectContainer(dockerClient, id);
        if (inspectContainerResponse.getState().getStatus().equals("running")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a CreateContainerResponse object.
     *
     * @param dockerClient  The DockerClient object to use.
     * @param imageId  The ID of the image we want container-ize.
     * @return  The CreateContainerResponse.
     * @throws ConflictException  If container already exists.
     * @throws NotFoundException  If unable to create container.
     */
     public CreateContainerResponse createContainer(DockerClient dockerClient, String imageId) {
         try {
             return dockerClient.createContainerCmd(imageId).exec();
         } catch (ConflictException e) {
             logger.error("Unable to create Container.  Container already exists: " + e);
             throw e;
         } catch (NotFoundException e) {
             logger.error("Unable to create Container: " + e);
             throw e;
         }
     }

    /**
     * Returns a requested InspectContainerResponse.
     *
     * @param dockerClient  The DockerClient object to use.
     * @param containerId  The Container ID to inspect.
     * @return  The InspectContainerResponse.
     * @throws NotFoundException  If unable to find container to inspect.
     */
    public InspectContainerResponse inspectContainer(DockerClient dockerClient, String containerId) {
        try {
            return dockerClient.inspectContainerCmd(containerId).exec();
        } catch (NotFoundException e) {
            logger.error("Unable to find Container " + containerId  + " to inspect: " + e);
            throw e;
        }
    }

    /**
     * Removes all Containers from an Image.
     *
     * @param imageId  The ID of the Image whose Containers need to be removed.
     * @throws NotFoundException  If unable to remove containers for requested image.
     */
    public void removeContainersFromImage(String imageId) {
        try {
            DockerClient dockerClient = clientManager.initializeDockerClient();
            List<_Container> _containers = getContainerListByImage(imageId);
            if (!_containers.isEmpty()) {
                for (_Container _container : _containers) {
                    if (containerIsRunning(dockerClient, _container.getId())) {
                        stopContainer(_container.getId());
                    }
                    dockerClient.removeContainerCmd(_container.getId()).withForce(true).exec();
                    _Container _c = getContainer(_container.getId());
                }
            }
        } catch (NotFoundException e) {
            logger.error("Unable to find and remove Containers for image " + imageId + ": " + e);
            throw e;
        }
    }

    /**
     * Removes a single Container from an Image.
     *
     * @param imageId  The ID of the Image corresponding to the Container.
     * @param containerId  The ID of the Container to remove.
     * @throws NotFoundException  If unable to remove the requested container for requested image.
     */
    public void removeSingleContainerFromImage(String imageId, String containerId) {
        DockerClient dockerClient = clientManager.initializeDockerClient();
        List<_Container> _containers = getContainerListByImage(imageId);
        if (!_containers.isEmpty()) {
            for (_Container _container : _containers) {
                if (_container.getId().equals(containerId)) {
                    if (containerIsRunning(dockerClient, _container.getId())) {
                        stopContainer(_container.getId());
                    }
                    dockerClient.removeContainerCmd(_container.getId()).withForce(true).exec();
                    _Container _c = getContainer(_container.getId());
                }
            }
        } else {
            String message = "Unable to find and remove container " + containerId + " for image " + imageId;
            logger.error(message);
            throw new NotFoundException(message);
        }
    }
}
