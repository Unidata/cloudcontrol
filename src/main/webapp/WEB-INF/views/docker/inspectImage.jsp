<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
<html>
 <head>
  <title><spring:message code="global.title"/> : <spring:message code="docker.image.inspect.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
 </head>
 <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>

   <h3>
    <spring:message code="docker.image.inspect.title"/> 
    <i>
     <c:forEach var="tag" items="${image.repoTags}">    
       <c:out value="${tag}" />
     </c:forEach>
    </i>
   </h3>
    
   <!-- left -->
   <div class="left">
    <c:choose>
     <c:when test="${error != null}">
      <p class="error"><b><c:out value="${error}" /></b></p>
     </c:when>
    </c:choose>
    
    <p>
     <spring:message code="docker.image.inspect.message"/> 
     <i>
      <c:forEach var="tag" items="${image.repoTags}">    
       <c:out value="${tag}" />
      </c:forEach>
     </i>
    </p>
    <table> 
     <tbody>
      <tr>
       <td>
        <spring:message code="docker.image.repoTags"/>
       </td>
       <td>
       <c:forEach var="tag" items="${image.repoTags}">    
        <c:out value="${tag}" /><br/>
       </c:forEach>
       </td>
      </tr>
      <tr>      
       <td>
        <spring:message code="docker.image.id"/>
       </td>
       <td>
        <c:out value="${image.id}" />
       </td>
      </tr>
      <tr>      
       <td>
        <spring:message code="docker.image.parentId"/>
       </td>
       <td>
        <c:out value="${image.parentId}" />
       </td>
      </tr>
      <tr>      
       <td> 
        <spring:message code="docker.image.created"/>
       </td>
       <td>
        <c:out value="${image.created}" />    
       </td>
      </tr>
      <tr>      
       <td>
        <spring:message code="docker.image.virtualSize"/> 
       </td>
       <td>
        <c:out value="${image.virtualSize}" />    
       </td>
      </tr>
      <tr>      
       <td>
        <spring:message code="docker.image.size"/> 
       </td>
       <td>
        <c:out value="${image.size}" />     
       </td>
      </tr>
     </tbody>
    </table>


    <h5><a href="#" id="inspectToggle" class="toggle"><spring:message code="docker.image.inspect.option.inspect"/></a></h5>
    <div id="inspectToggleSection" class="hideandshow">
     <table> 
      <tbody>
       <tr>
        <td>
         <spring:message code="docker.image.inspect.author"/> 
        </td>
        <td>
         <c:out value="${inspectImageResponse.author}" />
        </td>
       </tr> 
       <tr>
        <td>
         <spring:message code="docker.image.inspect.arch"/>
        </td>
        <td>
         <c:out value="${inspectImageResponse.arch}" />
        </td>
       </tr>
       <tr>
        <td> 
         <spring:message code="docker.image.inspect.os"/>
        </td>             
        <td>
         <c:out value="${inspectImageResponse.os}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.image.inspect.comment"/> 
        </td>
        <td>
         <c:out value="${inspectImageResponse.comment}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.image.inspect.config"/> 
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
              <c:out value="${inspectImageResponse.config.attachStderr}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.attachStdin"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.attachStdin}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.attachStdout"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.attachStdout}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.cmd"/>
             </td>
             <td>
              <c:forEach var="command" items="${inspectImageResponse.config.cmd}">    
               <c:out value="${command}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.domainname"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.domainName}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.entrypoint"/>
             </td>
             <td>
              <c:forEach var="ep" items="${inspectImageResponse.config.entrypoint}">    
               <c:out value="${ep}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.env"/>
             </td>
             <td>
              <c:forEach var="environ" items="${inspectImageResponse.config.env}">    
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
               <c:forEach var="exposedPort" items="${inspectImageResponse.config.exposedPorts}">  
                <c:out value="${exposedPort}" /><br/>
               </c:forEach>
              </c:catch>
             </td>
            </tr>      
            <tr>
             <td>
              <spring:message code="docker.container.config.hostname"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.hostName}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.image"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.image}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.labels"/>
             </td>
             <td>
              <c:forEach var="lab" items="${inspectImageResponse.config.labels}">    
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
              <c:out value="${inspectImageResponse.config.macAddress}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.networkDisabled"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.networkDisabled}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.onBuild"/>
             </td>
             <td>
              <c:forEach var="onb" items="${inspectImageResponse.config.onBuild}">    
               <c:out value="${onb}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.openStdin"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.stdinOpen}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.portSpecs"/>
             </td>
             <td>
              <c:forEach var="ps" items="${inspectImageResponse.config.portSpecs}">    
               <c:out value="${ps}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.stdinOnce"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.stdInOnce}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.tty"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.tty}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.user"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.user}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.volumes"/>
             </td>
             <td>
              <c:forEach var="vol" items="${inspectImageResponse.config.volumes}">    
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
              <c:out value="${inspectImageResponse.config.workingDir}" />
             </td>
            </tr>
           </tbody>
          </table>
         </c:catch>

        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.image.inspect.container"/> 
        </td>
        <td>
         <c:out value="${inspectImageResponse.container}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.image.inspect.containerConfig"/> 
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
              <c:out value="${inspectImageResponse.containerConfig.attachStderr}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.attachStdin"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.containerConfig.attachStdin}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.attachStdout"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.containerConfig.attachStdout}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.cmd"/>
             </td>
             <td>
              <c:forEach var="command" items="${inspectImageResponse.containerConfig.cmd}">    
               <c:out value="${command}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.domainname"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.containerConfig.domainName}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.entrypoint"/>
             </td>
             <td>
              <c:forEach var="ep" items="${inspectImageResponse.containerConfig.entrypoint}">    
               <c:out value="${ep}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.env"/>
             </td>
             <td>
              <c:forEach var="environ" items="${inspectImageResponse.containerConfig.env}">    
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
               <c:forEach var="port" items="${inspectImageResponse.containerConfig.exposedPorts}">  
                <c:out value="${port}" /><br/>
               </c:forEach>
              </c:catch>                  
             </td>
            </tr>              
            <tr>
             <td>
              <spring:message code="docker.container.config.hostname"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.containerConfig.hostName}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.image"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.containerConfig.image}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.labels"/>
             </td>
             <td>
              <c:forEach var="lab" items="${inspectImageResponse.containerConfig.labels}">    
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
              <c:out value="${inspectImageResponse.containerConfig.macAddress}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.networkDisabled"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.containerConfig.networkDisabled}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.onBuild"/>
             </td>
             <td>
              <c:forEach var="onb" items="${inspectImageResponse.containerConfig.onBuild}">    
               <c:out value="${onb}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.openStdin"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.containerConfig.stdinOpen}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.portSpecs"/>
             </td>
             <td>
              <c:forEach var="ps" items="${inspectImageResponse.containerConfig.portSpecs}">    
               <c:out value="${ps}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.stdinOnce"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.containerConfig.stdInOnce}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.tty"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.containerConfig.tty}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.user"/>
             </td>
             <td>
              <c:out value="${inspectImageResponse.containerConfig.user}" />
             </td>
            </tr>
            <tr>
             <td>
              <spring:message code="docker.container.config.volumes"/>
             </td>
             <td>
              <c:forEach var="vol" items="${inspectImageResponse.containerConfig.volumes}">    
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
              <c:out value="${inspectImageResponse.containerConfig.workingDir}" />
             </td>
            </tr>
           </tbody>
          </table>
         </c:catch>
            
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.image.inspect.created"/> 
        </td>
        <td>
         <c:out value="${inspectImageResponse.created}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.image.inspect.dockerVersion"/> 
        </td>
        <td>
         <c:out value="${inspectImageResponse.dockerVersion}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.image.inspect.id"/>
        </td>
        <td>
         <c:out value="${inspectImageResponse.id}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.image.inspect.parent"/> 
        </td>
        <td>
         <c:out value="${inspectImageResponse.parent}" />
        </td>
       </tr>
       <tr>
        <td>
         <spring:message code="docker.image.inspect.size"/> 
        </td>
        <td>
         <c:out value="${inspectImageResponse.size}" />
        </td>
       </tr>
      </tbody>
     </table>       
    </div><!--./hideandshow-->
    
   </div> <!-- /.left -->
   <!-- right -->
   <div class="right">
    <h5>
     <spring:message code="docker.image.inspect.option.title"/>
     <i>
      <c:forEach var="tag" items="${image.repoTags}">    
       <c:out value="${tag}" />
      </c:forEach>
     </i>
    </h5>
    <ul>
     <li><spring:message code="docker.image.inspect.option.history"/></li>
     <li><spring:message code="docker.image.inspect.option.registry"/></li>
     <li><spring:message code="docker.image.inspect.option.tag"/></li>
     <li><a href="${baseUrl}/docker/container/create"><spring:message code="docker.image.inspect.option.createContainer"/></a></li>
     <li><spring:message code="docker.image.inspect.option.delete"/></li>
     </ul>
   </div><!-- /.right -->

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
