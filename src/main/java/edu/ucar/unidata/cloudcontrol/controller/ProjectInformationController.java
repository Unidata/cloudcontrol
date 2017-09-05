package edu.ucar.unidata.cloudcontrol.controller;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

/**
 * Controller to handle projectInformation requests.
 */

@Controller
public class ProjectInformationController {

    protected static Logger logger = Logger.getLogger(ProjectInformationController.class);

    /**
     * Accepts a GET request for the welcome page. This page is visible
     * before the user is logged in. View is the welcome page. 
     *
     * @param model The Model used by the View.
     * @return The view path for the ViewResolver.
     */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String getWelcomePage(Model model) {
        logger.debug("Get welcome view.");
        return "welcome";
    }

    /**
     * Accepts a GET request for the gettingStarted page.  This page
     * is visible before the user is logged in.  View is the projectInformation page. 
     *
     * @param model The Model used by the View.
     * @return The view path for the ViewResolver.
     */
    @RequestMapping(value = "/gettingStarted", method = RequestMethod.GET)
    public String gettingStarted(Model model) {
        model.addAttribute("action", "gettingStarted");
        logger.debug("Get getting started view.");
        return "projectInformation";
    }

    /**
     * Accepts a GET request for the about page.  This page is 
     * visible before the user is logged in.  View is the projectInformation page. 
     *
     * @param model The Model used by the View.
     * @return The view path for the ViewResolver.
     */
    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(Model model) {
        logger.debug("Get about view.");
        model.addAttribute("action", "about");
        return "projectInformation";
    }
}
