package edu.ucar.unidata.cloudcontrol.controller.docker;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

import edu.ucar.unidata.cloudcontrol.domain.docker.ClientConfig;
import edu.ucar.unidata.cloudcontrol.service.docker.ClientManager;
import edu.ucar.unidata.cloudcontrol.service.docker.validators.ClientConfigValidator;


/**
 * Controller to handle the Docker client configuration and commands.
 */

@Controller
public class ClientController implements HandlerExceptionResolver {

    protected static Logger logger = Logger.getLogger(ClientController.class);

    @Resource(name = "clientManager")
    private ClientManager clientManager;
    
    @Autowired
    private ClientConfigValidator clientConfigValidator;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringtrimmer);
        binder.setValidator(clientConfigValidator);  
    }   
    
    /**
     * Accepts a GET request to create a new ClientConfig object. 
     *
     * The view is the dashboard.  The model contains a blank ClientConfig and
     * action information which will be loaded and displayed in the view via jspf.
     * If we already have configuration data in the database, the model contains the 
     * requested ClientConfig displayed in the view via jspf.
     * 
     * @param authentication  The Authentication object to check roles with. 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String getDashboardPage(Authentication authentication, Model model) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
	        List<ClientConfig> clientConfigs = clientManager.getAllClientConfigs();
	        if (clientConfigs.size() > 0) {
	            try {
	                ClientConfig clientConfig = clientConfigs.get(0);        
	                model.addAttribute("clientConfig", clientConfig);      
	            } catch (RecoverableDataAccessException e) {
	                throw new RuntimeException("Unable to find clientConfig: " +  e);
	            }
	        } else {          
	            ClientConfig clientConfig = new ClientConfig();
	            model.addAttribute("action", "configureClient");  
	            model.addAttribute("clientConfig", clientConfig); 
	        }
        }
        return "dashboard";
    }
    
    /**
     * Accepts a GET request for a List of all ClientConfig objects.
     *
     * The view is the dashboard.  The model contains a List of ClientConfig
     * objects which will be loaded and displayed in the view via jspf.
     * The view can handle an empty list of ClientConfigs if no ClientConfig objects 
     * have been persisted in the database yet.
     *
     * Only users with the role of 'ROLE_ADMIN' can view the list of ClientConfigs.
     * 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/client", method=RequestMethod.GET)
    public String listClientConfigs(Model model) { 
        List<ClientConfig> clientConfigs = clientManager.getAllClientConfigs();
        model.addAttribute("action", "listClientConfigs"); 
        if (clientConfigs.size() > 0) {
            model.addAttribute("clientConfig", clientConfigs.get(0)); 
        }            
        return "dashboard";
    }
    
    /**
     * Accepts a GET request to retrieve a specific ClientConfig object.
     *
     * View is the dashboard.  The model contains the 
     * requested ClientConfig displayed in the view via jspf.
     *    
     * Only Users with the role of 'ROLE_ADMIN' can see Docker client configuration information.
     * 
     * @param id  The id as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     * @throws RuntimeException  If unable to find the logged-in ClientConfig's info.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/client/view/{id}", method=RequestMethod.GET)
    public String viewClientConfig(@PathVariable int id, Model model) { 
        try {
            ClientConfig clientConfig = clientManager.lookupById(id);           
            model.addAttribute("clientConfig", clientConfig);    
            model.addAttribute("action", "viewClientConfig");    
            return "dashboard";
        } catch (RecoverableDataAccessException e) {
            throw new RuntimeException("Unable to find clientConfig with id: " +  new Integer(id).toString());
        }
    }
    
    /**
     * Accepts a GET request to create a new ClientConfig object. 
     *
     * The view is the dashboard.  The model contains a blank ClientConfig and
     * action information which will be loaded and displayed in the view via jspf.
     * If we already have configuration data in the database, the model contains the 
     * requested ClientConfig displayed in the view via jspf.
     *
     * Only Users with a role of 'ROLE_ADMIN' are allowed access
     * to this view. 
     * 
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @RequestMapping(value = "/dashboard/docker/client/configure", method = RequestMethod.GET)
    public String configure(Model model) {
        List<ClientConfig> clientConfigs = clientManager.getAllClientConfigs();
        if (clientConfigs.size() > 0) {
            try {
                ClientConfig clientConfig = clientConfigs.get(0);        
                model.addAttribute("clientConfig", clientConfig);    
                model.addAttribute("action", "viewClientConfig");    
                return "dashboard";
            } catch (RecoverableDataAccessException e) {
                throw new RuntimeException("Unable to find clientConfig: " +  e);
            }
        } else {          
            ClientConfig clientConfig = new ClientConfig();
            model.addAttribute("action", "configureClient");  
            model.addAttribute("clientConfig", clientConfig); 
        }
        return "dashboard";
    }
    
    /**
     * Accepts a POST request to create a new ClientConfig object and persist it. 
     *
     * View is the dashboard.  The model contains:
     * 1) the newly created ClientConfig object (if successful) displayed in the view via jspf; or
     * 2) the web form to create a new ClientConfig if there are validation errors with the user input. 
     *
     * Only Users with the role of 'ROLE_ADMIN' can configure a Docker Client.
     * 
     * @param clientConfig  The ClientConfig to persist. 
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View. 
     * @throws RuntimeException  If unable to create new ClientConfig.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/client/configure", method=RequestMethod.POST)
    public ModelAndView configure(@Valid ClientConfig clientConfig, BindingResult result, Model model) { 
        if (result.hasErrors()) {
           model.addAttribute("action", "configureClient");  
           model.addAttribute("clientConfig", clientConfig);   
           return new ModelAndView("dashboard"); 
        } else {
            try {
                User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                clientConfig.setCreatedBy(authUser.getUsername());
                clientConfig.setLastUpdatedBy(authUser.getUsername());
                int id = clientManager.createClientConfig(clientConfig);  
                return new ModelAndView(new RedirectView("/dashboard/docker/client/view/" + new Integer(id).toString(), true)); 
            } catch (RecoverableDataAccessException e) {
                throw new RuntimeException("Unable to create new ClientConfig: " + e);
            }
        }         
    }
    
    /**
     * Accepts a GET request to edit an existing ClientConfig object. 
     * 
     * The view is the dashboard.  The model contains the ClientConfig object to edit
     * and the information which will be loaded and displayed in the view via jspf.
     *
     * Only Users with the role of 'ROLE_ADMIN' can edit Docker Client Configuration information.
     * 
     * @param id  The id as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/client/edit/{id}", method=RequestMethod.GET)
    public String editClientConfig(@PathVariable int id, Model model) {   
        ClientConfig clientConfig = clientManager.lookupById(id);           
        model.addAttribute("clientConfig", clientConfig);    
        model.addAttribute("action", "editClientConfig");
        return "dashboard";   
    }

    /**
     * Accepts a POST request to edit an existing ClientConfig object.
     *
     * View is the dashboard.  The model contains:
     * 1) the updated ClientConfig object (if successful) displayed in the view via jspf; or
     * 2) the web form to edit the ClientConfig if there are validation errors with the clientConfig input.
     *
     * Only Users with the role of 'ROLE_ADMIN' can edit Docker Client Configuration information.
     * 
     * @param id  The id as provided by @PathVariable. 
     * @param clientConfig  The ClientConfig to edit. 
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View.
     * @throws RuntimeException  If unable to update ClientConfig.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/client/edit/{id}", method=RequestMethod.POST)
    public ModelAndView editClientConfig(@PathVariable int id, @Valid ClientConfig clientConfig, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.info("an error has occurred");
            model.addAttribute("action", "editClientConfig");
            model.addAttribute("clientConfig", clientConfig);  
            return new ModelAndView("dashboard"); 
        } else {   
            try {
                User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                clientConfig.setLastUpdatedBy(authUser.getUsername());
                ClientConfig dbClientConfig = clientManager.lookupById(id);
                clientConfig.setCreatedBy(dbClientConfig.getCreatedBy());
                clientConfig.setDateCreated(dbClientConfig.getDateCreated());
                clientManager.updateClientConfig(clientConfig);
                return new ModelAndView(new RedirectView("/dashboard/docker/client/view/" + new Integer(id).toString(), true));  
            } catch (RecoverableDataAccessException e) {
                throw new RuntimeException("Unable to edit clientConfig: " +  e);
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
