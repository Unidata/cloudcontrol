<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<c:choose>
 <c:when test="${loggedIn}">
  <c:redirect url="/docker/image/list"/>
 </c:when> 
 <c:otherwise> 
 <!DOCTYPE HTML>
  <html>
   <head>
    <title><spring:message code="global.title"/> : <spring:message code="welcome"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
   </head>
   <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
    <h1><spring:message code="welcome"/></h1> 
    <div id="doubleColumn">
<%@ include file="/WEB-INF/views/jspf/gettingStarted.jspf" %>
    </div><!-- /.doubleColumn -->
    <div id="rightColumn" class="dec">
<%@ include file="/WEB-INF/views/jspf/about.jspf" %>
    </div><!-- /.rightColumn -->
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
   </body>
  </html>
 </c:otherwise>
</c:choose>  

 