<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="docker.list.containers.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>

   <h3><c:out value="${fn:length(containers)}" /> <spring:message code="docker.list.containers.title"/></h3>
   <p><spring:message code="docker.list.containers.message"/></p>

   <c:choose>
    <c:when test="${error != null}">
     <p class="error"><b><c:out value="${error}" /></b></p>
    </c:when>
   </c:choose>
   
    <table class="list"> 
     <c:choose>
      <c:when test="${fn:length(containers) gt 0}">
       <thead>
        <tr>
         <th>
          <spring:message code="docker.container.command"/>
         </th>
         <th>
          <spring:message code="docker.container.id"/>
         </th>
         <th> 
          <spring:message code="docker.container.created"/>
         </th>
         <th>
          <spring:message code="docker.container.image"/>
         </th>
         <th>
          <spring:message code="docker.container.names"/> 
         </th>
         <th>
          <spring:message code="docker.container.ports"/> 
         </th>
         <th>
          <spring:message code="docker.container.labels"/> 
         </th>
         <th>
          <spring:message code="docker.container.status"/> 
         </th>
        </tr>
       </thead>
       <tbody>
        <c:forEach items="${containers}" var="container">    
         <tr>
          <td>
   		   <a href="${baseUrl}/docker/container/<c:out value="${container.id}" />">
            <c:out value="${container.command}" />
   		   </a> 
          </td>
          <td>
		   <a href="${baseUrl}/docker/container/<c:out value="${container.id}" />">
            <c:out value="${container.id}" />
		   </a>
          </td>		  
          <td>
		   <a href="${baseUrl}/docker/container/<c:out value="${container.id}" />">
            <c:out value="${container.created}" />	
	       </a> 		      
          </td>	  
          <td>
		   <a href="${baseUrl}/docker/container/<c:out value="${container.id}" />">
            <c:out value="${container.image}" />
		   </a> 
          </td>		  
          <td>
		   <a href="${baseUrl}/docker/container/<c:out value="${container.id}" />">
		    <c:forEach var="name" items="${container.names}">   
             <c:out value="${name}" /><br/>
		    </c:forEach>
	       </a>
          </td>
          <td>
		   <a href="${baseUrl}/docker/container/<c:out value="${container.id}" />">
		    <c:forEach var="port" items="${container.ports}">   
             <c:out value="${port}" /><br/>
		    </c:forEach>
	       </a>
          </td>
          <td>
		   <a href="${baseUrl}/docker/container/<c:out value="${container.id}" />">
		    <c:forEach var="label" items="${container.labels}">   
             <c:out value="${label.key}" /> : 
			 <c:out value="${label.value}" /><br/>
		    </c:forEach>
	       </a>
          </td>
          <td>
		   <a href="${baseUrl}/docker/container/<c:out value="${container.id}" />">
            <c:out value="${container.status}" />	  
	       </a> 
          </td>
         </tr>
        </c:forEach>
       </tbody>
      </c:when>
      <c:otherwise>
       <tr>
        <td>
         <spring:message code="docker.list.containers.none"/>
        </td>
       </tr>
      </c:otherwise>
     </c:choose>
    </table> 

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
