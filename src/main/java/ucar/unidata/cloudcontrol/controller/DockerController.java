package edu.ucar.unidata.cloudcontrol.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.SearchItem;

import edu.ucar.unidata.cloudcontrol.domain.docker.NewContainer;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ImageManager;
import edu.ucar.unidata.cloudcontrol.service.docker.NewContainerValidator;

/**
 * Controller to issue rudimentary docker commands.
 */

@Controller
public class DockerController implements HandlerExceptionResolver {

    protected static Logger logger = Logger.getLogger(DockerController.class);

    @Resource(name="imageManager")
    private ImageManager imageManager;
    
    @Resource(name="containerManager")
    private ContainerManager containerManager;
    
    @Autowired
    private NewContainerValidator newContainerValidator;

    @InitBinder("newContainer")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(newContainerValidator);  
    }   
    
    /**
     * Accepts a GET request for a List of com.github.dockerjava.api.model.Image objects.
     *
     * View is a list of all Image objects from the given host. The
     * view can handle an empty list of Images if none are found.
     * 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/image/list", method=RequestMethod.GET)
    public String listImages(Model model) { 
        List<Image> images = imageManager.getImageList();           
        model.addAttribute("images", images);   
        Map<String, String> statusMap = containerManager.getContainerStatusMap();         
        model.addAttribute("statusMap", statusMap);
        return "docker/listImages";
    }
    
    /**
     * Accepts a GET request for a com.github.dockerjava.api.command.InspectImageResponse object.
     *
     * View is the requested InspectImageResponse (details corresponding to an Image), 
     * or a list of all Image objects if unable to find the requested InspectImageResponse.
     * 
     * @param id  The Image ID as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/image/{id}", method=RequestMethod.GET)
    public String inspectImage(@PathVariable String id, Model model) { 
        try {
            InspectImageResponse inspectImageResponse = imageManager.inspectImage(id);         
            model.addAttribute("inspectImageResponse", inspectImageResponse);     
            Image image = imageManager.getImage(id); 
            if (image != null) {
                model.addAttribute("image", image);  
            } else {
                List<Image> images = imageManager.getImageList();     
                model.addAttribute("error", "Unable to load Image information.");            
                model.addAttribute("images", images); 
                return "docker/listImages";   
            }
            return "docker/inspectImage";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());    
            List<Image> images = imageManager.getImageList();           
            model.addAttribute("images", images);    
            return "docker/listImages";
        }
    }
    
    
    /**
     * Accepts a GET request to start a specific Image.
     *
     * The Docker Remote API and docker-java client run command is Container-
     * centric, meaning you have to start the Container associated with the Image.
     * Hence, we create a new Container for the Image and start the Container.
     *
     * View is the updated list of Image objects with run status indicated.
     *    
     * @param id  The Image ID as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/image/{id}/start", method=RequestMethod.GET)
    public String startImage(@PathVariable String id, Model model) { 
        CreateContainerResponse createContainerResponse = containerManager.createContainer(id);   
        String containerId = createContainerResponse.getId();
        try {
            containerManager.startContainer(containerManager.getContainer(containerId));  
        } catch (NotModifiedException e) {
            model.addAttribute("error", "Container is already running.");    
        } catch (NotFoundException e) {
            logger.error("Unable to find and start container: " + e.getMessage()); 
            model.addAttribute("error", "Unable to start image. Unable to find and start container for image with id: " + id + ".");    
        }
        InspectContainerResponse inspectContainerResponse = containerManager.inspectContainer(containerId); 
        if (!inspectContainerResponse.getState().isRunning()) {
            logger.error("Container " + containerId + " is not running when it should be: " + String.valueOf(inspectContainerResponse.getState().getExitCode()));
            model.addAttribute("error", "Unable to start Image.");    
        }    
        List<Image> images = imageManager.getImageList();       
        model.addAttribute("images", images);
        Map<String, String> statusMap = containerManager.getContainerStatusMap();         
        model.addAttribute("statusMap", statusMap);
        return "redirect:/docker/image/list";  
    }   
	
    /**
     * Accepts a GET request to stop a specific Image.
     *
     * The Docker Remote API and docker-java client run command is Container-
     * centric, meaning you have to stop the Container associated with the Image.
     *
     * View is the updated list of Image objects with run status indicated.
     *    
     * @param id  The Image ID as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/image/{id}/stop", method=RequestMethod.GET)
    public String stopImage(@PathVariable String id, Model model) { 
        List<Container> containers = containerManager.getRunningContainerListByImage(id);  
		if (containers.isEmpty()) {
            logger.error("Unable to find any running containers for image: " + id); 
            model.addAttribute("error", "Unable to find any running containers for image: " + id);  
		} else {
			Container container = null;
			for (Container c : containers) {
				container = c;  // ugh.  Assuming there is only on container for now.
			}
            try {
                containerManager.stopContainer(container);  
            } catch (NotModifiedException e) {
                model.addAttribute("error", "Container status hasn't changed.");    
            } catch (NotFoundException e) {
                logger.error("Unable to find and stop container: " + e.getMessage()); 
                model.addAttribute("error", "Unable to stop image. Unable to find and stop container for image with id: " + id + ".");    
            }
            InspectContainerResponse inspectContainerResponse = containerManager.inspectContainer(container.getId()); 
            if (inspectContainerResponse.getState().isRunning()) {
                logger.error("Container " + container.getId() + " is still running when it should not be: " + String.valueOf(inspectContainerResponse.getState().getExitCode()));
                model.addAttribute("error", "Unable to stop image.");    
            }   
	    }
        List<Image> images = imageManager.getImageList();       
        model.addAttribute("images", images);
        Map<String, String> statusMap = containerManager.getContainerStatusMap();         
        model.addAttribute("statusMap", statusMap);
        return "redirect:/docker/image/list";  
    }    
    
    
    

    /**
     * Accepts a GET request for a List of com.github.dockerjava.api.model.Container objects.
     *
     * View is a list of all Container objects. The view can
     * handle an empty list of Containers if none are found.
     * 
     * @param error An error (if provided).
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/container/list", method=RequestMethod.GET)
    public String listContainers(@RequestParam(value = "error", required = false) String error, Model model) { 
        model.addAttribute("error", error);
        List<Container> containers = containerManager.getContainerList();           
        model.addAttribute("containers", containers);    
        return "docker/listContainers";
    }

    /**
     * Accepts a GET request for the creation of a com.github.dockerjava.api.model.Container object.
     *
     * View is the requested InspectContainerResponse (details corresponding to an Container), 
     * or a list of all Container objects if unable to find the requested InspectContainerResponse.
     * 
     * @param error An error (if provided).
     * @param id  The Container ID as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/container/{id}", method=RequestMethod.GET)
    public String inspectContainer(@RequestParam(value = "error", required = false) String error, @PathVariable String id, Model model) {
        model.addAttribute("error", error);
        try {
            InspectContainerResponse inspectContainerResponse = containerManager.inspectContainer(id);         
            model.addAttribute("inspectContainerResponse", inspectContainerResponse);    
            Container container = containerManager.getContainer(id); 
            if (container != null) {
                model.addAttribute("container", container);  
            } else {
                List<Container> containers = containerManager.getContainerList();           
                model.addAttribute("error", "Unable to load Container information.");            
                model.addAttribute("containers", containers);  
                return "docker/listContainers";  
            }
            return "docker/inspectContainer";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());    
            List<Container> containers = containerManager.getContainerList();           
            model.addAttribute("containers", containers);    
            return "docker/listContainers";
        }
    }
    
    /**
     * Accepts a GET request to start a specific Container.
     *
     * View is one of the following: 
     * 1) the inspectContainer view with the updated Container status if successful;
     * 2) the inspectContainer view if Container state is shown as not running (unsuccessful);
     * 3) the listContainers view if unable to find the Container or some exception.
     *    
     * @param id  The Container ID as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/container/{id}/start", method=RequestMethod.GET)
    public String startContainer(@PathVariable String id, Model model) { 
        Container container = containerManager.getContainer(id); 
        if (container != null) {
            try {
                containerManager.startContainer(container);  
            } catch (NotModifiedException e) {
                model.addAttribute("error", "Container is already running.");    
            } catch (NotFoundException e) {
                logger.error("Unable to find and start container: " + e.getMessage());
                model.addAttribute("error", "Unable to find and start container.");    
                List<Container> containers = containerManager.getContainerList();           
                model.addAttribute("containers", containers);    
                return "redirect:/docker/container/list";  
            }
            InspectContainerResponse inspectContainerResponse = containerManager.inspectContainer(id); 
            if (!inspectContainerResponse.getState().isRunning()) {
                logger.error(String.valueOf(inspectContainerResponse.getState().getExitCode()));
                model.addAttribute("error", "Unable to start container.  Container state is listed as not running.");    
            }        
            model.addAttribute("inspectContainerResponse", inspectContainerResponse); 
            model.addAttribute("container", container);  
            return "redirect:/docker/container/{id}";
        } else {
            List<Container> containers = containerManager.getContainerList();           
            model.addAttribute("error", "Unable to start container. Container with ID: " + id + " not found.");            
            model.addAttribute("containers", containers);  
            return "redirect:/docker/container/list";  
        }
    }
    
    
    /**
     * Accepts a GET request to stop a specific Container.
     *
     * View is one of the following: 
     * 1) the inspectContainer view with the updated Container status if successful;
     * 2) the inspectContainer view if Container state is shown as running (unsuccessful);
     * 3) the listContainers view if unable to find the Container or some exception occurs.
     * 
     * @param id  The Container ID as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/container/{id}/stop", method=RequestMethod.GET)
    public String stopContainer(@PathVariable String id, Model model) { 
        Container container = containerManager.getContainer(id); 
        if (container != null) {
            try {
                containerManager.stopContainer(container);  
            } catch (NotModifiedException e) {
                model.addAttribute("error", "Container is not running.");    
            } catch (NotFoundException e) {
                logger.error("Unable to find and stop container: " + e.getMessage());
                model.addAttribute("error", "Unable to find and stop container.");    
                List<Container> containers = containerManager.getContainerList();           
                model.addAttribute("containers", containers);    
                return "redirect:/docker/container/list";  
            }
            InspectContainerResponse inspectContainerResponse = containerManager.inspectContainer(id); 
            if (inspectContainerResponse.getState().isRunning()) {
                logger.error(String.valueOf(inspectContainerResponse.getState().getExitCode()));
                model.addAttribute("error", "Unable to stop container.  Container state is listed as still running.");    
            }        
            model.addAttribute("inspectContainerResponse", inspectContainerResponse); 
            model.addAttribute("container", container);  
            return "redirect:/docker/container/{id}";
        } else {
            List<Container> containers = containerManager.getContainerList();           
            model.addAttribute("error", "Unable to stop container. Container with ID: " + id + " not found.");            
            model.addAttribute("containers", containers);  
            return "redirect:/docker/container/list";  
        }
    }

    /**
     * Accepts a GET request to create a new com.github.dockerjava.api.command.InspectContainerResponse object.
     *
     * View is a web form to collect attributes use to create a Container.
     * 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/container/create", method=RequestMethod.GET)
    public String createContainer(@RequestParam(value = "id", required = false) String id, Model model) { 
        model.addAttribute("newContainer", new NewContainer());     
        if (!StringUtils.isEmpty(id) ) {
            Image image = imageManager.getImage(id);
            String repository = imageManager.getImageRepository(image);
            NewContainer c = new NewContainer();
            c.setImageRepository(repository);
            model.addAttribute("newContainer", c);        
        }
        Map<String,String> imageMap = imageManager.getRepositoryMap();
        model.addAttribute("imageMap", imageMap);    
        return "docker/createContainer";
    }
    
    /**
     * Accepts a POST request to create a new com.github.dockerjava.api.command.InspectContainerResponse object.
     *
     * View is either inspectContainer for successful Container  
     * creation, or the web form to create a new Container.
     * 
     * @param newContainer  The NewContainer object. 
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View. 
     */
    @RequestMapping(value="/docker/container/create", method=RequestMethod.POST)
    public ModelAndView createContainer(@Valid NewContainer newContainer, BindingResult result, Model model) {  
        if (result.hasErrors()) {
           return new ModelAndView("docker/createContainer"); 
        } else {
            CreateContainerResponse createContainerResponse = containerManager.createContainer(newContainer);   
            String id = createContainerResponse.getId();
            try {
                InspectContainerResponse inspectContainerResponse = containerManager.inspectContainer(id);         
                model.addAttribute("inspectContainerResponse", inspectContainerResponse);    
                Container container = containerManager.getContainer(id); 
                if (container != null) {
                    model.addAttribute("container", container);  
                } else {
                    List<Container> containers = containerManager.getContainerList();           
                    model.addAttribute("error", "Unable to load Container information.");            
                    model.addAttribute("containers", containers);  
                    return new ModelAndView("docker/listContainers");  
                }
                return new ModelAndView("docker/inspectContainer");
            } catch (NotFoundException e) {
                model.addAttribute("error", e.getMessage());    
                List<Container> containers = containerManager.getContainerList();           
                model.addAttribute("containers", containers);    
                return new ModelAndView("docker/listContainers");
            } 
        }         
    }


    /**
     * This method gracefully handles any uncaught exception that are fatal 
     * in nature and unresolvable by the dockerImage.
     * 
     * @param request   The current HttpServletRequest request.
     * @param response  The current HttpServletRequest response.
     * @param handler  The executed handler, or null if none chosen at the time of the exception.  
     * @param exception  The  exception that got thrown during handler execution.
     * @return  The error page containing the appropriate message to the dockerImage. 
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        String message = "";
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> model = new HashMap<String, Object>();
        if (exception instanceof AccessDeniedException){ 
            message = exception.getMessage();
            modelAndView.setViewName("denied");
        } else  {
            message = "An error has occurred: " + exception.getClass().getName() + ": " + exception.getMessage();  
            modelAndView.setViewName("fatalError"); 
        }
        logger.error(message);       
        model.put("message", message);
        modelAndView.addAllObjects(model);
        return modelAndView;
    } 

}
