package edu.ucar.unidata.cloudcontrol.controller.docker;

import edu.ucar.unidata.cloudcontrol.domain.docker._Info;
import edu.ucar.unidata.cloudcontrol.domain.docker._Version;
import edu.ucar.unidata.cloudcontrol.service.docker.ServerManager;

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller to issue rudimentary server/system-related Docker commands.
 */
@Controller
public class ServerController {

    protected static Logger logger = Logger.getLogger(ServerController.class);

    @Resource(name="serverManager")
    private ServerManager serverManager;

    /**
     * Accepts a GET request for Docker server information.
     *
     * The view is the dashboard.  The model contains the server information
     * which will be loaded and displayed in the view via jspf.
     *
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/server/info", method=RequestMethod.GET)
    public String getInfo(Model model) {
        logger.debug("Get get docker server information view.");
        _Info _info = serverManager.getInfo();
        if (!Objects.isNull(_info)) {
            model.addAttribute("serverInfo", _info);
        }
        model.addAttribute("action", "serverInfo");
        return "dashboard";
    }

    /**
     * Accepts a GET request for the Docker server version.
     *
     * The view is the dashboard.  The model contains the server version
     * information which will be loaded and displayed in the view via jspf.
     *
     * @param model  The Model used by the View.
     * @return  The path for the ViewResolver.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value="/dashboard/docker/server/version", method=RequestMethod.GET)
    public String getVersion(Model model) {
        logger.debug("Get get docker server version view.");
        _Version _version = serverManager.getVersion();
        if (!Objects.isNull(_version)) {
            model.addAttribute("serverVersion", _version);
        }
        model.addAttribute("action", "serverVersion");
        return "dashboard";
    }
}
