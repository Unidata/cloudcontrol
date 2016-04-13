package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing a Docker container.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.model.Container object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.model.Container attribute types have 
 * been converted to String types to handle: 1) the @CheckForNull getter annotation in the 
 * com.github.dockerjava.api.model.Container object; and 2) custom formatting of data.
 */
public class _Container implements Serializable {

    private String command;
    private String created;
    private String id;
    private String image;
    private String imageId;
    private String names;
    private String ports;
    private String labels;
    private String status;
    private String sizeRw;
    private String sizeRootFs;
    private String hostConfig;
    private String networkSettings;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        if (Objects.isNull(command)) {
            this.command = "";
        } else {
            this.command = command;
        }        
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        if (Objects.isNull(created)) {
            this.created = "";
        } else {
            Date d = new Date();
            d.setTime(created.longValue() * 1000);
            SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
            this.created = format.format(d);
        }        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (Objects.isNull(id)) {
            this.id = "";
        } else {
            this.id = id.substring(0,11);
        }    
    }
    
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        if (Objects.isNull(image)) {
            this.image = "";
        } else {
            this.image = image;
        }   
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        if (Objects.isNull(imageId)) {
            this.imageId = "";
        } else {
            this.imageId = imageId.substring(0,11);
        }        
    }

    public String getNames() {
        return names;
    }

    public void setNames(String[] names) {
        if (Objects.isNull(names)) {
            this.names = "";
        } else {
            if (names.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (Iterator<String> it = Arrays.asList(names).iterator(); it.hasNext(); ) {
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
                this.names = sb.toString();
            } else {
                this.names = "";    
            }
        }         
    }
    
    public String getPorts() {
        return ports;
    }    
    
    public void setPorts(_ContainerPort[] ports) {
        if (Objects.isNull(ports)) {
            this.ports = "";
        } else {
            if (ports.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (Iterator<_ContainerPort> it = Arrays.asList(ports).iterator(); it.hasNext(); ) {
                    _ContainerPort p = it.next();
                    if (Objects.isNull(p)) {
                        sb.append("");
                    } else {
                        sb.append("IP: " + p.getIp() + "<br>");
                        sb.append("Private Port: " + p.getPrivatePort() + "<br>");
                        sb.append("Public Port: " + p.getPublicPort() + "<br>");
                        sb.append("Type: " + p.getType() + "<br><br>");                         
                    }
                }
                this.ports = sb.toString();
            } else {
                this.ports = "";    
            }
        }         
    }
    
    public String getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        if (Objects.isNull(labels)) {
            this.labels = "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : labels.entrySet()) {
                String key = entry.getKey();
                if (Objects.isNull(key)) {
                    sb.append("");
                } else {
                    sb.append(key + ": ");
                }
                String value = entry.getValue();
                if (Objects.isNull(value)) {
                    sb.append("");
                } else {
                    sb.append(value);
                }
                sb.append("<br>");
            }
            this.labels = sb.toString();
        }        
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (Objects.isNull(status)) {
            this.status = "";
        } else {
            this.status = status;
        }        
    }

    public String getSizeRw() {
        return sizeRw;
    }

    public void setSizeRw(Long sizeRw) {
        if (Objects.isNull(sizeRw)) {
            this.sizeRw = "";
        } else {
            if (sizeRw > 1000000) {
                this.sizeRw = String.valueOf(sizeRw.longValue()/1000000) + " MB";
            } else if (sizeRw > 1000) {
                this.sizeRw = String.valueOf(sizeRw.longValue()/1000) + " KB";
            } else {
                this.sizeRw = sizeRw.toString() + " B";
            }
        }   
    }

    public String getSizeRootFs() {
        return sizeRootFs;
    }

    public void setSizeRootFs(Long sizeRootFs) {
        if (Objects.isNull(sizeRootFs)) {
            this.sizeRootFs = "";
        } else {
            if (sizeRootFs > 1000000) {
                this.sizeRootFs = String.valueOf(sizeRootFs.longValue()/1000000) + " MB";
            } else if (sizeRootFs > 1000) {
                this.sizeRootFs = String.valueOf(sizeRootFs.longValue()/1000) + " KB";
            } else {
                this.sizeRootFs = sizeRootFs.toString() + " B";
            }
        }   
    }
	
    public String getHostConfig() {
        return hostConfig;
    }

    public void setHostConfig(_ContainerHostConfig hostConfig) {
        if (Objects.isNull(hostConfig)) {
            this.hostConfig = "";
        } else {
            this.hostConfig = "Network Mode: " + hostConfig.getNetworkMode();
        }        
    }
    public String getNetworkSettings() {
        return networkSettings;
    }

    public void setNetworkSettings(_ContainerNetworkSettings networkSettings) {
        if (Objects.isNull(networkSettings)) {
            this.networkSettings = "";
        } else {
            this.networkSettings = "Container Network Settings: " + networkSettings.getNetworks();
        }        
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  

}
