<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<c:if test="${not empty action}">
 <c:choose>
  <c:when test="${action == 'create'}">
   <spring:message code="user.create.title" var="title"/>
  </c:when>
  <c:when test="${action == 'register'}">
   <spring:message code="user.register.title" var="title"/>
  </c:when>
  <c:otherwise>
   <c:set var="title" value="submit" />
  </c:otherwise>
 </c:choose>
</c:if>

<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <c:out value="${title}" /></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h1><c:out value="${title}" /></h1>
   
   <p><spring:message code="required.message"/></p>
   
   <c:choose>
    <c:when test="${error != null}">
     <p class="error"><b><c:out value="${error}" /></b></p>
    </c:when>
   </c:choose>

   <form:form action="${baseUrl}/user/${action}" commandName="user" method="POST">
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
     <li>
      <label for="password" class="tooltip" title="<spring:message code="password.description"/>">
       <spring:message code="password"/>* 
      </label>
      <form:password path="password"/>
      <form:errors path="password" cssClass="error" />
      
      <label for="confirmPassword" class="tooltip" title="<spring:message code="confirmPassword.description"/>">
       <spring:message code="confirmPassword"/>* 
      </label>
      <form:password path="confirmPassword"/>
      <form:errors path="confirmPassword" cssClass="error" />
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
    </ul> 
    <ul class="format">   
     <li>        
      <input type="submit" value="<c:out value="${title}" />" />
     </li>
    </ul> 
   </form:form>

   
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
