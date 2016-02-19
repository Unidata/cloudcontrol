<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : Access Denied</title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3>Access Denied</h3>
   <!-- fullWidth -->
   <div class="fullWidth">    
    <p>You do not have permission to access this page.  </p>
   </div> <!-- /.fullWidth -->
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
