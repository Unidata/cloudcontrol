package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;

import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.SearchItem;


/**
 * Service for processing com.github.dockerjava.api.model.Image objects. 
 */
public interface ImageManager {

    /**
     * Requests a List of all available Images.
     * 
     * @return  A List of available Images.   
     */
    public List<Image> getImageList();
	
    /**
     * Requests a single Image.
     * 
	 * @param id  The Image ID.
     * @return  The Image.   
     */
    public Image getImage(String id); 
	
    /**
     * Requests a search of the available Images with a given query.
     * 
     * @param query  The query upon which to search.
     * @return  A List of resulting SearchItems.
     */
    public List<SearchItem> searchImages(String query);

    /**
     * Returns a requested InspectImageResponse.
     * 
     * @param id  The Image ID.
     * @return  The requested InspectImageResponse.  
     */
    public InspectImageResponse inspectImage(String id);

}
