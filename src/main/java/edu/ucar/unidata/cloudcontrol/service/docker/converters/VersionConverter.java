package edu.ucar.unidata.cloudcontrol.service.docker.converters;

import com.github.dockerjava.api.model.Version;

import edu.ucar.unidata.cloudcontrol.domain.docker._Version;

import java.util.function.Function;

/**
 * Service for converting com.github.dockerjava.* Version-related objects to a corresponding edu.ucar.unidata.* objects.
 */
public class VersionConverter {

    /**
     * Converts a com.github.dockerjava.api.model.Version object to
     * a edu.ucar.unidata.cloudcontrol.domain.docker._Version object.
     *
     * @param info  The com.github.dockerjava.api.model.Version object to convert.
     * @return  A edu.ucar.unidata.cloudcontrol.domain.docker._Version object.
     */
    public _Version convertVersion(Version version) {
        Function<Version, _Version> mapVersionTo_Version = new Function<Version, _Version>() {
            public _Version apply(Version v) {
                _Version _version = new _Version();
                _version.setApiVersion(v.getApiVersion());
                _version.setArch(v.getArch());
                _version.setGitCommit(v.getGitCommit());
                _version.setGoVersion(v.getGoVersion());
                _version.setKernelVersion(v.getKernelVersion());
                _version.setOperatingSystem(v.getOperatingSystem());
                _version.setVersion(v.getVersion());
                _version.setBuildTime(v.getBuildTime());
                _version.setExperimental(v.getExperimental());
                return _version;
            }
        };
        _Version _version = mapVersionTo_Version.apply(version);
        return _version;
    }
}
