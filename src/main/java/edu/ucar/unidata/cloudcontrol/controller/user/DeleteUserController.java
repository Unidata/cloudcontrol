package edu.ucar.unidata.cloudcontrol.controller.user;

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller to delete a User.
 */
@Controller
public class DeleteUserController {

    @Resource(name="userManager")
    private UserManager userManager;

    /**
     * Accepts a GET request to delete a specific User object.
     *
     * View is the dashboard.  The model contains the
     * requested User displayed in the view via jspf.
     *
     * Only Users with a role of 'ROLE_ADMIN' are allowed to delete the User account.
     *
     * @param userName  The 'userName' as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/user/delete/{userName}", method=RequestMethod.GET)
    public String deleteUser(@PathVariable String userName, Model model) {
        User user = userManager.lookupUser(userName);
        model.addAttribute("user", user);
        model.addAttribute("action", "deleteUser");
        return "dashboard";
    }

    /**
     * Accepts a POST request to delete an existing User object.
     *
     * View is the dashboard.  The model contains a List of remaining
     * User objects (if successful) displayed in the view via jspf.
     *
     * Only Users with a role of 'ROLE_ADMIN' are allowed to delete users.
     *
     * @param user  The User to delete.
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/user/delete", method=RequestMethod.POST)
    public ModelAndView deleteUser(User user, BindingResult result, Model model) {
        userManager.deleteUser(user.getUserId());
        List<User> users = userManager.getUserList();
        model.addAttribute("action", "listUsers");
        model.addAttribute("users", users);
        return new ModelAndView(new RedirectView("/dashboard/user", true));
    }
}
