<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
<html>
 <head>
  <title><spring:message code="global.title"/> : Image Inspection Details</title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
 </head>
 <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>

   <h3>
    Inspection Details for Image
    <i>
     <c:forEach var="tag" items="${image.repoTags}">    
       <c:out value="${tag}" />
     </c:forEach>
    </i>
   </h3>
    
   <!-- fullWidth -->
   <div class="fullWidth">
    <c:choose>
     <c:when test="${error != null}">
      <p class="error"><b><c:out value="${error}" /></b></p>
     </c:when>
    </c:choose>
    

    <table> 
     <tbody>
      <tr>
       <td>
        Repository/Tags
       </td>
       <td>
       <c:forEach var="tag" items="${image.repoTags}">    
        <c:out value="${tag}" /><br/>
       </c:forEach>
       </td>
      </tr>
      <tr>      
       <td>
        ID
       </td>
       <td>
        <c:out value="${image.id}" />
       </td>
      </tr>
      <tr>      
       <td>
        Parent ID
       </td>
       <td>
        <c:out value="${image.parentId}" />
       </td>
      </tr>
      <tr>      
       <td> 
       Date Created
       </td>
       <td>
        <c:set target="${myDate}" property="time" value="${image.created * 1000}"/>    
        <fmt:formatDate type="both" value="${myDate}" /> 
       </td>
      </tr>
      <tr>      
       <td>
        Virtual Size
       </td>
       <td>
        <c:choose>
         <c:when test="${image.virtualSize > 1000000}">
          <c:set var="vSize" value="${image.virtualSize / 1000000}"/>              
          <fmt:formatNumber value="${vSize}" type="number"/> MB
         </c:when>
         <c:when test="${image.virtualSize > 1000}">
          <c:set var="vSize" value="${image.virtualSize / 1000}"/>              
          <fmt:formatNumber value="${vSize}" type="number"/> KB
         </c:when>
         <c:otherwise>
          <c:out value="${image.virtualSize}" />  B
         </c:otherwise>
        </c:choose>
       </td>
      </tr>
      <c:if test="${inspectImageResponse.size > 0}">
       <tr>
        <td>
         Size
        </td>
        <td>
         <c:choose>
          <c:when test="${inspectImageResponse.size > 1000000}">
           <c:set var="size" value="${inspectImageResponse.size / 1000000}"/>              
           <fmt:formatNumber value="${size}" type="number"/> MB
          </c:when>
          <c:when test="${inspectImageResponse.size > 1000}">
           <c:set var="size" value="${inspectImageResponse.size / 1000}"/>              
           <fmt:formatNumber value="${size}" type="number"/> KB
          </c:when>
          <c:otherwise>
           <c:out value="${inspectImageResponse.size}" />  B
          </c:otherwise>
         </c:choose>  
        </td>
       </tr>
      </c:if>
      <tr>
       <td>
        Docker Version
       </td>
       <td>
        <c:out value="${inspectImageResponse.dockerVersion}" />
       </td>
      </tr>
       <tr>
        <td>
         Author
        </td>
        <td>
         <c:out value="${inspectImageResponse.author}" />
        </td>
       </tr> 
       <tr>
        <td>
         Architecture
        </td>
        <td>
         <c:out value="${inspectImageResponse.arch}" />
        </td>
       </tr>
       <tr>
        <td> 
         Operating System
        </td>             
        <td>
         <c:out value="${inspectImageResponse.os}" />
        </td>
       </tr>
       <tr>
        <td>
         Comment
        </td>
        <td>
         <c:out value="${inspectImageResponse.comment}" />
        </td>
       </tr>
       <tr>
        <td>
         Configuration
        </td>
        <td>
         <c:catch var="containerConfigError">    
          <table class="nodec">
           <tbody>
            <tr>
             <td>
              Attach STDERR
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.attachStderr}" />
             </td>
            </tr>
            <tr>
             <td>
              Attach STDIN
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.attachStdin}" />
             </td>
            </tr>
            <tr>
             <td>
              Attach STDOUT
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.attachStdout}" />
             </td>
            </tr>
            <tr>
             <td>
              Command
             </td>
             <td>
              <c:forEach var="command" items="${inspectImageResponse.config.cmd}">    
               <c:out value="${command}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              Domain Name
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.domainName}" />
             </td>
            </tr>
            <tr>
             <td>
              Entry Point
             </td>
             <td>
              <c:forEach var="ep" items="${inspectImageResponse.config.entrypoint}">    
               <c:out value="${ep}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              Environment Variables
             </td>
             <td>
              <c:forEach var="environ" items="${inspectImageResponse.config.env}">    
               <c:out value="${environ}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              Exposed Ports
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
              Host Name
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.hostName}" />
             </td>
            </tr>
            <tr>
             <td>
              Image
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.image}" />
             </td>
            </tr>
            <tr>
             <td>
              Labels
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
              Mac Address
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.macAddress}" />
             </td>
            </tr>
            <tr>
             <td>
              Network Disabled
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.networkDisabled}" />
             </td>
            </tr>
            <tr>
             <td>
              On Build
             </td>
             <td>
              <c:forEach var="onb" items="${inspectImageResponse.config.onBuild}">    
               <c:out value="${onb}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              Open STDIN
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.stdinOpen}" />
             </td>
            </tr>
            <tr>
             <td>
              Port Specifications
             </td>
             <td>
              <c:forEach var="ps" items="${inspectImageResponse.config.portSpecs}">    
               <c:out value="${ps}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
              STDIN Once
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.stdInOnce}" />
             </td>
            </tr>
            <tr>
             <td>
              TTY
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.tty}" />
             </td>
            </tr>
            <tr>
             <td>
              User
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.user}" />
             </td>
            </tr>
            <tr>
             <td>
              Volumes
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
              Working Directory
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
      </tbody>
     </table>       

    
   </div> <!-- /.fullWidth -->

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
