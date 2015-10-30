<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : Login</title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3>Login to Cloud Control</h3> 
   
   <!-- left -->
   <div class="left">
    <c:choose>
     <c:when test="${error != null}">
      <p class="error">
       <b>
		Authentication Error:
        <c:choose>
         <c:when test="${error == 'badCredentials'}">
          Bad credentials provided.  Please Try again.
         </c:when>
         <c:when test="${error == 'accountDisabled'}">
          This account has been disabled.
		  Please contact the administrator of this site for help.
         </c:when>
         <c:otherwise>
          Please contact the administrator of this site for help.
         </c:otherwise>
        </c:choose>
       </b>
      </p>
     </c:when>
    </c:choose>
    
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
   </div> <!-- /.left -->
   <!-- right -->
   <div class="right">
   </div><!-- /.right -->
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
