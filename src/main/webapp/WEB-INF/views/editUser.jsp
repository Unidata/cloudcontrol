<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="user.edit.title"/> : <c:out value="${user.userName}" /></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3><spring:message code="user.edit.title"/>: <c:out value="${user.userName}" /></h3>
   

   <!-- fullWidth -->
   <div class="fullWidth">        
    <p><spring:message code="user.edit.message"/></p>

    <c:choose>
     <c:when test="${error != null}">
      <p class="error"><b><c:out value="${error}" /></b></p>
     </c:when>
    </c:choose>
    
    <form:form action="${baseUrl}/user/edit" commandName="user" method="POST">
     <table>    
      <thead>
       <tr>
        <th>
         <spring:message code="user.userName"/> <img src="${baseUrl}/<spring:message code="help.path"/>" alt="<spring:message code="user.userName.description"/>"/>
        </th>
        <th>
         <spring:message code="user.firstName"/> <img src="${baseUrl}/<spring:message code="help.path"/>" alt="<spring:message code="user.firstName.description"/>"/>
        </th>
        <th>
         <spring:message code="user.lastName"/> <img src="${baseUrl}/<spring:message code="help.path"/>" alt="<spring:message code="user.lastName.description"/>"/>
        </th>
        <th>
         <spring:message code="user.emailAddress"/> <img src="${baseUrl}/<spring:message code="help.path"/>" alt="<spring:message code="user.emailAddress.description"/>"/>
        </th>
        <sec:authorize access="hasRole('ROLE_ADMIN')">     
         <th>
          <spring:message code="user.accessLevel"/> <img src="${baseUrl}/<spring:message code="help.path"/>" alt="<spring:message code="user.accessLevel.description"/>"/>
         </th>
         <th>
          <spring:message code="user.accountStatus"/> <img src="${baseUrl}/<spring:message code="help.path"/>" alt="<spring:message code="user.accountStatus.description"/>"/>
         </th>
        </sec:authorize>
        <th>
         <spring:message code="user.dateCreated"/> <img src="${baseUrl}/<spring:message code="help.path"/>" alt="<spring:message code="user.dateCreated.description"/>"/>
        </th>
        <th>
        </th>
       </tr>
      </thead>
      <tbody> 
       <tr>
        <td>
         <c:choose>
          <c:when test="${user.userName eq authUserName}">
           <c:out value="${user.userName}" />
		   <input type="hidden" name="userName" value="<c:out value="${user.userName}" />"/>
          </c:when>
          <c:otherwise>
           <form:errors path="userName" cssClass="error" />
           <form:input path="userName"/>
          </c:otherwise>
         </c:choose> 
        </td>
        <td>
         <form:errors path="firstName" cssClass="error" />
         <form:input path="firstName"/>
        </td>
        <td>
         <form:errors path="lastName" cssClass="error" />
         <form:input path="lastName"/>
        </td>
        <td>
         <form:errors path="emailAddress" cssClass="error" />
         <form:input path="emailAddress"/>
        </td>
        <sec:authorize access="hasRole('ROLE_ADMIN')"> 
         <c:choose>
          <c:when test="${user.userName eq authUserName}"> 
           <td>
            <c:choose>
             <c:when test="${user.accessLevel == 2}">
              Administrator
             </c:when>
             <c:otherwise>
              User
             </c:otherwise>
            </c:choose>
			<input type="hidden" name="accessLevel" value="<c:out value="${user.accessLevel}" />"/>
           </td>
           <td>
            <c:choose>
             <c:when test="${user.accountStatus == 1}">
              Enabled
             </c:when>
             <c:otherwise>
              Disabled
             </c:otherwise>
            </c:choose>
			<input type="hidden" name="accountStatus" value="<c:out value="${user.accountStatus}" />"/>
           </td>       
          </c:when>
          <c:otherwise>
           <td>
            <form:select path="accessLevel" items="${accessLevel}" />
           </td>
           <td>
            <form:select path="accountStatus" items="${accountStatus}" />
           </td>
          </c:otherwise>
         </c:choose>    
        </sec:authorize>
        <td>
         <fmt:formatDate value="${user.dateCreated}" type="BOTH" dateStyle="default"/>
		 <input type="hidden" name="dateCreated" value="<c:out value="${user.dateCreated}" />"/>
        </td>
        <td>
         <input type="submit" value="<spring:message code="user.save.changes"/>" />
        </td>
       </tr>
      </tbody>
     </table>
    </form:form> 
   </div> <!-- /.fullWidth -->

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
