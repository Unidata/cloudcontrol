<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<%-- General display page to display getting started and about information.  Accessible to anyone. --%>
<!DOCTYPE HTML>
 <html>
  <head>
   <title>
    <spring:message code="global.title"/>
   </title>
   <%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
   <%@ include file="/WEB-INF/views/jspf/header.jspf" %>
    <c:choose>
     <%-- about --%>
     <c:when test="${action eq 'about'}">     
      <%@ include file="/WEB-INF/views/EDIT/aboutContent.jspf" %>
     </c:when>
     <c:otherwise>
      <%-- Getting started page --%>
      <%@ include file="/WEB-INF/views/EDIT/gettingStartedContent.jspf" %>
     </c:otherwise>
    </c:choose>
   <%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>