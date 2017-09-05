package edu.ucar.unidata.cloudcontrol.controller.docker;

import edu.ucar.unidata.cloudcontrol.domain.docker.ClientConfig;
import edu.ucar.unidata.cloudcontrol.service.docker.ClientManager;
import edu.ucar.unidata.cloudcontrol.service.docker.validators.ClientConfigValidator;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
 * Controller to handle the Docker client configuration and commands.
 */
@Controller
public class ClientController {

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
     * The view is the dashboard.
     *
     * Only authenticated users are allowed access to this view.
     *
     * If the user has role of 'ROLE_ADMIN' and there are no configs in the database,
     * the model contains an empty ClientConfig object and a configureClient action
     * which will loaded the configureClient.jspf into the dashboard. Otherwise, if 
     * configuration data is already in the database, the model contains the requested 
     * ClientConfig object and no action is specifies, hence status.jspf is loaded.
     *
     * If the user does not have the role of 'ROLE_ADMIN', the model contains no action
     * or ClientConfig object and status.jspf is loaded into the dashboard view.
     *
     * @param authentication  The Authentication object to check roles with.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String getDashboardPage(Authentication authentication, Model model) {
        logger.debug("Get dashboard view.");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            List<ClientConfig> clientConfigs = clientManager.getAllClientConfigs();
            if (clientConfigs.size() > 0) {
                // the following for now until we implement connections to multiple docker instances
                ClientConfig clientConfig = clientConfigs.get(0);
                model.addAttribute("clientConfig", clientConfig);
            } else {
                ClientConfig clientConfig = new ClientConfig();
                model.addAttribute("action", "configureClient");
                model.addAttribute("clientConfig", clientConfig);
            }
        }
        return "dashboard";
    }

    /**
     * Accepts a GET request to create a new ClientConfig object.
     *
     * The view is the dashboard.  If there are no configs in the database, the
     * model contains an empty ClientConfig object and a configureClient action
     * which will loaded the configureClient.jspf into the dashboard. Otherwise,
     * if configuration data is already in the database, the model contains the
     * requested ClientConfig object and the viewClientConfig action which loads
     * the viewClientConfig.jspf into the dashbaord view.
     *
     * Only users with a role of 'ROLE_ADMIN' are allowed access to this view.
     *
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/dashboard/docker/client/configure", method = RequestMethod.GET)
    public String configure(Model model) {
        logger.debug("Get docker client configuration form.");
        List<ClientConfig> clientConfigs = clientManager.getAllClientConfigs();
        if (clientConfigs.size() > 0) {
            // the following for now until we implement connections to multiple docker instances
            ClientConfig clientConfig = clientConfigs.get(0);
            model.addAttribute("clientConfig", clientConfig);
            model.addAttribute("action", "viewClientConfig");
        } else {
            ClientConfig clientConfig = new ClientConfig();
            model.addAttribute("action", "configureClient");
            model.addAttribute("clientConfig", clientConfig);
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
        logger.debug("Get list all client configurations view.");
        List<ClientConfig> clientConfigs = clientManager.getAllClientConfigs();
        model.addAttribute("action", "listClientConfigs");
        // the following for now until we implement connections to multiple docker instances
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
     * Only users with the role of 'ROLE_ADMIN' can see Docker client configuration information.
     *
     * @param id  The id as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/client/view/{id}", method=RequestMethod.GET)
    public String viewClientConfig(@PathVariable int id, Model model) {
        logger.debug("Get specified client configuration view.");
        ClientConfig clientConfig = clientManager.lookupById(id);
        model.addAttribute("clientConfig", clientConfig);
        model.addAttribute("action", "viewClientConfig");
        return "dashboard";
    }

    /**
     * Accepts a POST request to create a new ClientConfig object and persist it.
     *
     * View is the dashboard.  The model contains:
     * 1) the newly created ClientConfig object (if successful) displayed in the view via jspf; or
     * 2) the web form to create a new ClientConfig if there are validation errors with the user input.
     *
     * Only users with the role of 'ROLE_ADMIN' can configure a Docker Client.
     *
     * @param clientConfig  The ClientConfig to persist.
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/client/configure", method=RequestMethod.POST)
    public ModelAndView createClientConfig(@Valid ClientConfig clientConfig, BindingResult result, Model model) {
        logger.debug("Processing submitted client configuration form data.");
        if (result.hasErrors()) {
            logger.debug("Validation errors detected in client configuration form data. Returning user to form view.");
            model.addAttribute("action", "configureClient");
            model.addAttribute("clientConfig", clientConfig);
            return new ModelAndView("dashboard");
        } else {
            logger.debug("No validation errors detected in client configuration form data. Proceeding with client configuration.");
            UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            clientConfig.setCreatedBy(authUser.getUsername());
            clientConfig.setLastUpdatedBy(authUser.getUsername());
            int id = clientManager.createClientConfig(clientConfig);
            return new ModelAndView(new RedirectView("/dashboard/docker/client/view/" + new Integer(id).toString(), true));
        }
    }

    /**
     * Accepts a GET request to edit an existing ClientConfig object.
     *
     * The view is the dashboard.  The model contains the ClientConfig object to edit
     * and the information which will be loaded and displayed in the view via jspf.
     *
     * Only users with the role of 'ROLE_ADMIN' can edit Docker Client Configuration information.
     *
     * @param id  The id as provided by @PathVariable.
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/client/edit/{id}", method=RequestMethod.GET)
    public String editClientConfig(@PathVariable int id, Model model) {
        logger.debug("Get edit client configuration form.");
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
     * Only users with the role of 'ROLE_ADMIN' can edit Docker Client Configuration information.
     *
     * @param id  The id as provided by @PathVariable.
     * @param clientConfig  The ClientConfig to edit.
     * @param result  The BindingResult for error handling.
     * @param model  The Model used by the View.
     * @return  The redirect to the needed View.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/client/edit/{id}", method=RequestMethod.POST)
    public ModelAndView editClientConfig(@PathVariable int id, @Valid ClientConfig clientConfig, BindingResult result, Model model) {
        logger.debug("Processing submitted edit client configuration form data.");
        if (result.hasErrors()) {
            logger.debug("Validation errors detected in edit client configuration form data. Returning user to form view.");
            model.addAttribute("action", "editClientConfig");
            model.addAttribute("clientConfig", clientConfig);
            return new ModelAndView("dashboard");
        } else {
            logger.debug("No validation errors detected in edit client configuration form data. Proceeding with edit client configuration process.");
            UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            clientConfig.setLastUpdatedBy(authUser.getUsername());
            ClientConfig dbClientConfig = clientManager.lookupById(id);
            clientConfig.setCreatedBy(dbClientConfig.getCreatedBy());
            clientConfig.setDateCreated(dbClientConfig.getDateCreated());
            clientManager.updateClientConfig(clientConfig);
            return new ModelAndView(new RedirectView("/dashboard/docker/client/view/" + new Integer(id).toString(), true));
        }
    }
}
