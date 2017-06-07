package edu.ucar.unidata.cloudcontrol.controller.user;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;
import edu.ucar.unidata.cloudcontrol.service.user.validators.PasswordValidator;

/**
 * Controller to reset User passwords. 
 */

@Controller
public class ResetPasswordController implements HandlerExceptionResolver {

    protected static Logger logger = Logger.getLogger(ResetPasswordController.class);

    @Resource(name="userManager")
    private UserManager userManager;
    
    @Autowired
    private PasswordValidator passwordValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringtrimmer);
        binder.setValidator(passwordValidator);  
    }   


    /**
     * Accepts a GET request to edit an existing User's password. 
     * 
     * The view is the dashboard.  The model contains the User object with the password
     * to edit and the information which will be loaded and displayed in the view via jspf.
     *
     * Only the User/owner and Users with a role of 'ROLE_ADMIN' are 
     * allowed to edit the User's password.
     * 
     * @param userName  The 'userName' as provided by @PathVariable. 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userName == authentication.name")
    @RequestMapping(value="/dashboard/user/password/{userName}", method=RequestMethod.GET)
    public String editUserPassword(@PathVariable String userName, Model model) {   
        User user = userManager.lookupUser(userName);  
        model.addAttribute("action", "resetPassword");  
        model.addAttribute("user", user);   
        return "dashboard";
    }
    
    
    /**
     * Accepts a POST request to edit an existing User's password. 
     *
     * View is the dashboard.  The model contains:
     * 1) the User object with the changed password (if successful) displayed in the view via jspf; or
     * 2) the web form to edit the User's password if there are validation errors with the user input.
     *
     * Only the User/owner and Users with a role of 'ROLE_ADMIN' are 
     * allowed to edit the User account.
     * 
     * @param userName  The 'userName' as provided by @PathVariable. 
     * @param user  The User to edit. 
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View.
     * @throws RuntimeException  If unable to update User's password.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userName == authentication.name")
    @RequestMapping(value="/dashboard/user/password/{userName}", method=RequestMethod.POST)
    public ModelAndView editUserPassword(@PathVariable String userName, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "resetPassword");  
            model.addAttribute("user", user);  
            return new ModelAndView("dashboard"); 
        } else {   
            try {
                User u = userManager.lookupUser(userName);
                user.setUserName(userName);
                userManager.updatePassword(user);
                return new ModelAndView(new RedirectView("/dashboard/user/view/" + userName, true));   
            } catch (RecoverableDataAccessException e) {
                throw new RuntimeException("Unable to modify password for user: " +  userName);
            }
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
