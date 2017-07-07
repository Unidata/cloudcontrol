package edu.ucar.unidata.cloudcontrol.controller.docker;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.view.RedirectView;

import edu.ucar.unidata.cloudcontrol.domain.docker.ImageMapping;
import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.domain.docker._Image;
import edu.ucar.unidata.cloudcontrol.domain.docker._Info;
import edu.ucar.unidata.cloudcontrol.domain.docker._InspectImageResponse;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerMappingManager;
import edu.ucar.unidata.cloudcontrol.service.docker.ImageMappingManager;
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

    @Resource(name = "imageMappingManager")
    private ImageMappingManager imageMappingManager;

    @Resource(name = "containerMappingManager")
    private ContainerMappingManager containerMappingManager;

    /**
     * Accepts a GET request for a List of Docker images.
     *
     * The view is the dashboard.  The model contains the image List
     * which will be loaded and displayed in the view via jspf.
     *
     * @param authentication  The Authentication object to check user roles.
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
            _images = imageMappingManager.filterByImageMapping(imageManager.getImageList());
        }
        if (_images != null) {
             model.addAttribute("imageList", _images);
        } else {
            // see if server info image number jives
            _Info _info = serverManager.getInfo();
            if (_info != null) {
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
     * Accepts an AJAX GET request start a Docker image.
     *
     * @param imageId  The image ID.
     * @param authentication  The Authentication object to check user roles.
     * @return  The ID of the image container that has been started (if successful), or an error message.
     */
    @RequestMapping(value="/dashboard/docker/image/{imageId}/start", method=RequestMethod.GET)
    @ResponseBody
    public String startImage(@PathVariable String imageId, Authentication authentication) {
        // check to see if user is allow to access/manipulate this image
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (!authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            if (!imageMappingManager.isVisibleToUser(imageId)) {
                logger.info("User " + authentication.getName() + " does not have permission to start image " + imageId);
                return "Error: you are not allowed to start this image.  Please contact the site administrator if you have any questions.";
            }
        }
        // start an image container
        String containerId = containerManager.startContainer(imageId, authentication.getName());
        if (containerId == null) {
            logger.error("Unable to start container for image " + imageId);
            return "Error: unable to start image.  Please contact the site administrator.";
        } else {
            logger.info("User " + authentication.getName() + " has successfully started container " + containerId + " in image " + imageId);
            return containerId;
        }
    }

    /**
     * Accepts an AJAX GET request stop a Docker image.
     *
     * @param containerId  The ID of the image container to stop.
     * @param authentication  The Authentication object to check user roles.
     * @return  The image ID of the container that was stopped (if successful), or an error message.
     */
    @ResponseBody
    @RequestMapping(value="/dashboard/docker/image/{containerId}/stop", method=RequestMethod.GET)
    public String stopImage(@PathVariable String containerId, Authentication authentication) {
        // check to see if user is allow to access/manipulate this image
        _Container _container = containerManager.getContainer(containerId);
        if (_container == null) {
            logger.error("Unable to find container " + containerId + " to stop.");
            return "Error: unable to find image container.  Please contact the site administrator.";
        }
        String imageId = _container.getImageId();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (!authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            if (!imageMappingManager.isVisibleToUser(imageId)) {
                logger.info("User " + authentication.getName() + " does not have permission to stop container " + containerId + " in image " + imageId);
                return "Error: you are not allowed to start/stop this image.  Please contact the site administrator if you have any questions.";
            }
        }
        // see if user making the request is the same as the one in the mapping
        String userName = containerMappingManager.lookupContainerMappingbyContainer(_container).getUserName();
        if (authentication.getName().equals(userName) || authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            if (!containerManager.stopContainer(containerId)) {
                logger.error("Unable to stop image container " + containerId);
                return "Error: unable to stop image container.  Please contact the site administrator.";
            } else {
                logger.info("User " + authentication.getName() + " has successfully stopped container " + containerId + " in image " + imageId);
                return _container.getImageId();
            }
        } else {
            logger.info("User" + authentication.getName() + " does not have permission to stop container " + containerId + " in image " + imageId);
            return "Error: you are not allowed to start/stop this image container.  Please contact the site administrator.";
        }
    }

    /**
     * Accepts an AJAX GET request for a Map of Docker image status information.
     *
     * @param authentication  The Authentication object to check user roles.
     * @return  The Map of Docker image statuses.
     */
    @ResponseBody
    @RequestMapping(value="/dashboard/docker/image/list/status", method=RequestMethod.GET)
    public Map<String,String> getImageList(Authentication authentication) {
        List<_Image> _images;
        Map<String,String> statusMap;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            _images = imageManager.getImageList();
        } else {
            _images = imageMappingManager.filterByImageMapping(imageManager.getImageList());
        }
        if (_images != null) {
            statusMap = imageManager.getImageStatusMap(_images);
        } else {
            statusMap = new HashMap<String, String>();
        }
        return statusMap;
    }

    /**
     * Accepts an AJAX GET request to Inspect a Docker image.
     *
     * View is is the requested _InspectImageResponse (details corresponding
     * to an Image). The model contains the image inspection information
     * which will be loaded and displayed in the view via jspf.
     *
     * @param id  The Image ID.
     * @param authentication  The Authentication object to check user roles.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/dashboard/docker/image/{id}/inspect", method=RequestMethod.GET)
    public String inspectImage(@PathVariable String id, Authentication authentication, Model model) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (!authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            if (!imageMappingManager.isVisibleToUser(id)) {
                model.addAttribute("error", "Permissions Error!  You are not allowed to access this image.  Please contact the site administrator if you have any questions.");
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
     * Only Users with the role of 'ROLE_ADMIN' can add an Image to the user display.
     *
     * @param id  The Image ID.
     * @return  Whether the Image is visible to the User or not (if successful), or an error message.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value="/dashboard/docker/image/{id}/show", method=RequestMethod.GET)
    public String addImageMapping(@PathVariable String id) {
        ImageMapping imageMapping = new ImageMapping();
        imageMapping.setImageId(id);
        imageMappingManager.createImageMapping(imageMapping);
        if (imageMappingManager.isVisibleToUser(id)) {
            return "Visible to Users";
        } else {
            return "Error: Unable to add image to user view.";
        }
    }

    /**
     * Accepts a GET request to remove an Image to the user display.
     * By default, images are not shown in user display until added by admin.
     *
     * Only Users with the role of 'ROLE_ADMIN' can remove an Image to the user display.
     *
     * @param id  The Image ID.
     * @return  Whether the Image is visible to the User or not (if successful), or an error message.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value="/dashboard/docker/image/{id}/hide", method=RequestMethod.GET)
    public String removeImageMapping(@PathVariable String id) {
        imageMappingManager.deleteImageMapping(id);
        if (imageMappingManager.isVisibleToUser(id)) {
            return "Error: Unable to hide image from user view.";
        } else {
            return "Hidden from Users";
        }
    }

    /**
     * Accepts a GET request to remove an Image from the Docker server.
     *
     * Only Users with the role of 'ROLE_ADMIN' can remove an Image.
     *
     * @param id  The Image ID.
     * @param model  The Model used by the View.
     * @return  Redirected view path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value="/dashboard/docker/image/{id}/remove", method=RequestMethod.GET)
    public String removeImage(@PathVariable String id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName(); //get logged in username
        if (!imageManager.removeImage(id, userName)) {
            return "Error: Unable to remove Image.";
        } else {
            return "success";
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
