package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing Docker container network information.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.model.ContainerNetwork object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.model.ContainerNetwork attribute types have 
 * been converted to String types to handle: 1) the @CheckForNull getter annotation in the 
 * com.github.dockerjava.api.model.ContainerNetwork object; and 2) custom formatting of data.
 */
public class _ContainerNetwork implements Serializable {

    private String aliases;
    private String networkID;
    private String endpointId;
    private String gateway;
    private String ipAddress;
    private String ipPrefixLen;
    private String ipV6Gateway;
    private String globalIPv6Address;
    private String globalIPv6PrefixLen;
    private String macAddress;
    
    public String getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        if (Objects.isNull(aliases)) {
            this.aliases = "";
        } else {
            if (!aliases.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String s : aliases) {
                    if (Objects.isNull(s)) {
                        sb.append("");
                    } else {
                        sb.append(s + ", ");
                    }
                }
                this.aliases = sb.toString();
            } else {
                this.aliases = "";
            }
        }
    }

    public String getNetworkID() {
        return networkID;
    }

    public void setNetworkID(String networkID) {
        if (Objects.isNull(networkID)) {
            this.networkID = "";
        } else {
            this.networkID = networkID;
        }
    }

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        if (Objects.isNull(endpointId)) {
            this.endpointId = "";
        } else {
            this.endpointId = endpointId;
        }
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        if (Objects.isNull(gateway)) {
            this.gateway = "";
        } else {
            this.gateway = gateway;
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        if (Objects.isNull(ipAddress)) {
            this.ipAddress = "";
        } else {
            this.ipAddress = ipAddress;
        }
    }

    public String getIpPrefixLen() {
        return ipPrefixLen;
    }

    public void setIpPrefixLen(Integer ipPrefixLen) {
        if (Objects.isNull(ipPrefixLen)) {
            this.ipPrefixLen = "";
        } else {
            this.ipPrefixLen = String.valueOf(ipPrefixLen);
        }
    }

    public String getIpV6Gateway() {
        return ipV6Gateway;
    }

    public void setIpV6Gateway(String ipV6Gateway) {
        if (Objects.isNull(ipV6Gateway)) {
            this.ipV6Gateway = "";
        } else {
            this.ipV6Gateway = ipV6Gateway;
        }
    }

    public String getGlobalIPv6Address() {
        return globalIPv6Address;
    }

    public void setGlobalIPv6Address(String globalIPv6Address) {
        if (Objects.isNull(globalIPv6Address)) {
            this.globalIPv6Address = "";
        } else {
            this.globalIPv6Address = globalIPv6Address;
        }
    }

    public String getGlobalIPv6PrefixLen() {
        return globalIPv6PrefixLen;
    }

    public void setGlobalIPv6PrefixLen(Integer globalIPv6PrefixLen) {
        if (Objects.isNull(globalIPv6PrefixLen)) {
            this.globalIPv6PrefixLen = "";
        } else {
            this.globalIPv6PrefixLen = String.valueOf(globalIPv6PrefixLen);
        }
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        if (Objects.isNull(macAddress)) {
            this.macAddress = "";
        } else {
            this.macAddress = macAddress;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  

}
