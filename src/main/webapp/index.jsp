<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <c:choose>
    <c:when test="${loggedIn}">
     <p><a href="${baseUrl}/user/${authUserName}"><spring:message code="link.account.title"/> ${authUserName}</a></p>
    </c:when>
   </c:choose>

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
