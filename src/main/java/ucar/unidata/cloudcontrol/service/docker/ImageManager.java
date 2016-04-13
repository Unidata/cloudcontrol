package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;
import java.util.Map;

import edu.ucar.unidata.cloudcontrol.domain.docker._Image;

/**
 * Service for processing Docker image-related api requests.
 */
public interface ImageManager {

    /**
     * Requests a List of all available _Image objects.
     *
     * @return  A a List edu.ucar.unidata.cloudcontrol.domain.docker._Info objects.
     */
    public List<_Image> getImageList();
	
    /**
     * Requests a single Image.
     * 
	 * @param id  The Image ID.
     * @return  The Image.   
     */
 //   public Image getImage(String id);
	
    /**
     * Requests an Image's repository.
     * 
     * @param image  The Image.
     * @return  The Image repository (String).  
     */
//	public String getImageRepository(Image image);
	
    /**
     * Requests an Image's Container ID.
     * 
     * @param image  The Image.
     * @return  The Image Container ID (String).  
     */
//    public String getImageContainerId(Image image);
	
    /**
     * Requests a Map of the Image's repositories (for form dropdown selection).
     * 
     * @return  The repository Map<String,String>.
     */
//    public Map<String,String> getRepositoryMap();
	
    /**
     * Requests a search of the available Images with a given query.
     * 
     * @param query  The query upon which to search.
     * @return  A List of resulting SearchItems.
     */
 //   public List<SearchItem> searchImages(String query);

    /**
     * Returns a requested InspectImageResponse.
     * 
     * @param id  The Image ID.
     * @return  The requested InspectImageResponse.  
     */
 //   public InspectImageResponse inspectImage(String id);

}
