package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object representing Docker container inspection response information.
 *
 * Data in this object corresponds to the com.github.dockerjava.api.command.InspectContainerResponse object.
 *
 * Note: This object is passed directly to the view with no intermediary manipulation.
 * Therefore, the com.github.dockerjava.api.command.InspectContainerResponse attribute types have
 * been converted to String types to handle: 1) the @CheckForNull getter method annotation in the 
 * com.github.dockerjava.api.command.InspectContainerResponse object; and 2) custom formatting of data.
 */

public class _InspectContainerResponse implements Serializable {

    private String args;
  //  private ContainerConfig config;
    private String created;
    private String driver;
    private String execDriver;
 //   private HostConfig hostConfig;
    private String hostnamePath;
    private String hostsPath;
    private String id;
    private String sizeRootFs;
    private String imageId;
    private String mountLabel;
    private String name;
    private String restartCount;
//    private NetworkSettings networkSettings;
    private String path;
    private String processLabel;
    private String resolvConfPath;
    private String execIds;
//    private _ContainerState state;
//    private VolumeBinds volumes;
//    private VolumesRW volumesRW;
    private String mounts;

    public String getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        if (Objects.isNull(args)) {
            this.args = "";
        } else {
            if (args.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (Iterator<String> it = Arrays.asList(args).iterator(); it.hasNext(); ) {
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
                this.args = sb.toString();
            } else {
                this.args = "";    
            }
        }         
    }
    /*
    public String getConfig() {
        return config;
    }

    public void setConfig(ContainerConfig config) {
        if (Objects.isNull(config)) {
            this.config = "";
        } else {
            this.config = config;
        }
    }
        */

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        if (Objects.isNull(created)) {
            this.created = "";
        } else {
            this.created = created;
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

    public String getExecDriver() {
        return execDriver;
    }

    public void setExecDriver(String execDriver) {
        if (Objects.isNull(execDriver)) {
            this.execDriver = "";
        } else {
            this.execDriver = execDriver;
        }
    }
    /*
    public String getHostConfig() {
        return hostConfig;
    }

    public void setHostConfig(HostConfig hostConfig) {
        if (Objects.isNull(hostConfig)) {
            this.hostConfig = "";
        } else {
            this.hostConfig = hostConfig;
        }
    }
*/
    public String getHostnamePath() {
        return hostnamePath;
    }

    public void setHostnamePath(String hostnamePath) {
        if (Objects.isNull(hostnamePath)) {
            this.hostnamePath = "";
        } else {
            this.hostnamePath = hostnamePath;
        }
    }

    public String getHostsPath() {
        return hostsPath;
    }

    public void setHostsPath(String hostsPath) {
        if (Objects.isNull(hostsPath)) {
            this.hostsPath = "";
        } else {
            this.hostsPath = hostsPath;
        }
    }

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
    
    public String getSizeRootFs() {
        return sizeRootFs;
    }

    public void setSizeRootFs(Integer sizeRootFs) {
        if (Objects.isNull(sizeRootFs)) {
            this.sizeRootFs = "";
        } else {
            this.sizeRootFs = String.valueOf(sizeRootFs);
        }
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        if (Objects.isNull(imageId)) {
            this.imageId = "";
        } else {
            this.imageId = imageId;
        }
    }

    public String getMountLabel() {
        return mountLabel;
    }

    public void setMountLabel(String mountLabel) {
        if (Objects.isNull(mountLabel)) {
            this.mountLabel = "";
        } else {
            this.mountLabel = mountLabel;
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

    public String getRestartCount() {
        return restartCount;
    }

    public void setRestartCount(Integer restartCount) {
        if (Objects.isNull(restartCount)) {
            this.restartCount = "";
        } else {
            this.restartCount = String.valueOf(restartCount);
        }
    }
    /*
    public String getNetworkSettings() {
        return networkSettings;
    }

    public void setNetworkSettings(NetworkSettings networkSettings) {
        if (Objects.isNull(networkSettings)) {
            this.networkSettings = "";
        } else {
            this.networkSettings = networkSettings;
        }
    }
        */

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

    public String getProcessLabel() {
        return processLabel;
    }

    public void setProcessLabel(String processLabel) {
        if (Objects.isNull(processLabel)) {
            this.processLabel = "";
        } else {
            this.processLabel = processLabel;
        }
    }

    public String getResolvConfPath() {
        return resolvConfPath;
    }

    public void setResolvConfPath(String resolvConfPath) {
        if (Objects.isNull(resolvConfPath)) {
            this.resolvConfPath = "";
        } else {
            this.resolvConfPath = resolvConfPath;
        }
    }

    public String getExecIds() {
        return execIds;
    }

    public void setExecIds(List<String> execIds) {
        if (Objects.isNull(execIds)) {
            this.execIds = "";
        } else {
            if (!execIds.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String s : execIds) {
                    if (Objects.isNull(s)) {
                        sb.append("");
                    } else {
                        sb.append(s + ", ");
                    }
                }
                this.execIds = sb.toString();
            } else {
                this.execIds = "";
            }
        }
    }
  /*  
    public _ContainerState getState() {
        return state;
    }
    
    public void setState(_ContainerState state) {
         this.state = state;
    }
    public String getVolumes() {
        return volumes;
    }

    public void setVolumes(VolumeBinds volumes) {
        if (Objects.isNull(volumes)) {
            this.volumes = "";
        } else {
            this.volumes = volumes;
        }
    }

    public String getVolumesRW() {
        return volumesRW;
    }

    public void setVolumesRW(VolumesRW volumesRW) {
        if (Objects.isNull(volumesRW)) {
            this.volumesRW = "";
        } else {
            this.volumesRW = volumesRW;
        }
    }
*/
    public String getMounts() {
        return mounts;
    }

    public void setMounts(List<_Mount> mounts) {
        if (Objects.isNull(mounts)) {
            this.mounts = "";
        } else {
            if (!mounts.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (_Mount m : mounts) {
                    if (Objects.isNull(m)) {
                        sb.append("");
                    } else {
                        sb.append(m.toString() + ", ");
                    }
                }
                this.mounts = sb.toString();
            } else {
                this.mounts = "";
            }
        }
    }
/*
    public class _ContainerState {
        private String status;
        private String running;
        private String paused;
        private String restarting;
        private String oomKilled;
        private String dead;
        private String pid;
        private String exitCode;
        private String error;
        private String startedAt;
        private String finishedAt;
        
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
        
        public String getRunning() {
            return running;
        }
        
        public void setRunning(Boolean running) {
            if (Objects.isNull(running)) {
                this.running = "";
            } else {
                this.running = String.valueOf(running);
            }
        }
        
        public String getPaused() {
            return paused;
        }
        
        public void setPaused(Boolean paused) {
            if (Objects.isNull(paused)) {
                this.paused = "";
            } else {
                this.paused = String.valueOf(paused);
            }
        }
        
        public String getRestarting() {
            return restarting;
        }
        
        public void setRestarting(Boolean restarting) {
            if (Objects.isNull(restarting)) {
                this.restarting = "";
            } else {
                this.restarting = String.valueOf(restarting);
            }
        }
        
        public String getOomKilled() {
            return oomKilled;
        }
        
        public void setOomKilled(Boolean oomKilled) {
            if (Objects.isNull(oomKilled)) {
                this.oomKilled = "";
            } else {
                this.oomKilled = String.valueOf(oomKilled);
            }
        }
        
        public String getDead() {
            return dead;
        }
        
        public void setDead(Boolean dead) {
            if (Objects.isNull(dead)) {
                this.dead = "";
            } else {
                this.dead = String.valueOf(dead);
            }
        }
        
        public String getPid() {
            return pid;
        }
        
        public void setPid(Integer pid) {
            if (Objects.isNull(pid)) {
                this.pid = "";
            } else {
                this.pid = String.valueOf(pid);
            }
        }
        
        public String getExitCode() {
            return exitCode;
        }
        
        public void setExitCode(Integer exitCode) {
            if (Objects.isNull(exitCode)) {
                this.exitCode = "";
            } else {
                this.exitCode = String.valueOf(exitCode);
            }
        }
        
        public String getError() {
            return error;
        }
        
        public void setError(String error) {
            if (Objects.isNull(error)) {
                this.error = "";
            } else {
                this.error = error;
            }
        }
        
        public String getStartedAt() {
            return startedAt;
        }
        
        public void setStartedAt(String startedAt) {
            if (Objects.isNull(startedAt)) {
                this.startedAt = "";
            } else {
                this.startedAt = startedAt;
            }
        }
        
        public String getFinishedAt() {
            return finishedAt;
        }
        
        public void setFinishedAt(String finishedAt) {
            if (Objects.isNull(finishedAt)) {
                this.finishedAt = "";
            } else {
                this.finishedAt = finishedAt;
            }
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("&nbsp; &nbsp; &nbsp; status: " + state.getStatus() + "<br>");
            sb.append("&nbsp; &nbsp; &nbsp; running: " + state.getRunning() + "<br>");
            sb.append("&nbsp; &nbsp; &nbsp; paused: " + state.getPaused() + "<br>");
            sb.append("&nbsp; &nbsp; &nbsp; restarting: " + state.getRestarting() + "<br>");
            sb.append("&nbsp; &nbsp; &nbsp; oomKilled: " + state.getOomKilled() + "<br>");
            sb.append("&nbsp; &nbsp; &nbsp; dead: " + state.getDead() + "<br>");
            sb.append("&nbsp; &nbsp; &nbsp; pid: " + state.getPid() + "<br>");
            sb.append("&nbsp; &nbsp; &nbsp; exitCode: " + state.getExitCode() + "<br>");
            sb.append("&nbsp; &nbsp; &nbsp; error: " + state.getError() + "<br>");
            sb.append("&nbsp; &nbsp; &nbsp; startedAt: " + state.getStartedAt() + "<br>");
            sb.append("&nbsp; &nbsp; &nbsp; finishedAt: " + state.getFinishedAt());
            return sb.toString();
        }  
    }
*/
    
    public static class _Mount {
        
        private String name;
        private String source;
        private String destination;
        private String driver;
        private String mode;
        private String rw;
        
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
        
        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            if (Objects.isNull(source)) {
                this.source = "";
            } else {
                this.source = source;
            }
        }
        
        public String getDestination() {
            return destination;
        }

        public void setDestination(_Volume destination) {
            if (Objects.isNull(destination)) {
                this.destination = "";
            } else {
                this.destination = "&nbsp; &nbsp; &nbsp;  path: " + destination.getPath();
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
        
        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            if (Objects.isNull(mode)) {
                this.mode = "";
            } else {
                this.mode = mode;
            }
        }
        
        public String getRw() {
            return rw;
        }
        
        public void setRw(Boolean rw) {
            if (Objects.isNull(rw)) {
                this.rw = "";
            } else {
                this.rw = String.valueOf(rw);
            }
        }
        
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }  
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }  
}
