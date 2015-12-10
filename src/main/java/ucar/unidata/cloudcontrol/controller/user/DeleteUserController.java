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
//import edu.ucar.unidata.cloudcontrol.service.UserValidator;

/**
 * Controller to delete a User. 
 */

@Controller
public class DeleteUserController implements HandlerExceptionResolver {

    protected static Logger logger = Logger.getLogger(DeleteUserController.class);

    @Resource(name="userManager")
    private UserManager userManager;
    
//    @Autowired
//    private UserValidator userValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringtrimmer);
//        binder.setValidator(userValidator);  
    }   


    /**
     * Accepts a POST request to delete an existing User object. 
     *
     * View is either a list of all remaining User objects, or the View of the User
     * we are trying to delete if we are unable to find the User in the database.
     *
     * TODO: handle images owned by user.
     * Only Users with a role of 'ROLE_ADMIN' are allowed to delete users.
     * 
     * @param user  The User to delete. 
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View.
     * @throws RuntimeException  If unable to deleet User.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/user/delete", method=RequestMethod.POST)
    public ModelAndView deleteUser(User user, BindingResult result, Model model) {   
        try {
            userManager.deleteUser(user.getUserId());
            List<User> users = userManager.getUserList();           
            model.addAttribute("users", users);           
            return new ModelAndView(new RedirectView("/user", true));
        } catch (RecoverableDataAccessException e) {
            model.addAttribute("error", e.getMessage());       
            return new ModelAndView(new RedirectView("/user" + user.getUserName(), true)); 
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
