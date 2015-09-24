<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="docker.create.container.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>

   <h3><spring:message code="docker.create.container.title"/></h3>
   <p><spring:message code="docker.create.container.message"/></p>

   <c:choose>
    <c:when test="${error != null}">
     <p class="error"><b><c:out value="${error}" /></b></p>
    </c:when>
   </c:choose>

   <form:form action="${baseUrl}/docker/container/create" commandName="newContainer" method="POST">   
    <table> 
     <thead>
      <tr>
      <th>
		<spring:message code="docker.create.container.name"/> <img src="${baseUrl}/<spring:message code="help.path"/>" alt="<spring:message code="docker.create.container.name.description"/>"/>
       </th>
       <th>
        <spring:message code="docker.create.container.portNumber"/>  <img src="${baseUrl}/<spring:message code="help.path"/>" alt="<spring:message code="docker.create.container.portNumber.description"/>"/>
       </th>
       <th>
        <spring:message code="docker.create.container.hostName"/>  <img src="${baseUrl}/<spring:message code="help.path"/>" alt="<spring:message code="docker.create.container.hostName.description"/>"/>
       </th>
       <th>
       </th>
      </tr>
     </thead>
     <tbody>
      <tr>
       <td>
        <form:errors path="name" cssClass="error" />
        <form:input path="name"/>
       </td>
       <td>
        <form:errors path="portNumber" cssClass="error" />
        <form:input path="portNumber"/>
       </td>
       <td>
        <form:errors path="hostName" cssClass="error" />
        <form:input path="hostName"/>
       </td>
       <td>
        <input type="submit" value="Create Container" />
       </td>
      </tr>
     </tbody>
    </table> 
   </form:form>

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
