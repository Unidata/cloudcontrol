<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<%-- Unrecoverable error display. --%>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : Oh Dear! Looks like we encountered a problem!</title>
    <%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
   <%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h1>Oh Dear! Looks like we encountered a problem!</h1>
   <p>Please contact the administrator of this site for help.</p>
   <c:out value="${message}" />
   
   <%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
