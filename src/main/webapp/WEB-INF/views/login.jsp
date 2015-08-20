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

   <form action="${baseUrl}/j_spring_security_check" method="POST">
    <ul class="format">   
     <li>
      <label>
       User Name:<br />
       <input type="text" name="j_username" value="" />
      </label>
     </li>
     <li>
      <label>
       Password:<br />
       <input type="password" name="j_password" value="" />
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
