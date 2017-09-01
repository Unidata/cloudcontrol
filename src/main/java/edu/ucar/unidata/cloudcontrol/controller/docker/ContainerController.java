package edu.ucar.unidata.cloudcontrol.controller.docker;

import edu.ucar.unidata.cloudcontrol.domain.docker._Container;
import edu.ucar.unidata.cloudcontrol.service.docker.ContainerManager;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to issue rudimentary Container-related Docker commands.
 */
@Controller
public class ContainerController {

    @Resource(name="containerManager")
    private ContainerManager containerManager;

    /**
     * Accepts a GET request for a List of Docker containers.
     *
     * The view is the dashboard.  The model contains the container List
     * which will be loaded and displayed in the view via jspf.
     *
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/container/list", method=RequestMethod.GET)
    public String getContainerList(Model model) {
        List<_Container> _containers = containerManager.getContainerList();
        if (!Objects.isNull(_containers)) {
            model.addAttribute("containerList", _containers);
        }
        return "dashboard";
    }
}
