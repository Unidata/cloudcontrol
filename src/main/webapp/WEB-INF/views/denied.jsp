<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<%-- Access denied page. --%>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : Access Denied</title>
   <%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
   <%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h1>Access Denied</h1>
   <p>You do not have permission to access this page.  </p>
   <%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
