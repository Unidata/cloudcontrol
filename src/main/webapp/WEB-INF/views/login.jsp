<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="login"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   
    <h1><spring:message code="login"/></h1> 
    <c:choose>
     <c:when test="${error != null}">
      <p class="error">
       <b>
        <spring:message code="authError"/>
        <c:choose>
         <c:when test="${error == 'badCredentials'}">
          <spring:message code="badCredentials"/>
         </c:when>
         <c:when test="${error == 'accountDisabled'}">
          <spring:message code="accountDisabled"/>
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
    
    <form action="/j_spring_security_check" method="POST">
     <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
     <ul class="format">   
      <li>
       <label for="userName">
        <spring:message code="userName"/>
       </label>
       <input type="text" id="userName" name="userName" value="" />
      </li>
      <li>
       <label for="password">
        <spring:message code="password"/>
       </label>
       <input type="password" id="password" name="password" value="" />
      </li>
      <li>
       <input type="submit" value="Login" /> 
       <a href="">
        <spring:message code="forgot"/>
       </a>
      </li> 
     </ul>
    </form>

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
