package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing the registry configuration information of a Docker instance.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.model.InfoRegistryConfig 
 * object and the JSON result of a GET /info request to the Docker Remote API.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.model.InfoRegistryConfig attribute types have 
 * been converted to String types to handle: 1) the @CheckForNull getter method annotation in the 
 * com.github.dockerjava.api.model.InfoRegsitryConfig object; and 2) custom formatting of data.
 */
public class _InfoRegistryConfig implements Serializable {

    private String indexConfigs;           // Changed from Map<String, IndexConfig>
    private String insecureRegistryCIDRs;  // Changed from List<String>
    private String mirrors;                // Changed from Object
    
    public String getIndexConfigs() {
        return indexConfigs;
    }

    public void setIndexConfigs(Map<String, _IndexConfig> indexConfigs) {
        if (Objects.isNull(indexConfigs)) {
            this.indexConfigs = "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, _IndexConfig> entry : indexConfigs.entrySet()) {
                String key = entry.getKey();
                sb.append("<br> &nbsp; " + key + ": <br>");
                _IndexConfig value = entry.getValue();
                if (Objects.isNull(value)) {
                    sb.append("");
                } else {
                    sb.append("&nbsp; &nbsp; &nbsp;  mirrors: " + value.getMirrors() + "<br>");
                    sb.append("&nbsp; &nbsp; &nbsp; name: " + value.getName() + "<br>");
                    sb.append("&nbsp; &nbsp; &nbsp; official: " + value.getOfficial() + "<br>");
                    sb.append("&nbsp; &nbsp; &nbsp; secure: " + value.getSecure());
                }
                sb.append("<br>");
            }
            this.indexConfigs = sb.toString();
        }        
    }

    public String getInsecureRegistryCIDRs() {
        return insecureRegistryCIDRs;
    }

    public void setInsecureRegistryCIDRs(List<String> insecureRegistryCIDRs) {
        if (Objects.isNull(insecureRegistryCIDRs)) {
            this.insecureRegistryCIDRs = "";
        } else {
            if (!insecureRegistryCIDRs.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String s : insecureRegistryCIDRs) {
                    if (Objects.isNull(s)) {
                        sb.append("");
                    } else {
                        sb.append(s + ", ");
                    }
                }
                this.insecureRegistryCIDRs = sb.toString();
            } else {
                this.insecureRegistryCIDRs = "";
            }
        }   
    }

    public String getMirrors() {
        return mirrors;
    }

    public void setMirrors(Object mirrors) {
        if (Objects.isNull(mirrors)) {
            this.mirrors = "";
        } else {
            this.mirrors = String.valueOf(mirrors);
        }         
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  
    
    public static final class _IndexConfig {
        private String mirrors;   // Changed from List<String>
        private String name;
        private String official;  // Changed from Boolean
        private String secure;    // Changed from Boolean
        
        public String getMirrors() {
            return mirrors;
        }

        public void setMirrors(List<String> mirrors) {
            if (Objects.isNull(mirrors)) {
                this.mirrors = "";
            } else {
                if (!mirrors.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (String s : mirrors) {
                        if (Objects.isNull(s)) {
                            sb.append("");
                        } else {
                            sb.append(s + ", ");
                        }
                    }
                    this.mirrors = sb.toString();
                } else {
                    this.mirrors = "";
                }
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            if (Objects.isNull(name)) {
                this.name = "";
            } else {
                this.name = name;
            }
        }

        public String getOfficial() {
            return official;
        }

        public void setOfficial(Boolean official) {
            if (Objects.isNull(official)) {
                this.official = "";
            } else {
                this.official = String.valueOf(official);
            }
        }

        public String getSecure() {
            return secure;
        }

        public void setSecure(Boolean secure) {
            if (Objects.isNull(secure)) {
                this.secure = "";
            } else {
                this.secure = String.valueOf(secure);
            }
        }
        
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        } 
    }
}
