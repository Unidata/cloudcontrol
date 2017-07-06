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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.HandlerExceptionResolver;

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;
import edu.ucar.unidata.cloudcontrol.service.user.validators.CreateUserValidator;

/**
 * Controller to create a new User.
 */
@Controller
public class CreateUserController implements HandlerExceptionResolver {

    protected static Logger logger = Logger.getLogger(CreateUserController.class);

    @Resource(name="userManager")
    private UserManager userManager;
    
    @Autowired
    private CreateUserValidator createUserValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringtrimmer);
        binder.setValidator(createUserValidator);
    }

    /**
     * Accepts a GET request to create a new User object.
     *
     * The view is the dashboard.  The model contains a blank User and action
     * information which will be loaded and displayed in the view via jspf.
     *
     * Only Users with a role of 'ROLE_ADMIN' are allowed access
     * to this view. (See register method for public access.)
     * 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/user/create", method=RequestMethod.GET)
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("action", "createUser");
        model.addAttribute("user", user);
        return "dashboard";
    }

    /**
     * Accepts a GET request to create a new User object via registration.
     *
     * The view is the welcome.  The model contains a blank User and action
     * information which will be loaded and displayed in the view via jspf.
     *
     * Anyone can access this view.
     * 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value="/welcome/register", method=RequestMethod.GET)
    public String register(Model model) {
        User user = new User();
        model.addAttribute("action", "register");
        model.addAttribute("user", user);
        return "welcome";
    }

    /**
     * Accepts a POST request to create a new User object and persist it. 
     *
     * View is the dashboard.  The model contains:
     * 1) the newly created User object (if successful) displayed in the view via jspf;or
     * 2) the web form to create a new User if there are validation errors with the user input. 
     * 
     * @param user  The User to persist. 
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View. 
     * @throws RuntimeException  If unable to create new User.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/user/create", method=RequestMethod.POST)
    public ModelAndView createUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
           model.addAttribute("action", "createUser");
           return new ModelAndView("dashboard");
        } else {
            try {
                user = userManager.createUser(user);
                return new ModelAndView(new RedirectView("/dashboard/user/view/" + user.getUserName(), true));
            } catch (RecoverableDataAccessException e) {
                throw new RuntimeException("Unable to create new user: " + e);
            }
        }
    }

    /**
     * Accepts a POST request to create a new User object via registration and persist it. 
     *
     * View is the dashboard.  The model contains:
     * 1) the newly created User object (if successful) displayed in the view via jspf;or
     * 2) the web form to create a new User if there are validation errors with the user input. 
     * 
     * @param user  The User to persist. 
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View. 
     * @throws RuntimeException  If unable to complete registration process.
     */
    @RequestMapping(value="/welcome/register", method=RequestMethod.POST)
    public ModelAndView register(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
           model.addAttribute("action", "register");
           return new ModelAndView("welcome");
        } else {
            try {
                userManager.createUser(user);
                return new ModelAndView(new RedirectView("/login", true));
            }catch (RecoverableDataAccessException e) {
                throw new RuntimeException("Unable to complete registration process: " + e);
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
        }else  {
            message = "An error has occurred: " + exception.getClass().getName() + ": " + stackTrace;
            modelAndView.setViewName("fatalError");
        }
        logger.error(message);     
        model.put("message", message);
        modelAndView.addAllObjects(model);
        return modelAndView;
    }
}
