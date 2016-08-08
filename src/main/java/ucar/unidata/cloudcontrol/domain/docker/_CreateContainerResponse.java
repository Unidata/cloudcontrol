package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing Docker create container response information.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.command.CreateContainerResponse object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.command.CreateContainerResponse attribute types have
 * been converted to String types to handle: 1) the @CheckForNull getter method annotation in the 
 * com.github.dockerjava.api.command.CreateContainerResponse object; and 2) custom formatting of data.
 */

public class _CreateContainerResponse implements Serializable {

    private String id;
	private String warnings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (Objects.isNull(id)) {
            this.id = "";
        } else {
            this.id = id.substring(0,12);
        }
    }
	
    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String[] warnings) {
        if (Objects.isNull(warnings)) {
            this.warnings = "";
        } else {
            if (warnings.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (Iterator<String> it = Arrays.asList(warnings).iterator(); it.hasNext(); ) {
                    String s = it.next();
                    if (Objects.isNull(s)) {
                        sb.append("");
                    } else {
                          if (it.hasNext()) {
                            sb.append(s + ", ");
                        } else {
                            sb.append(s);                            
                        }
                    }
                }
                this.warnings = sb.toString();
            } else {
                this.warnings = "";    
            }
        }         
    }
	
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  
}
