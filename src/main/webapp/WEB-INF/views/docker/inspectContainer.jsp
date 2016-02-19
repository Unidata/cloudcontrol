<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="docker.container.inspect.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>


   <h3>
    <spring:message code="docker.container.inspect.title"/> 
    <i><c:out value="${inspectContainerResponse.name}" /></i>
   </h3>
   <!-- left -->
   <div class="left">
    <c:choose>
     <c:when test="${error != null}">
      <p class="error"><b><c:out value="${error}" /></b></p>
     </c:when>
    </c:choose>
    
    <p>
     <spring:message code="docker.container.inspect.message"/> 
     <i><c:out value="${inspectContainerResponse.name}" /></i>
    </p>
   
    <table> 
     <tbody>
      <tr>
       <td>
        <spring:message code="docker.container.names"/>
       </td>
       <td>
        <table class="nodec">
         <tbody>
          <c:forEach var="name" items="${container.names}">   
           <tr>
            <td>
             <c:out value="${name}" />
            </td>
           </tr>                         
          </c:forEach>
         </tbody>
        </table> 
       </td>
      </tr>
      <tr>
       <td>
        <spring:message code="docker.container.id"/>
       </td>
       <td>
        <c:out value="${container.id}" />
       </td>
      </tr>   
      <tr>
       <td>
        <spring:message code="docker.container.image"/>
       </td>
       <td>
        <c:out value="${container.image}" />
       </td>
      </tr>       
      <tr>
       <td>
        <spring:message code="docker.container.command"/>
       </td>
       <td>
        <c:out value="${container.command}" />
       </td>
      </tr>    
      <tr>
       <td>
        <spring:message code="docker.container.created"/>
       </td>
       <td> 
        <c:set target="${myDate}" property="time" value="${container.created * 1000}"/>    
        <fmt:formatDate type="both" value="${myDate}" /> 
       </td>
      </tr>       
      <tr>
       <td>
        <spring:message code="docker.container.ports"/>
       </td>
       <td>
        <table class="nodec">
         <tbody>
          <c:forEach var="port" items="${container.ports}">
           <tr>
            <td>
             <spring:message code="docker.container.port.ip"/>
            </td>
            <td> 
             <c:out value="${port.ip}" />
            </td>
           </tr> 
           <tr>
            <td>
             <spring:message code="docker.container.port.privatePort"/>
            </td>
            <td> 
             <c:out value="${port.privatePort}" />
            </td>
           </tr>   
           <tr>
            <td>
             <spring:message code="docker.container.port.publicPort"/>
            </td>
            <td> 
             <c:out value="${port.publicPort}" />
            </td>
           </tr>  
           <tr>
            <td>
             <spring:message code="docker.container.port.type"/>
            </td>
            <td> 
             <c:out value="${port.type}" />
            </td>
           </tr>  
          </c:forEach>                       
         </tbody>
        </table> 
       </td>
      </tr>          
      <tr>
       <td>
        <spring:message code="docker.container.labels"/>
       </td>
       <td>
        <table class="nodec">
         <tbody>
          <c:forEach var="label" items="${container.labels}"> 
           <tr>
            <td>  
             <c:out value="${label.key}" />
            </td>
            <td>
             <c:out value="${label.value}" />
            </td>
           </tr> 
          </c:forEach>
         </tbody>
        </table> 
       </td>
      </tr>          
      <tr>
       <td>
        <spring:message code="docker.container.status"/>
       </td>
       <td>
        <c:out value="${container.status}" />      
       </td>
      </tr>             
     </tbody>
    </table>
     
    <h5><a href="#" id="inspectToggle" class="toggle"><spring:message code="docker.container.inspect.option.inspect"/></a></h5>
    <div id="inspectToggleSection" class="hideandshow">   
     <table class="list"> 
      <tbody>
       <tr>
        <td>
         <spring:message code="docker.container.inspect.args"/>
        </td>
        <td>
         <table class="nodec">
          <tbody>
           <c:forEach var="arg" items="${inspectContainerResponse.args}"> 
            <tr>
             <td>
              <c:out value="${arg}" />
             </td>
            </tr>         
           </c:forEach>
          </tbody>
         </table> 
        </td>
       </tr> 
       <tr>
        <td>
         <spring:message code="docker.container.inspect.config"/> 
        </td>
        <td>           
         <c:catch var="containerConfigError">        
          <table class="nodec">
           <tbody>
            <tr>
             <td>
              <spring:message code="docker.container.config.attachStderr"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.attachStderr}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.attachStdin"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.attachStdin}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.attachStdout"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.attachStdout}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.cmd"/>
             </td>
             <td>
              <table class="nodec">
               <tbody>
                <c:forEach var="command" items="${inspectContainerResponse.config.cmd}"> 
                 <tr>
                  <td>  
                   <c:out value="${command}" />
                  </td>
                 </tr>
                </c:forEach>
               </tbody>
              </table> 
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.domainname"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.domainName}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.entrypoint"/>
             </td>
             <td>
              <table class="nodec">
               <tbody>
                <c:forEach var="ep" items="${inspectContainerResponse.config.entrypoint}"> 
                 <tr>
                  <td>  
                   <c:out value="${ep}" />
                  </td>
                 </tr>  
                 </c:forEach>
               </tbody>
              </table> 
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.env"/>
             </td>
             <td>
              <table class="nodec">
               <tbody>
                <c:forEach var="environ" items="${inspectContainerResponse.config.env}"> 
                 <tr>
                  <td>  
                   <c:out value="${environ}" />
                  </td>
                 </tr>    
                </c:forEach>
               </tbody>
              </table> 
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.exposedPorts"/>
             </td>
             <td>
                                   <!--
              <c:catch var="exposedPortsError">
               <table class="nodec">
                <tbody>
                 <c:forEach var="ePort" items="${inspectContainerResponse.config.exposedPorts}">  
                  <tr>
                   <td>  
                    <spring:message code="docker.container.config.exposedPorts.port"/>
                   </td>
                   <td>  
                    <c:out value="${ePort.port}" />
                   </td>
                  </tr>    
                  <tr>
                   <td>  
                    <spring:message code="docker.container.config.exposedPorts.protocol"/>
                   </td>
                   <td>  
                    <c:out value="${ePort.protocol}" />
                   </td>
                  </tr>    
                 </c:forEach>
                </tbody>
               </table> 
              </c:catch>
                                      -->
             </td>
            </tr>      
            <tr>
             <td>
              <spring:message code="docker.container.config.hostname"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.hostName}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.image"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.image}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.labels"/>
             </td>
             <td>
              <table class="nodec">
               <tbody>
                <c:forEach var="lab" items="${inspectContainerResponse.config.labels}">
                 <tr>
                  <td>   
                   <c:out value="${lab.key}" />
                  </td>
                  <td>
                   <c:out value="${lab.value}" />
                  </td>
                 </tr>
                </c:forEach>
               </tbody>
              </table>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.macAddress"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.macAddress}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.networkDisabled"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.networkDisabled}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.onBuild"/>
             </td>
             <td>
              <table class="nodec">
               <tbody>
                <c:forEach var="onb" items="${inspectContainerResponse.config.onBuild}"> 
                 <tr>
                  <td>
                   <c:out value="${onb}" />    
                  </td>
                 </tr>  
                </c:forEach>
               </tbody>
              </table>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.openStdin"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.stdinOpen}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.portSpecs"/>
             </td>
             <td>
              <table class="nodec">
               <tbody>
                <c:forEach var="ps" items="${inspectContainerResponse.config.portSpecs}">  
                 <tr>
                  <td> 
                   <c:out value="${ps}" />
                  </td>
                 </tr> 
                </c:forEach>
               </tbody>
              </table>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.stdinOnce"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.stdInOnce}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.tty"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.tty}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.user"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.user}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.volumes"/>
             </td>
             <td>
              <table class="nodec">
               <tbody>
                <c:forEach var="vol" items="${inspectContainerResponse.config.volumes}">   
                 <tr>
                  <td>
                   <c:out value="${vol.key}" />
                  </td>
                  <td>
                   <c:out value="${vol.value}" />
                  </td>
                 </tr>
                </c:forEach>
               </tbody>
              </table>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.workingDir"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.config.workingDir}" />
             </td>
            </tr>
           </tbody>
          </table>
         </c:catch>           
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.container.inspect.created"/>
        </td>
        <td>
         <c:out value="${inspectContainerResponse.created}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.container.inspect.driver"/>
        </td>
        <td>
         <c:out value="${inspectContainerResponse.driver}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.container.inspect.execDriver"/>
        </td>
        <td>
         <c:out value="${inspectContainerResponse.execDriver}" />
        </td>
       </tr>        
       <!-- 
       <tr>
        <td>
         <spring:message code="docker.container.inspect.hostConfig"/>
        </td>
        <td>                          
         <c:catch var="hostConfigError">            
         <table class="nodec">
          <tbody>
           <tr>
            <td>
             <spring:message code="docker.host.config.binds"/>
            </td>
            <td>
             <c:catch var="bindsError">
              <table class="nodec">
               <tbody>
                <c:forEach var="bind" items="${inspectContainerResponse.hostConfig.binds}">  
                 <tr>
                  <td> 
                   <c:out value="${bind}" />
                  </td>
                 </tr>
                </c:forEach>
               </tbody>
              </table>
             </c:catch>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.links"/>
            </td>
            <td>
             <c:catch var="linksError">
              <table class="nodec">
               <tbody>                                  
                <c:forEach var="link" items="${inspectContainerResponse.hostConfig.links}">  
                 <tr>
                  <td> 
                   <c:out value="${link}" />
                  </td> 
                 </tr>
                </c:forEach>
               </tbody>
              </table>              
             </c:catch>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.lxcConf"/>
            </td>
            <td>
             <c:catch var="lxcConfError">
              <table class="nodec">
               <tbody>    
                <c:forEach var="conf" items="${inspectContainerResponse.hostConfig.lxcConf}"> 
                 <tr>
                  <td> 
                   <c:out value="${conf}" />
                  </td> 
                 </tr>  
                </c:forEach>
               </tbody>
              </table>     
             </c:catch>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.logConfig"/>
            </td>
            <td>
             <c:catch var="logConfigError">
              <table class="nodec">
               <tbody>    
                <c:forEach var="logConf" items="${inspectContainerResponse.hostConfig.logConfig}"> 
                  <tr>
                   <td> 
                   <c:out value="${logConf}" />
                  </td> 
                 </tr>    
                </c:forEach>
               </tbody>
              </table>     
             </c:catch>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.portBindings"/>
            </td>
            <td>
                
             <c:catch var="portBindingsError">
              <table class="nodec">
               <tbody>
                <c:out value="${inspectContainerResponse.hostConfig.portBindings}" />
               </tbody>
              </table>   
             </c:catch> 
              
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.publishAllPorts"/>
            </td>
            <td>
             <c:out value="${inspectContainerResponse.hostConfig.publishAllPorts}" />
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.privileged"/>
            </td>
            <td>
             <c:out value="${inspectContainerResponse.hostConfig.privileged}" />
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.readonlyRootfs"/>
            </td>
            <td>
             <c:out value="${inspectContainerResponse.hostConfig.readonlyRootfs}" />
            </td>
           </tr>               
           <tr>
            <td>
             <spring:message code="docker.host.config.dns"/>
            </td>
            <td>
             <table class="nodec">
              <tbody>
               <c:forEach var="d" items="${inspectContainerResponse.hostConfig.dns}">  
                <tr>
                 <td>
                  <c:out value="${d}" />
                 </td>
                </tr>             
               </c:forEach>
              </tbody>
             </table>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.dnsSearch"/>
            </td>
            <td>
             <table class="nodec">
              <tbody>
               <c:forEach var="dSearch" items="${inspectContainerResponse.hostConfig.dnsSearch}">   
                <tr>
                 <td>
                  <c:out value="${dSearch}" />
                 </td>
                </tr>   
               </c:forEach>
              </tbody>
             </table>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.volumesFrom"/>
            </td>
            <td>
             <c:catch var="volumesFromError">
              <table class="nodec">
               <tbody>
                <c:forEach var="volume" items="${inspectContainerResponse.hostConfig.volumesFrom}"> 
                 <tr>
                  <td>  
                   <c:out value="${volume}" />
                  </td>
                 </tr>   
                </c:forEach>
               </tbody>
              </table>
             </c:catch>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.containerIDFile"/>
            </td>
            <td>
             <c:out value="${inspectContainerResponse.hostConfig.containerIDFile}" />
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.capAdd"/>
            </td>
            <td>
             <c:catch var="capAddError">
              <table class="nodec">
               <tbody>
                <c:forEach var="cAdd" items="${inspectContainerResponse.hostConfig.capAdd}">   
                 <tr>
                  <td>
                   <c:out value="${cAdd}" />
                  </td>
                 </tr>
                </c:forEach>
               </tbody>
              </table>
             </c:catch>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.capDrop"/>
            </td>
            <td>
             <c:catch var="capDropError">      
              <table class="nodec">
               <tbody>
                <c:forEach var="cDrop" items="${inspectContainerResponse.hostConfig.capDrop}">   
                 <tr>
                  <td>
                   <c:out value="${cDrop}" />
                  </td>
                 </tr>
                </c:forEach>
               </tbody>
              </table>                
             </c:catch>
            </td>
           </tr>
           <tr>
            <td> 
             <spring:message code="docker.host.config.restartPolicy"/>
            </td>
            <td>
             <c:catch var="restartPolicyError">
              <table class="nodec">
               <tbody>
                <tr>
                 <td>   
                  <c:out value="${inspectContainerResponse.hostConfig.restartPolicy}" />
                 </td>
                </tr>
               </tbody>
              </table>  
             </c:catch>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.networkMode"/>
            </td>
            <td>
             <c:out value="${inspectContainerResponse.hostConfig.networkMode}" />
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.devices"/>
            </td>
            <td>
             <c:catch var="devicesError">  
              <table class="nodec">
               <tbody> 
                <c:forEach var="device" items="${inspectContainerResponse.hostConfig.devices}">   
                 <tr>
                  <td>  
                   <c:out value="${device}" />
                  </td>
                 </tr>
                </c:forEach>
               </tbody>
              </table>  
             </c:catch>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.extraHosts"/>
            </td>
            <td>
             <table class="nodec">
              <tbody> 
               <c:forEach var="extraHost" items="${inspectContainerResponse.hostConfig.extraHosts}">  
                <tr>
                 <td>   
                  <c:out value="${extraHost}" />
                 </td>
                </tr>
               </c:forEach>
              </tbody>
             </table>  
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.ulimits"/>
            </td>
            <td>
             <c:catch var="ulimitsError">
              <table class="nodec">
               <tbody>    
                <c:forEach var="ulimit" items="${inspectContainerResponse.hostConfig.ulimits}">   
                 <tr>
                  <td>   
                   <c:out value="${ulimit}" />
                  </td>
                 </tr>
                </c:forEach>
               </tbody>
              </table>  
             </c:catch>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.memory"/>
            </td>
            <td>
             <c:out value="${inspectContainerResponse.hostConfig.memoryLimit}" />
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.memorySwap"/>
            </td>
            <td>
             <c:out value="${inspectContainerResponse.hostConfig.memorySwap}" />
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.cpuShares"/>
            </td>
            <td>
             <c:out value="${inspectContainerResponse.hostConfig.cpuShares}" />
            </td>
           </tr>                    
          </tbody>
         </table>
        </c:catch>
        </td>
       </tr>
           -->
       <tr>
        <td>
         <spring:message code="docker.container.inspect.hostnamePath"/> 
        </td>
        <td>
         <c:out value="${inspectContainerResponse.hostnamePath}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.container.inspect.hostsPath"/> 
        </td>
        <td>
         <c:out value="${inspectContainerResponse.hostsPath}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.container.inspect.id"/>
        </td>
        <td>
         <c:out value="${inspectContainerResponse.id}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.container.inspect.imageId"/>
        </td>
        <td>
         <c:out value="${inspectContainerResponse.imageId}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.container.inspect.mountLabel"/>
        </td>
        <td>
         <c:out value="${inspectContainerResponse.mountLabel}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.container.inspect.name"/>
        </td>
        <td>
         <c:out value="${inspectContainerResponse.name}" />
        </td>
       </tr>        
       <tr>
        <td>
         <spring:message code="docker.container.inspect.networkSettings"/>
        </td>
        <td>                          
         <c:catch var="networkSettingsError">           
          <table class="nodec">
           <tbody>
            <tr>
             <td>
              <spring:message code="docker.container.inspect.networkSettings.ipAddress"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.networkSettings.ipAddress}" />
             </td>
            </tr>   
            <tr>
             <td>
              <spring:message code="docker.container.inspect.networkSettings.ipPrefixLen"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.networkSettings.ipPrefixLen}" />
             </td>
            </tr>            
            <tr>
             <td>
              <spring:message code="docker.container.inspect.networkSettings.gateway"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.networkSettings.gateway}" />
             </td>
            </tr> 
            <tr>
             <td>
              <spring:message code="docker.container.inspect.networkSettings.bridge"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.networkSettings.bridge}" />
             </td>
            </tr>     
            <tr>
             <td>
              <spring:message code="docker.container.inspect.networkSettings.portMapping"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.networkSettings.portMapping}" />
             </td>
            </tr>      
            <tr>
             <td>
              <spring:message code="docker.container.inspect.networkSettings.ports"/>
             </td>
             <td>
			  <c:out value="${inspectContainerResponse.networkSettings.ports}" />
             </td>
            </tr>               
           </tbody>
          </table>
         </c:catch>
        </td>
       </tr>        
       <tr>
        <td>
         <spring:message code="docker.container.inspect.path"/>
        </td>
        <td>
         <c:out value="${inspectContainerResponse.path}" />
        </td>
       </tr>                
       <tr>
        <td>
         <spring:message code="docker.container.inspect.processLabel"/>
        </td>
        <td>
         <c:out value="${inspectContainerResponse.processLabel}" />
        </td>
       </tr>               
       <tr>
        <td>
         <spring:message code="docker.container.inspect.resolvConfPath"/>
        </td>
        <td>
         <c:out value="${inspectContainerResponse.resolvConfPath}" />
        </td>
       </tr>               
       <tr>
        <td>
         <spring:message code="docker.container.inspect.execIds"/>
        </td>
        <td>
         <table class="nodec">
          <tbody>
           <c:forEach var="execId" items="${inspectContainerResponse.execIds}"> 
            <tr>  
             <td>             
              <c:out value="${execId}" /><br/>
             </td>
            </tr> 
           </c:forEach>
          </tbody>
         </table>
        </td>
       </tr>      
       <tr>
        <td>
         <spring:message code="docker.container.inspect.state"/>
        </td>
        <td>                  
         <c:catch var="stateError">           
          <table class="nodec">
           <tbody>
            <tr>
             <td>
              <spring:message code="docker.container.inspect.state.running"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.state.running}" />
             </td>
            </tr>  
            <tr>
             <td>
              <spring:message code="docker.container.inspect.state.paused"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.state.paused}" />
             </td>
            </tr>  
            <tr>
             <td>
              <spring:message code="docker.container.inspect.state.pid"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.state.pid}" />
             </td>
            </tr>    
            <tr>
             <td>
              <spring:message code="docker.container.inspect.state.exitCode"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.state.exitCode}" />
             </td>
            </tr>    
            <tr>
             <td>
              <spring:message code="docker.container.inspect.state.startedAt"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.state.startedAt}" />
             </td>
            </tr>              
            <tr>
             <td>
              <spring:message code="docker.container.inspect.state.finishedAt"/>
             </td>
             <td>
              <c:out value="${inspectContainerResponse.state.finishedAt}" />
             </td>
            </tr>     
           </tbody>
          </table>
         </c:catch>
        </td>
       </tr>                
       <tr>
        <td>
         <spring:message code="docker.container.inspect.volumes"/>
        </td>
        <td>              
         <c:catch var="volumesError">           
          <table class="nodec">
           <tbody>
			<c:forEach var="vol" items="${inspectContainerResponse.volumes}">  
             <tr>
              <td>
               <spring:message code="docker.container.inspect.volumes.hostPath"/>
              </td>
              <td>
               <c:out value="${vol.hostPath}" />
              </td>
             </tr> 
             <tr>
              <td>
               <spring:message code="docker.container.inspect.volumes.containerPath"/>
              </td>
              <td>
               <c:out value="${vol.containerPath}" />
              </td>
             </tr> 
			</c:forEach> 
           </tbody>
          </table>
         </c:catch>
        </td>
       </tr>        
       <tr>
        <td>
         <spring:message code="docker.container.inspect.volumesRW"/>
        </td>
        <td>            
         <c:catch var="volumesRWError">           
          <table class="nodec">
           <tbody>
   			<c:forEach var="volRW" items="${inspectContainerResponse.volumesRW}">  
             <tr>
              <td>
               <spring:message code="docker.container.inspect.volumesRW.volume"/>
              </td>
              <td>
               <c:out value="${volRW.volume}" />
              </td>
             </tr> 
             <tr>
              <td>
               <spring:message code="docker.container.inspect.volumesRW.accessMode"/>
              </td>
              <td>
               <c:out value="${volRW.accessMode}" />
              </td>
             </tr> 
			</c:forEach>
           </tbody>
          </table>
         </c:catch>
        </td>
       </tr>                
      </tbody>
     </table> 
    </div><!--./hideandshow-->
 
    </div> <!-- /.left -->
    <!-- right -->
    <div class="right">
     <h5>
      <spring:message code="docker.container.inspect.option.title"/>
      <i>
       <c:out value="${inspectContainerResponse.name}" />
      </i>
     </h5>
     <ul>
      <li><a href="${baseUrl}/docker/container/<c:out value="${container.id}" />/start"><spring:message code="docker.container.inspect.option.start"/></a></li>
      <li><a href="${baseUrl}/docker/container/<c:out value="${container.id}" />/stop"><spring:message code="docker.container.inspect.option.stop"/></a></li>
      <li><spring:message code="docker.container.inspect.option.delete"/></li>
     </ul>
    </div><!-- /.right -->    

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
