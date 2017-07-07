package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;
import java.util.Map;

import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse;

/**
 * Service for processing Docker image-related api requests.
 */
public interface ImageManager {

    /**
     * Requests a List of all available _Image objects.
     *
     * @return  A List edu.ucar.unidata.cloudcontrol.domain.docker._Image objects.
     */
    public List<_Image> getImageList();

    /**
     * Requests a specific _Image object.
     *
     * @param imageId  The _Image ID.
     * @return  The _Image.
     */
    public _Image getImage(String imageId);

    /**
     * Returns the reposnse to a request image request.
     *
     * @param imageId  The Image ID.
     * @return  The edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse object.
     */
    public _InspectImageResponse inspectImage(String imageId);

    /**
     * Requests a Map of the statuses of all available _Image objects.
     *
     * @return  A Map edu.ucar.unidata.cloudcontrol.domain.docker._Image statuses.
     */
    public Map<String, String> getImageStatusMap();

    /**
     * Requests a Map of the statuses of the provided List of _Image objects.
     *
     * @param _images  The List of _Images to use when getting the status information.
     * @return  A Map edu.ucar.unidata.cloudcontrol.domain.docker._Image statuses.
     */
    public Map<String, String> getImageStatusMap(List<_Image> _images);

    /**
     * Removes an Image from the Docker instance.
     *
     * @param imageId  The ID of the Image to remove.
     * @param userName  The userName of person who is removing the Image (for logging purposes)
     * @return  The whether the Image was successfully removed or not.
     */
    public boolean removeImage(String imageId, String userName);
}
