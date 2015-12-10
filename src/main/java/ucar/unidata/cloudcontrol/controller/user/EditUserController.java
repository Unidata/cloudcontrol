package edu.ucar.unidata.cloudcontrol.controller.user;

import java.util.Collection;
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
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.service.UserManager;
import edu.ucar.unidata.cloudcontrol.service.user.validators.EditUserValidator;

/**
 * Controller to edit/modify a User. 
 */

@Controller
public class EditUserController implements HandlerExceptionResolver {

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
     * View is a web form to edit an existing User.
     *
     * Only the User/owner and Users with a role of 'ROLE_ADMIN' are 
     * allowed to edit the User account.
     * 
     * @param userName  The 'userName' as provided by @PathVariable. 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userName == authentication.name")
    @RequestMapping(value="/user/edit/{userName}", method=RequestMethod.GET)
    public String editUser(@PathVariable String userName, Model model) {   
        User user = userManager.lookupUser(userName);  
        model.addAttribute("user", user);
        return "user/editUser";   
    }

    /**
     * Accepts a POST request to edit an existing User object.
     *
     * View is:
     * 1) the updated User object (if successful); or 
     * 2) the web form if there are validation errors with the user input.
     *
     * Only the User/owner and Users with a role of 'ROLE_ADMIN' are 
     * allowed to edit the User account.
     * 
     * @param user  The User to edit. 
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View.
     * @throws RuntimeException  If unable to update User.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or #user.userName == authentication.name")
    @RequestMapping(value="/user/edit", method=RequestMethod.POST)
    public ModelAndView editUser(@Valid User user, BindingResult result, Model model) {
		/*
		Map<String,Object>map = model.asMap();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
    logger.info(entry.getKey() + "=" + entry.getValue().toString());
}
			*/
        if (result.hasErrors()) {
            return new ModelAndView("user/editUser"); 
        } else {   
            try {
                userManager.updateUser(user);  
                return new ModelAndView(new RedirectView("/user/view/" + user.getUserName(), true));   
            } catch (RecoverableDataAccessException e) {
                throw new RuntimeException("Unable to edit user: " +  user.getUserName());
            }
        }    
    }

    /**
     * This method gracefully handles any uncaught exception that are fatal 
     * in nature and unresolvable by the user.
     * 
     * @param request   The current HttpServletRequest request.
     * @param response  The current HttpServletRequest response.
     * @param handler  The executed handler, or null if none chosen at the time of the exception.  
     * @param exception  The  exception that got thrown during handler execution.
     * @return  The error page containing the appropriate message to the user. 
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
