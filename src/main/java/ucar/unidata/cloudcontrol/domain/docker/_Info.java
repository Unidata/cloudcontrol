package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing the system-wide information of a Docker instance.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.model.Info 
 * object and the JSON result of a GET /info request to the Docker Remote API.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.model.Info attribute types have been
 * converted to String types to handle: 1) the @CheckForNull getter method annotation
 * in the com.github.dockerjava.api.model.Info object; and 2) custom formatting of data.
 */
public class _Info implements Serializable {

    private String architecture;
    private String containers;
    private String containersStopped;
    private String containersPaused;
    private String containersRunning;
    private String cpuCfsPeriod; 
    private String cpuCfsQuota;
    private String cpuShares;
    private String cpuSet;
    private String debug;
    private String discoveryBackend;
    private String dockerRootDir;
    private String driver;
    private String driverStatuses;
    private String systemStatus;
    private String plugins;
    private String executionDriver;
    private String loggingDriver;
    private String experimentalBuild;
    private String httpProxy;
    private String httpsProxy;
    private String id;
    private String ipv4Forwarding;
    private String bridgeNfIptables;
    private String bridgeNfIp6tables;
    private String images;
    private String indexServerAddress;
    private String initPath;
    private String initSha1;
    private String kernelVersion;
    private String labels;
    private String memoryLimit;
    private String memTotal;
    private String name;
    private String ncpu;
    private String nEventsListener;
    private String nfd;
    private String nGoroutines;
    private String noProxy;
    private String oomKillDisable;
    private String osType;
    private String oomScoreAdj;
    private String operatingSystem;
    private String registryConfig;
    private String sockets;
    private String swapLimit;
    private String systemTime;
    private String serverVersion;
    private String clusterStore;
    private String clusterAdvertise;
    
    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        if (Objects.isNull(architecture)) {
            this.architecture = "";
        } else {
            this.architecture = architecture;
        }        
    }

    public String getContainers() {
        return containers;
    }

    public void setContainers(Integer containers) {
        if (Objects.isNull(containers)) {
            this.containers = "";
        } else {
            this.containers = String.valueOf(containers);
        }   
    }

    public String getContainersStopped() {
        return containersStopped;
    }

    public void setContainersStopped(Integer containersStopped) {
        if (Objects.isNull(containersStopped)) {
            this.containersStopped = "";
        } else {
            this.containersStopped = String.valueOf(containersStopped);
        }         
    }

    public String getContainersPaused() {
        return containersPaused;
    }

    public void setContainersPaused(Integer containersPaused) {
        if (Objects.isNull(containersPaused)) {
            this.containersPaused = "";
        } else {
            this.containersPaused = String.valueOf(containersPaused);
        }        
    }

    public String getContainersRunning() {
        return containersRunning;
    }

    public void setContainersRunning(Integer containersRunning) {
        if (Objects.isNull(containersRunning)) {
            this.containersRunning = "";
        } else {
            this.containersRunning = String.valueOf(containersRunning);
        }          
    }

    public String getCpuCfsPeriod() {
        return cpuCfsPeriod;
    }

    public void setCpuCfsPeriod(Boolean cpuCfsPeriod) {
        if (Objects.isNull(cpuCfsPeriod)) {
            this.cpuCfsPeriod = "";
        } else {
            this.cpuCfsPeriod = String.valueOf(cpuCfsPeriod);
        }         
    }

    public String getCpuCfsQuota() {
        return cpuCfsQuota;
    }

    public void setCpuCfsQuota(Boolean cpuCfsQuota) {
        if (Objects.isNull(cpuCfsQuota)) {
            this.cpuCfsQuota = "";
        } else {
            this.cpuCfsQuota = String.valueOf(cpuCfsQuota);
        }
    }

    public String getCpuShares() {
        return cpuShares;
    }

    public void setCpuShares(Boolean cpuShares) {
        if (Objects.isNull(cpuShares)) {
            this.cpuShares = "";
        } else {
            this.cpuShares = String.valueOf(cpuShares);
        }
    }

    public String getCpuSet() {
        return cpuSet;
    }

    public void setCpuSet(Boolean cpuSet) {
        if (Objects.isNull(cpuSet)) {
            this.cpuSet = "";
        } else {
            this.cpuSet = String.valueOf(cpuSet);
        }
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        if (Objects.isNull(debug)) {
            this.debug = "";
        } else {
            this.debug = String.valueOf(debug);
        }
    }

    public String getDiscoveryBackend() {
        return discoveryBackend;
    }

    public void setDiscoveryBackend(String discoveryBackend) {
        if (Objects.isNull(discoveryBackend)) {
            this.discoveryBackend = "";
        } else {
            this.discoveryBackend = discoveryBackend;
        } 
    }

    public String getDockerRootDir() {
        return dockerRootDir;
    }

    public void setDockerRootDir(String dockerRootDir) {
        if (Objects.isNull(dockerRootDir)) {
            this.dockerRootDir = "";
        } else {
            this.dockerRootDir = dockerRootDir;
        } 
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        if (Objects.isNull(driver)) {
            this.driver = "";
        } else {
            this.driver = driver;
        } 
    }

    public String getDriverStatuses() {
        return driverStatuses;
    }

    public void setDriverStatuses(List<List<String>> driverStatuses) {
        if (Objects.isNull(driverStatuses)) {
            this.driverStatuses = "";
        } else {
            if (!driverStatuses.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (List<String> statuses : driverStatuses) {
                    if (Objects.isNull(statuses)) {
                        sb.append("");
                    } else {
                        for (Iterator<String> it = statuses.iterator(); it.hasNext(); ) {
                            String s = it.next();
                            if (Objects.isNull(s)) {
                                sb.append("");
                            } else {
                                  if (it.hasNext()) {
                                    sb.append(s + ": ");
                                } else {
                                    sb.append(s);                            
                                }
                            }
                         }
                    sb.append("<br>");
                    } 
                }
                this.driverStatuses = sb.toString();
            } else {
                this.driverStatuses = "";
            }
        }         
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(List<Object> systemStatus) {
        if (Objects.isNull(systemStatus)) {
            this.systemStatus = "";
        } else {
            if (!systemStatus.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Object status : systemStatus) {
                    if (Objects.isNull(status)) {
                        sb.append("");
                    } else {
                        sb.append(String.valueOf(status) + ", ");
                    }
                }
                this.systemStatus = sb.toString();
            } else {
                this.systemStatus = "";
            }
        } 
    }

    public String getPlugins() {
        return plugins;
    }

    public void setPlugins(Map<String, List<String>> plugins) {
        if (Objects.isNull(plugins)) {
            this.plugins = "";
        } else {
            if (!plugins.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, List<String>> entry : plugins.entrySet()) {
                    String key = entry.getKey();
                    if (Objects.isNull(key)) {
                        sb.append("");
                    } else {
                        sb.append(key + ": ");
                    }
                    List<String> value = entry.getValue();
                    if (Objects.isNull(value)) {
                        sb.append("");
                    } else {
                        for (Iterator<String> it = entry.getValue().iterator(); it.hasNext(); ) {
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
                    }
                    sb.append("<br>");
                }
                this.plugins = sb.toString();
            } else {
                this.plugins = "";
            }
        }        
    }

    public String getExecutionDriver() {
        return executionDriver;
    }

    public void setExecutionDriver(String executionDriver) {
        if (Objects.isNull(executionDriver)) {
            this.executionDriver = "";
        } else {
            this.executionDriver = executionDriver;
        } 
    }

    public String getLoggingDriver() {
        return loggingDriver;
    }

    public void setLoggingDriver(String loggingDriver) {
        if (Objects.isNull(loggingDriver)) {
            this.loggingDriver = "";
        } else {
            this.loggingDriver = loggingDriver;
        }
    }

    public String getExperimentalBuild() {
        return experimentalBuild;
    }
	
    public void setExperimentalBuild(Boolean experimentalBuild) {
        if (Objects.isNull(experimentalBuild)) {
            this.experimentalBuild = "";
        } else {
            this.experimentalBuild = String.valueOf(experimentalBuild);
        }
    }

    public String getHttpProxy() {
        return httpProxy;
    }

    public void setHttpProxy(String httpProxy) {
        if (Objects.isNull(httpProxy)) {
            this.httpProxy = "";
        } else {
            this.httpProxy = httpProxy;
        } 
    }

    public String getHttpsProxy() {
        return httpsProxy;
    }

    public void setHttpsProxy(String httpsProxy) {
        if (Objects.isNull(httpsProxy)) {
            this.httpsProxy = "";
        } else {
            this.httpsProxy = httpsProxy;
        } 
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (Objects.isNull(id)) {
            this.id = "";
        } else {
            this.id = id;
        } 
    }

    public String getIpv4Forwarding() {
        return ipv4Forwarding;
    }

    public void setIpv4Forwarding(Boolean ipv4Forwarding) {
        if (Objects.isNull(ipv4Forwarding)) {
            this.ipv4Forwarding = "";
        } else {
            this.ipv4Forwarding = String.valueOf(ipv4Forwarding);
        }
    }
	
    public String getBridgeNfIptables() {
        return bridgeNfIptables;
    }

    public void setBridgeNfIptables(Boolean bridgeNfIptables) {
        if (Objects.isNull(bridgeNfIptables)) {
            this.bridgeNfIptables = "";
        } else {
            this.bridgeNfIptables = String.valueOf(bridgeNfIptables);
        }
    }

    public String getBridgeNfIp6tables() {
        return bridgeNfIp6tables;
    }

    public void setBridgeNfIp6tables(Boolean bridgeNfIp6tables) {
        if (Objects.isNull(bridgeNfIp6tables)) {
            this.bridgeNfIp6tables = "";
        } else {
            this.bridgeNfIp6tables = String.valueOf(bridgeNfIp6tables);
        }
    }

    public String getImages() {
        return images;
    }

    public void setImages(Integer images) {
        if (Objects.isNull(images)) {
            this.images = "";
        } else {
            this.images = String.valueOf(images);
        } 
    }

    public String getIndexServerAddress() {
        return indexServerAddress;
    }

    public void setIndexServerAddress(String indexServerAddress) {
        if (Objects.isNull(indexServerAddress)) {
            this.indexServerAddress = "";
        } else {
            this.indexServerAddress = indexServerAddress;
        } 
    }

    public String getInitPath() {
        return initPath;
    }

    public void setInitPath(String initPath) {
        if (Objects.isNull(initPath)) {
            this.initPath = "";
        } else {
            this.initPath = initPath;
        } 
    }

    public String getInitSha1() {
        return initSha1;
    }

    public void setInitSha1(String initSha1) {
        if (Objects.isNull(initSha1)) {
            this.initSha1 = "";
        } else {
            this.initSha1 = initSha1;
        } 
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        if (Objects.isNull(kernelVersion)) {
            this.kernelVersion = "";
        } else {
            this.kernelVersion = kernelVersion;
        } 
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        if (Objects.isNull(labels)) {
            this.labels = "";
        } else {
            if (labels.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (Iterator<String> it = Arrays.asList(labels).iterator(); it.hasNext(); ) {
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
                this.labels = sb.toString();
            } else {
                this.labels = "";    
            }
        }         
    }

    public String getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Boolean memoryLimit) {
        if (Objects.isNull(memoryLimit)) {
            this.memoryLimit = "";
        } else {
            this.memoryLimit = String.valueOf(memoryLimit);
        }
    }

    public String getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(Long memTotal) {
        if (Objects.isNull(memTotal)) {
            this.memTotal = "";
        } else {
            this.memTotal = String.valueOf(memTotal);
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
	
    public String getNcpu() {
        return ncpu;
    }

    public void setNcpu(Integer ncpu) {
        if (Objects.isNull(ncpu)) {
            this.ncpu = "";
        } else {
            this.ncpu = String.valueOf(ncpu);
        } 
    }

    public String getNEventsListener() {
        return nEventsListener;
    }

    public void setNEventsListener(Integer nEventsListener) {
        if (Objects.isNull(nEventsListener)) {
            this.nEventsListener = "";
        } else {
            this.nEventsListener = String.valueOf(nEventsListener);
        } 
    }

    public String getNfd() {
        return nfd;
    }

    public void setNfd(Integer nfd) {
        if (Objects.isNull(nfd)) {
            this.nfd = "";
        } else {
            this.nfd = String.valueOf(nfd);
        } 
    }

    public String getNGoroutines() {
        return nGoroutines;
    }

    public void setNGoroutines(Integer nGoroutines) {
        if (Objects.isNull(nGoroutines)) {
            this.nGoroutines = "";
        } else {
            this.nGoroutines = String.valueOf(nGoroutines);
        } 
    }

    public String getNoProxy() {
        return noProxy;
    }

    public void setNoProxy(String noProxy) {
        if (Objects.isNull(noProxy)) {
            this.noProxy = "";
        } else {
            this.noProxy = noProxy;
        } 
    }

    public String getOomKillDisable() {
        return oomKillDisable;
    }

    public void setOomKillDisable(Boolean oomKillDisable) {
        if (Objects.isNull(oomKillDisable)) {
            this.oomKillDisable = "";
        } else {
            this.oomKillDisable = String.valueOf(oomKillDisable);
        }
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        if (Objects.isNull(osType)) {
            this.osType = "";
        } else {
            this.osType = osType;
        } 
    }

    public String getOomScoreAdj() {
        return oomScoreAdj;
    }

    public void setOomScoreAdj(Integer oomScoreAdj) {
        if (Objects.isNull(oomScoreAdj)) {
            this.oomScoreAdj = "";
        } else {
            this.oomScoreAdj = String.valueOf(oomScoreAdj);
        } 
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        if (Objects.isNull(operatingSystem)) {
            this.operatingSystem = "";
        } else {
            this.operatingSystem = operatingSystem;
        } 
    }

    public String getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(_InfoRegistryConfig registryConfig) {
        if (Objects.isNull(registryConfig)) {
            this.registryConfig = "";
        } else {
            StringBuilder sb = new StringBuilder();
			sb.append("IndexConfigs: " + registryConfig.getIndexConfigs());
            sb.append("InsecureRegistryCIDRs: " + registryConfig.getInsecureRegistryCIDRs() + "<br>");
            sb.append("Mirrors: " + registryConfig.getMirrors() + "<br>");
            this.registryConfig = sb.toString();
        } 
    }

    public String getSockets() {
        return sockets;
    }

    public void setSockets(String[] sockets) {
        if (Objects.isNull(sockets)) {
            this.sockets = "";
        } else {
            if (sockets.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (Iterator<String> it = Arrays.asList(sockets).iterator(); it.hasNext(); ) {
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
                this.sockets = sb.toString();
            } else {
                this.sockets = "";    
            }
        }         
    }

    public String getSwapLimit() {
        return swapLimit;
    }

    public void setSwapLimit(Boolean swapLimit) {
        if (Objects.isNull(swapLimit)) {
            this.swapLimit = "";
        } else {
            this.swapLimit = String.valueOf(swapLimit);
        }
    }

    public String getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(String systemTime) {
        if (Objects.isNull(systemTime)) {
            this.systemTime = "";
        } else {
            this.systemTime = systemTime;
        } 
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        if (Objects.isNull(serverVersion)) {
            this.serverVersion = "";
        } else {
            this.serverVersion = serverVersion;
        } 
    }

    public String getClusterStore() {
        return clusterStore;
    }

    public void setClusterStore(String clusterStore) {
        if (Objects.isNull(clusterStore)) {
            this.clusterStore = "";
        } else {
            this.clusterStore = clusterStore;
        } 
    }

    public String getClusterAdvertise() {
        return clusterAdvertise;
    }

    public void setClusterAdvertise(String clusterAdvertise) {
        if (Objects.isNull(clusterAdvertise)) {
            this.clusterAdvertise = "";
        } else {
            this.clusterAdvertise = clusterAdvertise;
        } 
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  

}
