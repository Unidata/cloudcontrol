package edu.ucar.unidata.cloudcontrol.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;

/**
 * Controller to handle misc requests.
 */

@Controller
public class MiscController {
    
    /**
     * Accepts a GET request for the welcome page. This page is visible
     * before the user is logged in. View is the welcome page. 
     *
     * @param model The Model used by the View.
     * @return The view path for the ViewResolver.
     */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String getWelcomePage(Model model) {
        return "welcome";
    }
	
    /**
     * Accepts a GET request for the gettingStarted page.  This page
     * is visible before the user is logged in.  View is the misc page. 
     *
     * @param model The Model used by the View.
     * @return The view path for the ViewResolver.
     */
    @RequestMapping(value = "/gettingStarted", method = RequestMethod.GET)
    public String gettingStarted(Model model) {
		model.addAttribute("action", "gettingStarted");   
        return "misc";
    }
	
    /**
     * Accepts a GET request for the about page.  This page is 
     * visible before the user is logged in.  View is the misc page. 
     *
     * @param model The Model used by the View.
     * @return The view path for the ViewResolver.
     */
    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(Model model) {
		model.addAttribute("action", "about");   
        return "misc";
    }
    
}
