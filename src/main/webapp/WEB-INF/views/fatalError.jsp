<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : Oh Dear! Looks like we encountered a problem!</title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3>Oh Dear! Looks like we encountered a problem!</h3>
   <p>Please contact the administrator of this site for help.</p>
   <pre>
   <c:out value="${message}" />
   </pre>
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
