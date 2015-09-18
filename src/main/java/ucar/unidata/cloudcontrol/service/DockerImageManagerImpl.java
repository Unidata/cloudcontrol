package edu.ucar.unidata.cloudcontrol.service;;

import java.util.List;

import edu.ucar.unidata.cloudcontrol.domain.DockerImage;
import edu.ucar.unidata.cloudcontrol.repository.DockerImageDao;


/**
 * Service for processing DockerImage objects. 
 */
public class DockerImageManagerImpl implements DockerImageManager { 

    private DockerImageDao dockerImageDao;

    /**
     * Sets the data access object which will acquire the data 
     * and passed to it via the methods of this DockerImageManager. 
     * 
     * @param dockerImageDao  The service mechanism data access object representing a DockerImage. 
     */
    public void setDockerImageDao(DockerImageDao dockerImageDao) {
        this.dockerImageDao = dockerImageDao;
    }

    /**
     * Requests a List of all available DockerImages.
     * 
     * @return  A List of available DockerImages.   
     */
    public List<DockerImage> getDockerImageList() {
    	return dockerImageDao.getDockerImageList();
    }
   
    /**
     * Returns the number of available DockerImages.
     * 
     * @return  The total number of available DockerImages.   
     */
    public int getDockerImageCount() {
    	return dockerImageDao.getDockerImageCount();
    }
}
