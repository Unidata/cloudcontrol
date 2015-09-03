<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="login.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3><spring:message code="login.title"/></h3>
   <div class="error">
    <p><c:out value="${error}" /></p>
   </div>
   <c:url var="loginUrl" value="/j_spring_security_check" />
   <form action="${loginUrl}" method="POST">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <ul class="format">   
     <li>
      <label>
       User Name:<br />
       <input type="text" name="userName" value="" />
      </label>
     </li>
     <li>
      <label>
       Password:<br />
       <input type="password" name="password" value="" />
      </label>
     </li>
     <li>
      <input type="submit" value="Login" />
     </li> 
    </ul>
   </form>
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
