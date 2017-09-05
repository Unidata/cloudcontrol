package edu.ucar.unidata.cloudcontrol.controller.user;

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;
import edu.ucar.unidata.cloudcontrol.service.user.validators.EditUserValidator;

import java.util.Collection;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

/**
 * Controller to edit/modify a User.
 */
@Controller
public class EditUserController {

    protected static Logger logger = Logger.getLogger(EditUserController.class);

    @Resource(name="userManager")
    private UserManager userManager;

    @Autowired
    private EditUserValidator editUserValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringtrimmer);
        binder.setValidator(editUserValidator);
    }

    /**
     * Accepts a GET request to edit an existing User object.
     *
     * The view is the dashboard.  The model contains the User object to edit
     * and the information which will be loaded and displayed in the view via jspf.
     *
     * Only the User/owner and Users with a role of 'ROLE_ADMIN' are
     * allowed to edit the User account.
     *
     * @param userName  The 'userName' as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userName == authentication.name")
    @RequestMapping(value="/dashboard/user/edit/{userName}", method=RequestMethod.GET)
    public String editUser(@PathVariable String userName, Model model) {
        logger.debug("Get edit user form.");
        User user = userManager.lookupUser(userName);
        model.addAttribute("action", "editUser");
        model.addAttribute("user", user);
        return "dashboard";
    }

    /**
     * Accepts a POST request to edit an existing User object.
     *
     * View is the dashboard.  The model contains:
     * 1) the updated User object (if successful) displayed in the view via jspf; or
     * 2) the web form to edit the User if there are validation errors with the user input.
     *
     * Only the User/owner and Users with a role of 'ROLE_ADMIN' are
     * allowed to edit the User account.
     *
     * @param user  The User to edit.
     * @param authentication  The Authentication object to check roles with.
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userName == authentication.name")
    @RequestMapping(value="/dashboard/user/edit/{userName}", method=RequestMethod.POST)
    public ModelAndView editUser(@PathVariable String userName, @Valid User user, BindingResult result, Authentication authentication, Model model) {
        logger.debug("Processing submitted edit user form data.");
        if (result.hasErrors()) {
            logger.debug("Validation errors detected in edit user form data. Returning user to form view.");
            model.addAttribute("action", "editUser");
            model.addAttribute("user", user);
            return new ModelAndView("dashboard");
        } else {
            logger.debug("No validation errors detected in edit user form data. Proceeding with edit user process.");
            User u = userManager.lookupUser(user.getUserName());
            String name = user.getFullName();
            u.setFullName(name);
            u.setEmailAddress(user.getEmailAddress());
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                u.setAccessLevel(user.getAccessLevel());
                u.setAccountStatus(user.getAccountStatus());
            }
            u = userManager.updateUser(u);
            // update the session
            Authentication auth = new UsernamePasswordAuthenticationToken(u, u.getPassword(), u.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new ModelAndView(new RedirectView("/dashboard/user/view/" + userName, true));
        }
    }
}
