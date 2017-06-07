package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing a bind mounted volume in a Docker container.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.model.Volume object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.model.Volume attribute types have been
 * converted to String types to handle: 1) checking for null values passed from the 
 * com.github.dockerjava.api.model.Volume object; and 2) custom formatting of data.
 */
public class _Volume implements Serializable {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (Objects.isNull(path)) {
            this.path = "";
        } else {
            this.path = path;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  
}
