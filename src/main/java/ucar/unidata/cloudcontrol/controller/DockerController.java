package edu.ucar.unidata.cloudcontrol.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Container;

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
            return "docker/inspectImage";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());    
	        List<Image> images = imageManager.getImageList();           
	        model.addAttribute("images", images);    
	        return "docker/listImages";
        }
    }

    /**
     * Accepts a GET request for a List of com.github.dockerjava.api.model.Container objects.
     *
     * View is a list of all Container objects. The view can
	 * handle an empty list of Containers if none are found.
     * 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/container/list", method=RequestMethod.GET)
    public String listContainers(Model model) { 
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
     * @param id  The Container ID as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/container/{id}", method=RequestMethod.GET)
    public String inspectContainer(@PathVariable String id, Model model) { 
        try {
            InspectContainerResponse inspectContainerResponse = containerManager.inspectContainer(id);         
            model.addAttribute("inspectContainerResponse", inspectContainerResponse);       
            return "docker/inspectContainer";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());    
	        List<Container> containers = containerManager.getContainerList();           
	        model.addAttribute("containers", containers);    
	        return "docker/listContainers";
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
    public String createContainer(Model model) { 
        model.addAttribute("newContainer", new NewContainer());       
		return "docker/createContainer";
    }
	
	
    /**
     * Accepts a POST request to create a new com.github.dockerjava.api.command.InspectContainerResponse object.
     *
     * View is either the newly created User object, or the web form to create a 
     * new User if: 
     * 1) a User of the same user name has already exists in the database, 
     * 2) or if there are validation errors with the user input. 
     * 
     * @param newContainer  The NewContainer object. 
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View. 
     */
    @RequestMapping(value="/docker/container/create", method=RequestMethod.POST)
    public ModelAndView createUser(@Valid NewContainer newContainer, BindingResult result, Model model) {  
        if (result.hasErrors()) {
           return new ModelAndView("docker/createContainer"); 
        } else {
            CreateContainerResponse createContainerResponse = containerManager.createContainer(newContainer);      
            model.addAttribute("createContainerResponse", createContainerResponse);       
            return new ModelAndView("docker/inspectContainer");
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
