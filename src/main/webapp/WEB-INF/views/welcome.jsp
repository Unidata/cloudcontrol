<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<%-- Seen if user is not logged in.  Displays general information about the project. The default index page gets redirected to here.  --%>
<c:choose>
 <c:when test="${loggedIn}">
  <c:redirect url="${baseUrl}/dashboard"/>
 </c:when> 
 <c:otherwise> 
  <!DOCTYPE HTML>
   <html>
    <head>
     <title>
      <spring:message code="global.title"/> : 
      <c:choose>
       <c:when test="${action eq 'register'}">     
        Register
       </c:when>
       <c:otherwise>
        Welcome!
       </c:otherwise>
      </c:choose>
     </title>
     <%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
    </head>
    <body> 
     <%@ include file="/WEB-INF/views/jspf/header.jspf" %>
     
     <h1>
      <c:choose>
       <c:when test="${action eq 'register'}">     
        Register
       </c:when>
       <c:otherwise>
        Welcome!
       </c:otherwise>
      </c:choose>
     </h1> 
     
     <div id="leftColumn" class="dec">  
      <%@ include file="/WEB-INF/views/EDIT/aboutContent.jspf" %>
     </div><!-- /.leftColumn -->
     <div id="doubleColumn">
      <c:choose>
       <%-- User registration --%>
       <c:when test="${action eq 'register'}">     
        <%@ include file="/WEB-INF/views/user/createUser.jspf" %>
       </c:when>
       <c:otherwise>
        <%-- Getting started page --%>
        <%@ include file="/WEB-INF/views/EDIT/gettingStartedContent.jspf" %>
       </c:otherwise>
      </c:choose>
     </div><!-- /.doubleColumn -->
     
     <%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
    </body>
   </html>
 </c:otherwise>
</c:choose>  

 