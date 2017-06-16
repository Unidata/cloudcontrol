package edu.ucar.unidata.cloudcontrol.controller.user;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.HandlerExceptionResolver;

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;

/**
 * Controller to view a User or group of Users. 
 */
@Controller
public class ViewUserController implements HandlerExceptionResolver {

    protected static Logger logger = Logger.getLogger(ViewUserController.class);

    @Resource(name="userManager")
    private UserManager userManager;

    /**
     * Accepts a GET request for a List of all User objects.
     *
     * The view is the dashboard.  The model contains a List of User
     * objects which will be loaded and displayed in the view via jspf.
     * The view can handle an empty list of Users if no User objects
     * have been persisted in the database yet.
     *
     * Only Users with the role of 'ROLE_ADMIN' can view the list of Users.
     *
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/user", method=RequestMethod.GET)
    public String listUsers(Model model) {
        List<User> users = userManager.getUserList();
        model.addAttribute("action", "listUsers");
        model.addAttribute("users", users);
        return "dashboard";
    }

    /**
     * Accepts a GET request to retrieve a specific User object.
     *
     * View is the dashboard.  The model contains the 
     * requested User displayed in the view via jspf.
     *
     * Only the User/owner and Users with a role of 'ROLE_ADMIN' are 
     * allowed to view the User account.
     *
     * @param userName  The 'userName' as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     * @throws RuntimeException  If unable to find the logged-in User's info.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userName == authentication.name")
    @RequestMapping(value="/dashboard/user/view/{userName}", method=RequestMethod.GET)
    public String viewUser(@PathVariable String userName, Model model) {
        try {
            User user = userManager.lookupUser(userName);
            model.addAttribute("user", user);
            model.addAttribute("action", "viewUser");
            return "dashboard";
        } catch (RecoverableDataAccessException e) {
            throw new RuntimeException("Unable to find user with user name: " +  userName);
        }
    }

    /**
     * This method gracefully handles any uncaught exception
     * that are fatal in nature and unresolvable by the user.
     *
     * @param request   The current HttpServletRequest request.
     * @param response  The current HttpServletRequest response.
     * @param handler  The executed handler, or null if none chosen at the time of the exception
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
        } else {
            message = "An error has occurred: " + exception.getClass().getName() + ": " + stackTrace;
            modelAndView.setViewName("fatalError"); 
        }
        logger.error(message);
        model.put("message", message);
        modelAndView.addAllObjects(model);
        return modelAndView;
    }

}
