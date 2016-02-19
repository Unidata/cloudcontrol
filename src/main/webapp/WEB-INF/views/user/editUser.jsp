<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="user.edit.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h1><spring:message code="user.edit.title"/>: <c:out value="${user.fullName}" /></h1>
   <p><spring:message code="required.message"/></p>
   
    <c:choose>
     <c:when test="${error != null}">
      <p class="error"><b><c:out value="${error}" /></b></p>
     </c:when>  
    </c:choose>
    
    <form:form action="${baseUrl}/user/edit" commandName="user" method="POST">
	 <form:hidden path="userId" />
     <ul class="format thirds">   
	  <li>      
	   <label for="userName" class="tooltip" title="<spring:message code="userName.description"/>">
	    <spring:message code="userName"/>* 
	   </label>
	   <form:input path="userName" />
	   <form:errors path="userName" cssClass="error" />
      
	   <label for="fullName" class="tooltip" title="<spring:message code="fullName.description"/>">
	    <spring:message code="fullName"/>* 
	   </label>
	   <form:input path="fullName"/>
	   <form:errors path="fullName" cssClass="error" />
      
	   <label for="emailAddress" class="tooltip" title="<spring:message code="emailAddress.description"/>">
	    <spring:message code="emailAddress"/>* 
	   </label>      
	   <form:input path="emailAddress"/>
	   <form:errors path="emailAddress" cssClass="error" />
	  </li>
      <sec:authorize access="hasRole('ROLE_ADMIN')">     
       <li> 		  
        <label for="accessLevel" class="tooltip" title="<spring:message code="accessLevel.description"/>">
         <spring:message code="accessLevel"/>* 
        </label>
        <form:select path="accessLevel">
         <form:option value="1">User</form:option>
         <form:option value="2">Administrator</form:option>
        </form:select>
        <form:errors path="accessLevel" cssClass="error" />
       
        <label for="accountStatus" class="tooltip" title="<spring:message code="accountStatus.description"/>">
         <spring:message code="accountStatus"/>* 
        </label>
        <form:select path="accountStatus">
         <form:option value="1">Active</form:option>
         <form:option value="0">Disabled</form:option>
        </form:select>
        <form:errors path="accountStatus" cssClass="error" />
       </li>
      </sec:authorize>
	  <li>
	   <a href="${baseUrl}/user/password/${user.userName}">
      <c:choose>
       <c:when test="${user.userName eq authUserName}"> 
	    <spring:message code="user.password.title"/>
       </c:when>
	   <c:otherwise>
	    <spring:message code="user.password.reset.admin"/>
       </c:otherwise>
      </c:choose>
       </a>
      </li>
     </ul> 
     <ul class="format">   
      <li>        
       <input type="submit" value="<spring:message code="user.edit.title"/>" />
      </li>
     </ul> 
    </form:form>

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
