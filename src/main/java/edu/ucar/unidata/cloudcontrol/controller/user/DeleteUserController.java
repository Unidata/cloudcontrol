package edu.ucar.unidata.cloudcontrol.controller.user;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;

import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.HandlerExceptionResolver;

import edu.ucar.unidata.cloudcontrol.domain.User;
import edu.ucar.unidata.cloudcontrol.service.user.UserManager;

/**
 * Controller to delete a User.
 */
@Controller
public class DeleteUserController implements HandlerExceptionResolver {

    protected static Logger logger = Logger.getLogger(DeleteUserController.class);

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
     * @throws RuntimeException  If unable to find the logged-in User's info.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/user/delete/{userName}", method=RequestMethod.GET)
    public String deleteUser(@PathVariable String userName, Model model) {
        try {
            User user = userManager.lookupUser(userName);
            model.addAttribute("user", user);
            model.addAttribute("action", "deleteUser");
            return "dashboard";
        } catch (RecoverableDataAccessException e) {
            throw new RuntimeException("Unable to find user with user name: " +  userName);
        }
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
     * @throws RuntimeException  If unable to deleet User.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/user/delete", method=RequestMethod.POST)
    public ModelAndView deleteUser(User user, BindingResult result, Model model) {
        try {
            userManager.deleteUser(user.getUserId());
            List<User> users = userManager.getUserList();
            model.addAttribute("action", "listUsers");
            model.addAttribute("users", users);
            return new ModelAndView(new RedirectView("/dashboard/user", true));
        } catch (RecoverableDataAccessException e) {
            throw new RuntimeException("Unable to delete user: " +  user.getUserName());
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
