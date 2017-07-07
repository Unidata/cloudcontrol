package edu.ucar.unidata.cloudcontrol.repository.docker;

import java.util.Date;
import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.docker.ContainerMapping;
import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;

/**
 * The data access object representing a ContainerMapping.
 */
public interface ContainerMappingDao {

    /**
     * Looks up and retrieves the ContainerMapping from the persistence mechanism using the container mapping ID.
     *
     * @param containerMappingId   The ID of the ContainerMapping object (will be unique for each ContainerMapping).
     * @return  The ContainerMapping.
     */
    public ContainerMapping lookupContainerMappingById(int containerMappingId);
    
    /**
     * Looks up and retrieves a List of all ContainerMappings by user name.
     *
     * @param userName  The user who created the ContainerMapping.
     * @return  The List of ContainerMappings.
     */
    public  List<ContainerMapping> lookupContainerMappingsByUserName(String userName);
    
    /**
     * Looks up and retrieves the ContainerMapping from the persistence mechanism using the _Container.
     *
     * @param _container  The _container corresponding to the ContainerMapping object.
     * @return  The ContainerMapping.
     */
    public ContainerMapping lookupContainerMappingbyContainer(_Container _container);
    
    /**
     * Looks up and retrieves a List of all ContainerMappings by _Image.
     *
     * @param _Image  The _image corresponding to the ContainerMapping object.
     * @return  The List of ContainerMappings.
     */
    public  List<ContainerMapping> lookupContainerMappingsByImage(_Image _image);
    
    /**
     * Looks up and retrieves a List of all ContainerMappings by Date Performed.
     *
     * @param datePerformed  The date perfomed corresponding to the ContainerMapping object.
     * @return  The List of ContainerMappings.
     */
    public  List<ContainerMapping> lookupContainerMappingsByDatePerfomed(Date datePerformed);

    /**
     * Looks up and retrieves a List of all ContainerMappings from the persistence mechanism.
     *
     * @return  The List of ContainerMappings.
     */
    public List<ContainerMapping> getAllContainerMappings();

    /**
     * Finds and removes the ContainerMapping from the persistence mechanism using the ContainerMapping ID.
     *
     * @param containerMappingId  The ID of the ContainerMapping object.
     */
    public void deleteContainerMapping(String containerMappingId);

    /**
     * Creates a new ContainerMapping in the persistence mechanism.
     *
     * @param containerMapping  The ContainerMapping to be created.
     */
    public void createContainerMapping(ContainerMapping containerMapping);

}
