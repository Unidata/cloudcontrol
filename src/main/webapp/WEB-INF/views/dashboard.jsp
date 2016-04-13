<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<%-- Only seen when user is logged in.  Main dashboard/page that displays available tasks.  --%>
 <!DOCTYPE HTML>
  <html>
   <head>
    <title>
     <spring:message code="global.title"/> : 
     <sec:authorize access="hasRole('ROLE_ADMIN')"> 
      Administrator
     </sec:authorize>
     <sec:authorize access="hasRole('ROLE_USER')">   
      User
    </sec:authorize> 
    Dashboard      
   </title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
   </head>
   <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
    <h1>
     <sec:authorize access="hasRole('ROLE_ADMIN')"> 
      Administrator
     </sec:authorize>
     <sec:authorize access="hasRole('ROLE_USER')">   
      User
     </sec:authorize> 
     Dashboard      
    </h1> 
    
    <%-- Administrator Interface 
         if configured:
         - show 
         if not configured:
         - show warning with link to docker server configuration interface:
          - add, edit, delete docker server
           - name
           - host 
           - port
           - cert path
           - auto detect
           
           - add, edit, delete image
           
           
           users
    
    --%>
    <sec:authorize access="hasRole('ROLE_ADMIN')"> 
<%@ include file="/WEB-INF/views/jspf/admin.jspf" %>
     <div id="doubleColumn">
      <c:choose>
       <c:when test="${not empty serverVersion}">   
        <%@ include file="/WEB-INF/views/docker/server/version.jspf" %>
       </c:when>
       <c:when test="${not empty serverInfo}">   
        <%@ include file="/WEB-INF/views/docker/server/info.jspf" %>
       </c:when>
       <c:when test="${not empty imageList}">   
        <%@ include file="/WEB-INF/views/docker/image/listImages.jspf" %>
       </c:when>
       <c:otherwise>
              nope
       </c:otherwise>
      </c:choose>
     </div><!-- /.doubleColumn -->
    </sec:authorize>
    
    
    <%-- User Interface --%>
    <sec:authorize access="hasRole('ROLE_USER')">          
    <%@ include file="/WEB-INF/views/docker/image/listImages.jspf" %>
    </sec:authorize> 

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
   </body>
  </html>


 