<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="docker.container.inspect.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>

   <h3><spring:message code="docker.container.inspect.title"/> <c:out value="${inspectContainerResponse.id}" /></h3>
   <p><spring:message code="docker.container.inspect.message"/></p>

   <c:choose>
    <c:when test="${error != null}">
     <p class="error"><b><c:out value="${error}" /></b></p>
    </c:when>
   </c:choose>
   
    <table class="list"> 
     <thead>
      <tr>
       <th>
        <spring:message code="docker.container.inspect.attributeName"/>
       </th>
       <th>
        <spring:message code="docker.container.inspect.attributeValue"/> 
       </th>
      </tr>
     </thead>
     <tbody>
      <tr>
       <td>
        <spring:message code="docker.container.inspect.args"/>
       </td>
       <td>
        <c:forEach var="arg" items="${inspectContainerResponse.args}">   
         <c:out value="${arg}" /><br/>
        </c:forEach>
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
             <c:forEach var="command" items="${inspectContainerResponse.config.cmd}">   
              <c:out value="${command}" /><br/>
             </c:forEach>
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
             <c:forEach var="ep" items="${inspectContainerResponse.config.entrypoint}">   
              <c:out value="${ep}" /><br/>
             </c:forEach>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.container.config.env"/>
            </td>
            <td>
             <c:forEach var="environ" items="${inspectContainerResponse.config.env}">   
              <c:out value="${environ}" /><br/>
             </c:forEach>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.container.config.exposedPorts"/>
            </td>
            <td>
             <c:catch var="exposedPortsError">
              <c:forEach var="port" items="${inspectContainerResponse.config.exposedPorts}">  
               <c:out value="${port}" /><br/>
              </c:forEach>
             </c:catch>
             <c:if test = "${exposedPortsError != null}">
              <i class="error">An exception was raised whilst trying to access the Exposed Port information. Exception is: ${exposedPortsError}</i>
             </c:if>
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
             <c:forEach var="lab" items="${inspectContainerResponse.config.labels}">   
              <c:out value="${lab.key}" /> :
              <c:out value="${lab.value}" /><br/>
             </c:forEach>
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
             <c:forEach var="onb" items="${inspectContainerResponse.config.onBuild}">   
              <c:out value="${onb}" /><br/>
             </c:forEach>
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
             <c:forEach var="ps" items="${inspectContainerResponse.config.portSpecs}">   
              <c:out value="${ps}" /><br/>
             </c:forEach>
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
             <c:forEach var="vol" items="${inspectContainerResponse.config.volumes}">   
              <c:out value="${vol.key}" /> :
              <c:out value="${vol.value}" /><br/>
             </c:forEach>
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
        <c:if test = "${containerConfigError != null}">
         <i class="error">An exception was raised whilst trying to access the Container Configuration information. Exception is: ${containerConfigError}</i>
        </c:if>
            
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
              <c:forEach var="bind" items="${inspectContainerResponse.hostConfig.binds}">   
               <c:out value="${bind}" /><br/>
              </c:forEach>
             </c:catch>
             <c:if test = "${bindsError != null}">
              <i class="error">An exception was raised whilst trying to access the Binds information. Exception is: ${bindsError}</i>
             </c:if>         
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.links"/>
            </td>
            <td>
             <c:catch var="linksError">
              <c:forEach var="link" items="${inspectContainerResponse.hostConfig.links}">   
               <c:out value="${link}" /><br/>
              </c:forEach>
              <c:out value="${inspectContainerResponse.hostConfig.links}" />
             </c:catch>
             <c:if test = "${linksError != null}">
              <i class="error">An exception was raised whilst trying to access the Links information. Exception is: ${linksError}</i>
             </c:if>     
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.lxcConf"/>
            </td>
            <td>
             <c:catch var="lxcConfError">
              <c:forEach var="conf" items="${inspectContainerResponse.hostConfig.lxcConf}">   
               <c:out value="${conf}" /><br/>
              </c:forEach>
             </c:catch>
             <c:if test = "${lxcConfError != null}">
              <i class="error">An exception was raised whilst trying to access the LXC Configuration information. Exception is: ${lxcConfError}</i>
             </c:if>  
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.logConfig"/>
            </td>
            <td>
             <c:catch var="logConfigError">
              <c:forEach var="logConf" items="${inspectContainerResponse.hostConfig.logConfig}">   
               <c:out value="${logConf}" /><br/>
              </c:forEach>
             </c:catch>
             <c:if test = "${logConfigError != null}">
              <i class="error">An exception was raised whilst trying to access the Log Configuration information. Exception is: ${logConfigError}</i>
             </c:if> 
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.portBindings"/>
            </td>
            <td>
             <c:catch var="portBindingsError">
              <c:out value="${inspectContainerResponse.hostConfig.portBindings}" />
             </c:catch>
             <c:if test = "${portBindingsError != null}">
              <i class="error">An exception was raised whilst trying to access the Port Bindings information. Exception is: ${portBindingsError}</i>
             </c:if> 
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
             <c:forEach var="d" items="${inspectContainerResponse.hostConfig.dns}">   
              <c:out value="${d}" /><br/>
             </c:forEach>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.dnsSearch"/>
            </td>
            <td>
             <c:forEach var="dSearch" items="${inspectContainerResponse.hostConfig.dnsSearch}">   
              <c:out value="${dSearch}" /><br/>
             </c:forEach>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.volumesFrom"/>
            </td>
            <td>
             <c:catch var="volumesFromError">
              <c:forEach var="volume" items="${inspectContainerResponse.hostConfig.volumesFrom}">   
               <c:out value="${volume}" /><br/>
              </c:forEach>
             </c:catch>
             <c:if test = "${volumesFromError != null}">
              <i class="error">An exception was raised whilst trying to access the Volumes From information. Exception is: ${volumesFromError}</i>
             </c:if> 
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
              <c:forEach var="cAdd" items="${inspectContainerResponse.hostConfig.capAdd}">   
               <c:out value="${cAdd}" /><br/>
              </c:forEach>
             </c:catch>
             <c:if test = "${capAddError != null}">
              <i class="error">An exception was raised whilst trying to access the Add Capabilities information. Exception is: ${capAddError}</i>
             </c:if> 
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.capDrop"/>
            </td>
            <td>
             <c:catch var="capDropError">                
              <c:forEach var="cDrop" items="${inspectContainerResponse.hostConfig.capDrop}">   
               <c:out value="${cDrop}" /><br/>
              </c:forEach>
             </c:catch>
             <c:if test = "${capDropError != null}">
              <i class="error">An exception was raised whilst trying to access the Drop Capabilities information. Exception is: ${capDropError}</i>
             </c:if> 
            </td>
           </tr>
           <tr>
            <td> 
             <spring:message code="docker.host.config.restartPolicy"/>
            </td>
            <td>
             <c:catch var="restartPolicyError">   
              <c:out value="${inspectContainerResponse.hostConfig.restartPolicy}" />
             </c:catch>
             <c:if test = "${restartPolicyError != null}">
              <i class="error">An exception was raised whilst trying to access the Restart Policy information. Exception is: ${restartPolicyError}</i>
             </c:if> 
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
              <c:forEach var="device" items="${inspectContainerResponse.hostConfig.devices}">   
               <c:out value="${device}" /><br/>
              </c:forEach>
             </c:catch>
             <c:if test = "${devicesError != null}">
              <i class="error">An exception was raised whilst trying to access the Devices information. Exception is: ${devicesError}</i>
             </c:if> 
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.extraHosts"/>
            </td>
            <td>
             <c:forEach var="extraHost" items="${inspectContainerResponse.hostConfig.extraHosts}">   
              <c:out value="${extraHost}" /><br/>
             </c:forEach>
            </td>
           </tr>
           <tr>
            <td>
             <spring:message code="docker.host.config.ulimits"/>
            </td>
            <td>
             <c:catch var="ulimitsError">   
              <c:forEach var="ulimit" items="${inspectContainerResponse.hostConfig.ulimits}">   
               <c:out value="${ulimit}" /><br/>
              </c:forEach>
             </c:catch>
             <c:if test = "${ulimitsError != null}">
              <i class="error">An exception was raised whilst trying to access the Ulimits information. Exception is: ${ulimitsError}</i>
             </c:if> 
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
        <c:if test = "${hostConfigError != null}">
         <i class="error">An exception was raised whilst trying to access the Host Configuration information. Exception is: ${hostConfigError}</i>
        </c:if>
            
       </td>
      </tr>
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
          </tbody>
         </table>
        </c:catch>
        <c:if test = "${networkSettingsError != null}">
         <i class="error">An exception was raised whilst trying to access the Network Settings information. Exception is: ${networkSettingsError}</i>
        </c:if>    
            
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
        <c:forEach var="execId" items="${inspectContainerResponse.execIds}">   
         <c:out value="${execId}" /><br/>
        </c:forEach>
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
          </tbody>
         </table>
        </c:catch>
        <c:if test = "${stateError != null}">
         <i class="error">An exception was raised whilst trying to access the Container State information. Exception is: ${stateError}</i>
        </c:if>    
            
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
          </tbody>
         </table>
        </c:catch>
        <c:if test = "${volumesError != null}">
         <i class="error">An exception was raised whilst trying to access the Volume Binds information. Exception is: ${volumesError}</i>
        </c:if>              
            
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
          </tbody>
         </table>
        </c:catch>
        <c:if test = "${volumesRWError != null}">
         <i class="error">An exception was raised whilst trying to access the Volume RW information. Exception is: ${volumesRWError}</i>
        </c:if>              
            
       </td>
      </tr>                
        
        
     </tbody>
    </table> 

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
