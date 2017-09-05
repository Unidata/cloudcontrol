package edu.ucar.unidata.cloudcontrol.controller.user;

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to view a User or group of Users. 
 */
@Controller
public class ViewUserController {

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
        logger.debug("Get list all users view.");
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
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userName == authentication.name")
    @RequestMapping(value="/dashboard/user/view/{userName}", method=RequestMethod.GET)
    public String viewUser(@PathVariable String userName, Model model) {
        logger.debug("Get specified user information view.");
        User user = userManager.lookupUser(userName);
        model.addAttribute("user", user);
        model.addAttribute("action", "viewUser");
        return "dashboard";
    }
}
