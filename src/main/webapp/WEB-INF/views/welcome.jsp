<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<c:choose>
 <c:when test="${loggedIn}">
  <c:redirect url="/docker/image/list"/>
 </c:when> 
 <c:otherwise> 
 <!DOCTYPE HTML>
  <html>
   <head>
    <title><spring:message code="global.title"/> : Welcome!</title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
   </head>
   <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
    <h3>Welcome!</h3> 
    <!-- left -->
    <div class="left">
     <h5>Getting Started</h5>
    </div> <!-- /.left -->
    <!-- right -->
    <div class="right">
     <h5>About Cloud Control</h5>
    </div><!-- /.right -->
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
   </body>
  </html>
 </c:otherwise>
</c:choose>  

 