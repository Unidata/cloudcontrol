<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="denied.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3><spring:message code="denied.title"/></h3>
   <p><spring:message code="denied.message"/></p>
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
