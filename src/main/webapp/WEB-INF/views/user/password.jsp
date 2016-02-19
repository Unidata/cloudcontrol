<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="user.password.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h1><spring:message code="user.password.title"/>: <c:out value="${user.fullName}" /> </h1>
   <p><spring:message code="required.message"/></p>
    <c:choose>
     <c:when test="${error != null}">
      <p class="error"><b><c:out value="${error}" /></b></p>
     </c:when>  
    </c:choose>
    
    <form:form action="${baseUrl}/user/password" commandName="user" method="POST">
     <ul class="format thirds">   
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
     </ul>
	 <ul class="format"> 
      <li>
	   <input type="submit" value="<spring:message code="user.password.title"/>" />
      </li>
     </ul> 
    </form:form>

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
