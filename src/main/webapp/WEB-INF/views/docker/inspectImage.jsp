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
        <div class="tooltip" class="tooltip" title="The <b>repository</b> can be a combination of a <b>Docker Hub account name</b> and an <b>image name</b>.  The <b>tag</b> is typically a <b>version name</b> or an <b>identifying implementation</b> of the image.">
         Repository/Tag
        </div>
       </td>
       <td>
       <c:forEach var="tag" items="${image.repoTags}">    
        <c:out value="${tag}" /><br/>
       </c:forEach>
       </td>
      </tr>
      <tr>      
       <td>
		<div class="tooltip" class="tooltip" title="This is the <b>unique identifier</b> of the image.">
         ID
	    </div>
       </td>
       <td>
        <c:out value="${image.id}" />
       </td>
      </tr>
      <tr>      
       <td>
		<div class="tooltip" class="tooltip" title="A link to the <b>identifier</b> of the <b>parent image</b>. It is very common for an image to have a defined parent.">
         Parent ID
	    </div>
       </td>
       <td>
        <c:out value="${image.parentId}" />
       </td>
      </tr>
      <tr>      
       <td>
		<div class="tooltip" class="tooltip" title="The <b>date and time</b> the image was created.">
         Date Created
	    </div>
       </td>
       <td>
        <c:set target="${myDate}" property="time" value="${image.created * 1000}"/>    
        <fmt:formatDate type="both" value="${myDate}" /> 
       </td>
      </tr>
      <tr>      
       <td>
		<div class="tooltip" class="tooltip" title="The <b>size</b> of the image.">
         Virtual Size
	    </div>
       </td>
       <td>
        <c:choose>
         <c:when test="${image.virtualSize > 1000000}">
          <c:set var="vSize" value="${image.virtualSize / 1000000}"/>              
          <fmt:formatNumber value="${vSize}" type="number" maxFractionDigits="0" /> MB
         </c:when>
         <c:when test="${image.virtualSize > 1000}">
          <c:set var="vSize" value="${image.virtualSize / 1000}"/>              
          <fmt:formatNumber value="${vSize}" type="number" maxFractionDigits="0" /> KB
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
		 <div class="tooltip" class="tooltip" title="The <b>size</b> of the image.">
         Size
	     </div>
        </td>
        <td>
         <c:choose>
          <c:when test="${inspectImageResponse.size > 1000000}">
           <c:set var="size" value="${inspectImageResponse.size / 1000000}"/>              
           <fmt:formatNumber value="${size}" type="number" maxFractionDigits="0" /> MB
          </c:when>
          <c:when test="${inspectImageResponse.size > 1000}">
           <c:set var="size" value="${inspectImageResponse.size / 1000}"/>              
           <fmt:formatNumber value="${size}" type="number" maxFractionDigits="0" /> KB
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
        <div class="tooltip" class="tooltip" title="The version of Docker <b>used to create the image</b> is stored in this value.">
         Docker Version
        </div>
       </td>
       <td>
        <c:out value="${inspectImageResponse.dockerVersion}" />
       </td>
      </tr>
       <tr>
        <td>
		 <div class="tooltip" class="tooltip" title="The <b>person or organization</b> who authored the image.">
          Author
	     </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.author}" />
        </td>
       </tr> 
       <tr>
        <td>
		 <div class="tooltip" class="tooltip" title="Type of <b>system</b> of the server the image runs on (e.g.: 64-bit, x64, or x86_64).">
          Architecture
	     </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.arch}" />
        </td>
       </tr>
       <tr>
        <td> 
		 <div class="tooltip" class="tooltip" title="Type of <b>operating system</b> of the server the image uses (e.g: linux).">
          Operating System
	     </div>
        </td>             
        <td>
         <c:out value="${inspectImageResponse.os}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="Any  <b>comments</b> about the image provided by the author."> 
          Comment
	     </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.comment}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="A container identifier is for an image not a container. This container identifier is a <b>temporary container created when the image was built</b>. Docker will create a container during the image construction process, and this identifier is stored in the image data."> 
          Container
	     </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.container}" />
        </td>
       </tr>
       <tr>
        <td>
		 <div class="tooltip" class="tooltip" title="This data again is referring to the <b>configuration of the temporary container</b> created when the Docker build command was executed."> 
          Configuration
	     </div>
        </td>
        <td>
         <c:catch var="containerConfigError">    
          <table class="nodec">
           <tbody>
            <tr>
             <td>
              <div class="tooltip" class="tooltip" title="Whether the image is configured to attach the <b>process standard error</b> to the console.">
               Attach STDERR 
              </div>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.attachStderr}" />
             </td>
            </tr>
            <tr>
             <td>
              <div class="tooltip" class="tooltip" title="Whether the image is configured to attach the <b>process standard input</b> to the console.">
                Attach STDIN
              </div>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.attachStdin}" />
             </td>
            </tr>
            <tr>
             <td>
              <div class="tooltip" class="tooltip" title="Whether the image is configured to attach the <b>process standard output</b> to the console.">
               Attach STDOUT
              </div>
             </td>
             <td>
              <c:out value="${inspectImageResponse.config.attachStdout}" />
             </td>
            </tr>
            <tr>
             <td>
			  <div class="tooltip" class="tooltip" title="Default <b>commands</b> provided by the author for <b>execution</b>. ">
              Command
		      </div>
             </td>
             <td>
              <c:forEach var="command" items="${inspectImageResponse.config.cmd}">    
               <c:out value="${command}" /><br/>
              </c:forEach>
             </td>
            </tr>
            <tr>
             <td>
			  <div class="tooltip" class="tooltip" title="Default <b>commands</b> provided by the author for <b>execution</b>. ">
               Domain Name
		      </div>
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
