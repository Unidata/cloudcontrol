<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : Edit User : <c:out value="${user.userName}" /></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3>Edit User: <c:out value="${user.userName}" /></h3>
   

   <!-- fullWidth -->
   <div class="fullWidth">        

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
         User Name
        </th>
        <th>
         First Name
        </th>
        <th>
         Last Name
        </th>
        <th>
         Email Address
        </th>
        <sec:authorize access="hasRole('ROLE_ADMIN')">     
         <th>
		  Access Level
         </th>
         <th>
          Account Status
         </th>
        </sec:authorize>
        <th>
         Date Created
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
         <input type="submit" value="Save Changes" />
        </td>
       </tr>
      </tbody>
     </table>
    </form:form> 
   </div> <!-- /.fullWidth -->

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>