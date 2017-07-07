package edu.ucar.unidata.cloudcontrol.service.docker;

import java.util.List;
import java.util.Map;

import edu.ucar.unidata.cloudcontrol.domain.docker._Info;
import edu.ucar.unidata.cloudcontrol.domain.docker._Version;


/**
 * Service for processing docker server and system-related api requests.
 */
public interface ServerManager {

    /**
     * Requests Docker server information.
     *
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Info object.
     */
    public _Info getInfo();

    /**
     * Requests the docker server version.
     *
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Version object.
     */
    public _Version getVersion();
}
