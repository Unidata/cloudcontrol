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

import edu.ucar.unidata.cloudcontrol.domain.DockerImage;
import edu.ucar.unidata.cloudcontrol.service.DockerImageManager;
import edu.ucar.unidata.cloudcontrol.service.DockerImageValidator;

/**
 * Controller to issue rudimentary docker commands.
 */

@Controller
public class DockerController implements HandlerExceptionResolver {

    protected static Logger logger = Logger.getLogger(DockerController.class);

    @Resource(name="dockerImageManager")
    private DockerImageManager dockerImageManager;
	
    @Autowired
    private DockerImageValidator dockerImageValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(dockerImageValidator);  
    } 

    /**
     * Accepts a GET request for a List of all DockerImage objects.
     *
     * View is a list of all DockerImage objects from the given host. The
	 * view can handle an empty list of DockerImages if none are found.
     * 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/docker/images", method=RequestMethod.GET)
    public String listDockerImages(Model model) { 
        List<DockerImage> dockerImages = dockerImageManager.getDockerImageList();           
        model.addAttribute("dockerImages", dockerImages);    
        return "listDockerImages";
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
