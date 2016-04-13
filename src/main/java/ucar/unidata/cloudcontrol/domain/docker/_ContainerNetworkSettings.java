package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing Docker container host configuration information.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.model.ContainerNetworkSettings object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.model.ContainerNetworkSettings attribute types have 
 * been converted to String types to handle: 1) checking for null values passed from the 
 * com.github.dockerjava.api.model.ContainerNetworkSettings object; and 2) custom formatting of data.
 */
public class _ContainerNetworkSettings implements Serializable {

    private String networks;
    
    public String getNetworks() {
        return networks;
    }

    public void setNetworks(Map<String, _ContainerNetwork> networks) {
        if (Objects.isNull(networks)) {
            this.networks = "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, _ContainerNetwork> entry : networks.entrySet()) {
                String key = entry.getKey();
				if (Objects.isNull(key)) {
				    sb.append("");
				} else {
			    	sb.append(key + ": <br>");
				}
                _ContainerNetwork value = entry.getValue();
                if (Objects.isNull(value)) {
                    sb.append("");
                } else {
                    sb.append("&nbsp; &nbsp; &nbsp; Aliases: " + value.getAliases() + "<br>");
                    sb.append("&nbsp; &nbsp; &nbsp; Network ID: " + value.getNetworkID() + "<br>");
                    sb.append("&nbsp; &nbsp; &nbsp; Endpoint ID: " + value.getEndpointId() + "<br>");
                    sb.append("&nbsp; &nbsp; &nbsp; Gateway: " + value.getGateway() + "<br>");
					sb.append("&nbsp; &nbsp; &nbsp; IP Address: " + value.getIpAddress() + "<br>");
					sb.append("&nbsp; &nbsp; &nbsp; IP Prefix Length: " + value.getIpPrefixLen() + "<br>");
					sb.append("&nbsp; &nbsp; &nbsp; IPv6 Gateway: " + value.getIpV6Gateway() + "<br>");
					sb.append("&nbsp; &nbsp; &nbsp; Global IPv6 Address: " + value.getGlobalIPv6Address() + "<br>");
					sb.append("&nbsp; &nbsp; &nbsp; Global IPv6 Prefix Length: " + value.getGlobalIPv6PrefixLen() + "<br>");
					sb.append("&nbsp; &nbsp; &nbsp; MAC Address: " + value.getMacAddress());
                }
                sb.append("<br>");
            }
            this.networks = sb.toString();
        }        
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  

}
