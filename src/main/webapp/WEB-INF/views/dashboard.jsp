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
    <div id="leftColumn" class="accordion dec">
     <sec:authorize access="hasRole('ROLE_ADMIN')"> 
      <%@ include file="/WEB-INF/views/jspf/adminMenu.jspf" %>
     </sec:authorize>
     <sec:authorize access="hasRole('ROLE_USER')"> 
      <%@ include file="/WEB-INF/views/jspf/userMenu.jspf" %>
     </sec:authorize> 
    </div><!-- /.leftColumn -->
    <div id="doubleColumn">
        
     <c:if test="${error != null}">
      <p class="error">
       <b>  
        <c:out value="${error}"/>  
       </b>
	  </p>
      <p><c:out value="${image.created}"/></p>       
     </c:if>
             
     <c:choose>      
      <%-- View server version (admin only) --%>
      <c:when test="${action eq 'serverVersion'}">   
       <%@ include file="/WEB-INF/views/docker/server/serverVersion.jspf" %>
      </c:when>
    
      <%-- View server info (admin only) --%>
      <c:when test="${action eq 'serverInfo'}">   
       <%@ include file="/WEB-INF/views/docker/server/serverInfo.jspf" %>
      </c:when>
    
      <%-- List images (admin and auth user) --%>
      <c:when test="${action eq 'listImages'}">   
       <%@ include file="/WEB-INF/views/docker/image/listImages.jspf" %>
      </c:when>
      
      <%-- List all users (admin only) --%>
      <c:when test="${action eq 'listUsers'}">   
       <%@ include file="/WEB-INF/views/user/listUsers.jspf" %>
      </c:when>
     
      <%-- View a specific user (admin and auth user) --%>
      <c:when test="${action eq 'viewUser'}">   
       <%@ include file="/WEB-INF/views/user/viewUser.jspf" %>
      </c:when>
     
      <%-- Edit a user (admin and auth user) --%>
      <c:when test="${action eq 'editUser'}">   
       <%@ include file="/WEB-INF/views/user/editUser.jspf" %>
      </c:when>
     
      <%-- Reset user password (admin and auth user) --%>
      <c:when test="${action eq 'resetPassword'}">   
       <%@ include file="/WEB-INF/views/user/resetPassword.jspf" %>
      </c:when>
     
      <%-- User creation (admin only) --%>
      <c:when test="${action eq 'createUser'}">   
       <%@ include file="/WEB-INF/views/user/createUser.jspf" %>
      </c:when>
     
      <%-- Delete a user (admin only) --%>
      <c:when test="${action eq 'deleteUser'}">   
       <%@ include file="/WEB-INF/views/user/deleteUser.jspf" %>
      </c:when>

      <%-- Configure Docker client--%>
      <c:when test="${action eq 'configureClient'}">   
       <%@ include file="/WEB-INF/views/docker/client/configureClient.jspf" %>
      </c:when>
     
      <%-- View Docker client configuration --%>
      <c:when test="${action eq 'viewClientConfig'}">   
       <%@ include file="/WEB-INF/views/docker/client/viewClientConfig.jspf" %>
      </c:when>
     
      <%-- Edit Docker client configuration --%>
      <c:when test="${action eq 'editClientConfig'}">   
       <%@ include file="/WEB-INF/views/docker/client/editClientConfig.jspf" %>
      </c:when>
     
      <%-- Edit Docker client configuration --%>
      <c:when test="${action eq 'listClientConfigs'}">   
       <%@ include file="/WEB-INF/views/docker/client/listClientConfig.jspf" %>
      </c:when>
     
      <c:otherwise>
       <%@ include file="/WEB-INF/views/jspf/status.jspf" %>
      </c:otherwise>
     </c:choose>
    </div><!-- /.doubleColumn -->
   <%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>


 