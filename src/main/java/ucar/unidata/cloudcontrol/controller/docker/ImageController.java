package edu.ucar.unidata.cloudcontrol.controller.docker;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import java.io.StringWriter;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.view.RedirectView;

import edu.ucar.unidata.cloudcontrol.domain.docker.DisplayImage;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.domain.docker._Info;
import edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ImageManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ServerManager;


/**
 * Controller to issue rudimentary image-related Docker commands.
 */

@Controller
public class ImageController implements HandlerExceptionResolver {

    protected static Logger logger = Logger.getLogger(ImageController.class);

    @Resource(name="imageManager")
    private ImageManager imageManager;
    
    @Resource(name="serverManager")
    private ServerManager serverManager;
    
    @Resource(name="containerManager")
    private ContainerManager containerManager;
    
    /**
     * Accepts a GET request for a List of Docker images.
     *
     * The view is the dashboard.  The model contains the image List
     * which will be loaded and displayed in the view via jspf.  
     * 
     * @param authentication  The Authentication object to check roles with. 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/dashboard/docker/image/list", method=RequestMethod.GET)
    public String getImageList(Authentication authentication, Model model) { 
        List<_Image> _images; 
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            _images = imageManager.getImageList(); 
        } else {
            _images = imageManager.filterByDisplayImage(); 
        }
        if (!Objects.isNull(_images)) {
             model.addAttribute("imageList", _images);   
        } else {
            // see if server info image number jives 
            _Info _info = serverManager.getInfo(); 
            if (!Objects.isNull(_info)) {
                if (Integer.parseInt(_info.getImages()) == 0) {
                    _images = new ArrayList<_Image>();
                    model.addAttribute("imageList", _images);   
                }
            }
        }
        model.addAttribute("action", "listImages");               
        return "dashboard";
    }
    
    /**
     * Accepts a GET request start a Docker image.
     *
     * The view is the dashboard.  The model contains the image List
     * which will be loaded and displayed in the view via jspf.  
     * 
     * @param id  The Image ID.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/dashboard/docker/image/{id}/start", method=RequestMethod.GET)
    public String startImage(@PathVariable String id, Model model) { 
        if (!containerManager.startContainer(id)) {
            model.addAttribute("error", "Unable to start Image."); 
        }
        return "redirect:/dashboard/docker/image/list";  
    }
    
    /**
     * Accepts a GET request stop a Docker image.
     *
     * The view is the dashboard.  The model contains the image List
     * which will be loaded and displayed in the view via jspf.  
     * 
     * @param id  The Image ID.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/dashboard/docker/image/{id}/stop", method=RequestMethod.GET)
    public String stopImage(@PathVariable String id, Model model) {   
        if (!containerManager.stopContainer(id)) {
            model.addAttribute("error", "Unable to start Image."); 
        }
        return "redirect:/dashboard/docker/image/list";  
    }
    
    /**
     * Accepts a GET request to Inspect a Docker image.
     *
     * View is is the requested _InspectImageResponse (details corresponding 
     * to an Image). The model contains the image inspection information
     * which will be loaded and displayed in the view via jspf.  
     * 
     * @param id  The Image ID.
     * @param authentication  The Authentication object to check roles with. 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/dashboard/docker/image/{id}/inspect", method=RequestMethod.GET)
    public String inspectImage(@PathVariable String id, Authentication authentication, Model model) { 
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (!authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {    
			logger.info("not admin");      
            if (!imageManager.isDisplayImage(id)) {    
				logger.info("not display image");     
                model.addAttribute("error", "Permissions Error!  You are not allowed to access this Image.  Please contact the site administrator if you have any questions.");
            }
	    }
        _InspectImageResponse _inspectImageResponse = imageManager.inspectImage(id);   
        if (_inspectImageResponse != null) {
            model.addAttribute("inspectImageResponse", _inspectImageResponse);     
        } else {
            throw new RuntimeException("An error occurred whilst processing Inspect Image request. InspectImageResponse in null.");  
        }       
        _Image _image = imageManager.getImage(id); 
        if (_image != null) {
            model.addAttribute("image", _image);  
        } else {
            throw new RuntimeException("An error occurred whilst processing Inspect Image request.  Image is null.");  
        }
        return "docker/image/inspectImage";
    }
    
    
    /**
     * Accepts a GET request to add an Image to the user display.  
     * By default, images are not shown in user display until added by admin. 
     *
     * The view is the dashboard.  The model contains the image List
     * which will be loaded and displayed in the view via jspf.  
     *
     * Only Users with the role of 'ROLE_ADMIN' can add an Image to the user display.
     * 
     * @param id  The Image ID. 
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View. 
     * @throws RuntimeException  If unable to create a DisplayImage.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/image/userDisplay/add/{id}", method=RequestMethod.GET)
    public ModelAndView addDisplayImage(@PathVariable String id, Model model) { 
        try {
            DisplayImage displayImage = new DisplayImage();
            displayImage.setImageId(id);
            imageManager.createDisplayImage(displayImage);  
            
            List<_Image> _images = imageManager.getImageList(); 
            if (!Objects.isNull(_images)) {
                 model.addAttribute("imageList", _images);  
                 model.addAttribute("action", "listImages");       
            } else {
                throw new RuntimeException("An error occurred whilst processing addDisplayImage request.  Image List is null.");
            }
            return new ModelAndView(new RedirectView("/dashboard/docker/image/list", true)); 
        } catch (RecoverableDataAccessException e) {
            throw new RuntimeException("Unable to create new DisplayImage: " + e);
        }        
    }
    
    /**
     * Accepts a GET request to remove an Image to the user display.  
     * By default, images are not shown in user display until added by admin. 
     *
     * The view is the dashboard.  The model contains the image List
     * which will be loaded and displayed in the view via jspf.  
     *
     * Only Users with the role of 'ROLE_ADMIN' can remove an Image to the user display.
     * 
     * @param id  The Image ID. 
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View. 
     * @throws RuntimeException  If unable to create a DisplayImage.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/image/userDisplay/remove/{id}", method=RequestMethod.GET)
    public ModelAndView removeDisplayImage(@PathVariable String id, Model model) { 
        try {
            imageManager.deleteDisplayImage(id);  
            
            List<_Image> _images = imageManager.getImageList(); 
            if (!Objects.isNull(_images)) {
                 model.addAttribute("imageList", _images);  
                 model.addAttribute("action", "listImages");       
            } else {
                throw new RuntimeException("An error occurred whilst processing removeDisplayImage request.  Image List is null.");
            }
            return new ModelAndView(new RedirectView("/dashboard/docker/image/list", true)); 
        } catch (RecoverableDataAccessException e) {
            throw new RuntimeException("Unable to delete DisplayImage: " + e);
        }        
    }
    
    /**
     * This method gracefully handles any uncaught exception
     * that are fatal in nature and unresolvable by the user.
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
