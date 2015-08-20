package edu.ucar.unidata.cloudcontrol.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Controller to issue rudimentary docker commands.
 */

@Controller
public class DockerController implements HandlerExceptionResolver {

    protected static Logger logger = Logger.getLogger(DockerController.class);

    @RequestMapping(value="/docker", method=RequestMethod.GET)
    public String listDockerVersion(Model model) throws InterruptedException, IOException { 
        ProcessBuilder pb = new ProcessBuilder("docker", "version");
        Process process = pb.start();
        int errCode = process.waitFor();
        logger.error("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "<br>");
            }
        } finally {
            br.close();
        }
        model.addAttribute("output", sb.toString());   
        return "docker";
    }


    @RequestMapping(value="/docker/images", method=RequestMethod.GET)
    public String listDockerImages(Model model) throws InterruptedException, IOException { 
        ProcessBuilder pb = new ProcessBuilder("docker", "images");
        Process process = pb.start();
        int errCode = process.waitFor();
        logger.error("Echo command executed, any errors? " + (errCode == 0 ? "No" : "Yes"));
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "<br>");
            }
        } finally {
            br.close();
        }
        model.addAttribute("output", sb.toString());   
        return "docker";
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
