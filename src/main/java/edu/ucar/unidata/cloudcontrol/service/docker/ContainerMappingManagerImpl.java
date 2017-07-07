package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ucar.unidata.cloudcontrol.domain.docker.ContainerMapping;
import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.repository.docker.ContainerMappingDao;


/**
 * Service for processing requests associated with ContainerMappings.
 */
public class ContainerMappingManagerImpl implements ContainerMappingManager {
    protected static Logger logger = Logger.getLogger(ContainerMappingManagerImpl.class);

    private ContainerMappingDao containerMappingDao;

    /**
     * Sets the data access object which will acquire and persist the data
     * passed to it via the methods of this ImageManager.
     *
     * @param containerMappingDao  The service mechanism data access object representing a ContainerMapping.
     */
    public void setContainerMappingDao(ContainerMappingDao containerMappingDao) {
        this.containerMappingDao = containerMappingDao;
    }

    /**
     * Looks up and retrieves the ContainerMapping from the persistence mechanism using the container mapping ID.
     *
     * @param containerMappingId   The ID of the ContainerMapping object (will be unique for each ContainerMapping).
     * @return  The ContainerMapping.
     */
    public ContainerMapping lookupContainerMappingById(int containerMappingId){
        return containerMappingDao.lookupContainerMappingById(containerMappingId);
    }

    /**
     * Looks up and retrieves a List of all ContainerMappings by user name.
     *
     * @param userName  The user who created the ContainerMapping.
     * @return  The List of ContainerMappings.
     */
    public  List<ContainerMapping> lookupContainerMappingsByUserName(String userName){
        return containerMappingDao.lookupContainerMappingsByUserName(userName);
    }

    /**
     * Looks up and retrieves the ContainerMapping from the persistence mechanism using the _Container.
     *
     * @param _container  The _container corresponding to the ContainerMapping object.
     * @return  The ContainerMapping.
     */
    public ContainerMapping lookupContainerMappingbyContainer(_Container _container){
        return containerMappingDao.lookupContainerMappingbyContainer(_container);
    }

    /**
     * Looks up and retrieves a List of all ContainerMappings by _Image.
     *
     * @param _Image  The _image corresponding to the ContainerMapping object.
     * @return  The List of ContainerMappings.
     */
    public  List<ContainerMapping> lookupContainerMappingsByImage(_Image _image){
        return containerMappingDao.lookupContainerMappingsByImage(_image);
    }

    /**
     * Looks up and retrieves a List of all ContainerMappings by Date Performed.
     *
     * @param datePerformed  The date perfomed corresponding to the ContainerMapping object.
     * @return  The List of ContainerMappings.
     */
    public  List<ContainerMapping> lookupContainerMappingsByDatePerfomed(Date datePerformed){
        return containerMappingDao.lookupContainerMappingsByDatePerfomed(datePerformed);
    }

    /**
     * Looks up and retrieves a List of all ContainerMappings from the persistence mechanism.
     *
     * @return  The List of ContainerMappings.
     */
    public List<ContainerMapping> getAllContainerMappings(){
        return containerMappingDao.getAllContainerMappings();
    }

    /**
     * Finds and removes the ContainerMapping from the persistence mechanism using the ContainerMapping ID.
     *
     * @param containerMappingId  The ID of the ContainerMapping object.
     */
    public void deleteContainerMapping(String containerMappingId){
        containerMappingDao.deleteContainerMapping(containerMappingId);
    }

    /**
     * Creates a new ContainerMapping in the persistence mechanism.
     *
     * @param containerMapping  The ContainerMapping to be created.
     */
    public void createContainerMapping(ContainerMapping containerMapping){
        containerMappingDao.createContainerMapping(containerMapping);
    }

}
