<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<%-- Login page. --%>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : Login</title>
   <%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
   <%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   
   <h1>Login</h1> 
   <c:choose>
    <c:when test="${error != null}">
     <p class="error">
      <b>
       Authentication Error<br>
       <c:choose>
        <c:when test="${error == 'badCredentials'}">
         Bad login credentials provided.  Please Try again.
        </c:when>
        <c:when test="${error == 'accountDisabled'}">
         This account has been disabled.
         <spring:message code="help.message"/>
        </c:when>
        <c:otherwise>
         <spring:message code="help.message"/>
        </c:otherwise>
       </c:choose>
      </b>
     </p>
    </c:when>
   </c:choose>
    
   <form action="${baseUrl}/j_spring_security_check" method="POST">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <ul class="format">   
     <li>
      <label for="userName">
       Username
      </label>
      <input type="text" id="userName" name="userName" value="" />
     </li>
     <li>
      <label for="password">
       Password
      </label>
      <input type="password" id="password" name="password" value="" />
     </li>
     <li>
      <input type="submit" value="Login" /> 
     </li> 
    </ul>
   </form>
   <%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
