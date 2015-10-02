<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3><spring:message code="global.title"/></h3> 
   <!-- left -->
   <div class="left">
    <c:choose>
     <c:when test="${loggedIn}">
      <sec:authorize access="hasRole('ROLE_ADMIN')">
		  admin stuff:
		<a href="${baseUrl}/user"><spring:message code="user.list.title"/></a>
      </sec:authorize>
     </c:when>
    </c:choose>    
   </div> <!-- /.left -->
   <!-- right -->
   <div class="right">
   </div><!-- /.right -->
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>