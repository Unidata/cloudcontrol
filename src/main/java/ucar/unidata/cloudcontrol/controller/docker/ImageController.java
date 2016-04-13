package edu.ucar.unidata.cloudcontrol.controller.docker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.StringWriter;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.HandlerExceptionResolver;

import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.service.docker.ImageManager;


/**
 * Controller to issue rudimentary image-related Docker commands.
 */

@Controller
public class ImageController implements HandlerExceptionResolver {

    protected static Logger logger = Logger.getLogger(ImageController.class);

    @Resource(name="imageManager")
    private ImageManager imageManager;
    
    /**
     * Accepts a GET request for a List of Docker images.
     *
     * The view is the dashboard.  The model contains the image List
     * which will be loaded and displayed in the view via jspf.  Model
     * also contains a Map container statuses corresponding to the images.
     * 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/image/list", method=RequestMethod.GET)
    public String getImageList(Model model) { 
        List<_Image> _images = imageManager.getImageList();           
        model.addAttribute("imageList", _images);   
        return "dashboard";
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
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter( writer );
        exception.printStackTrace( printWriter );
        printWriter.flush();

        String stackTrace = writer.toString();
        
        ModelAndView modelAndView = new ModelAndView();
        Map<String, Object> model = new HashMap<String, Object>();
        if (exception instanceof AccessDeniedException){ 
            message = exception.getMessage();
            modelAndView.setViewName("denied");
        } else  {
            message = "An error has occurred: " + exception.getClass().getName() + ": " + stackTrace;  
            modelAndView.setViewName("fatalError"); 
        }
        logger.error(message);       
        model.put("message", message);
        modelAndView.addAllObjects(model);
        return modelAndView;
    } 

}
