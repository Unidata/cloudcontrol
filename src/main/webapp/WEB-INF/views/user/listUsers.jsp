<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : Manage Cloud Control Users</title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3>Manage Cloud Control Users</h3>

   <!-- fullWidth -->
   <div class="fullWidth">        
    <c:choose>
     <c:when test="${error != null}">
      <p class="error"><b><c:out value="${error}" /></b></p>
     </c:when>
    </c:choose>

    <table class="list"> 
     <c:choose>
      <c:when test="${fn:length(users) gt 0}">
       <thead>
        <tr>
         <th>
          User/Login Name
         </th>
         <th>
          First Name
         </th>
         <th>
          Last Name
         </th>
         <th>
          Email Address
         </th>
         <th>
          Access Level 
         </th>
         <th>
          Account Status
         </th>
         <th>
          Date Created
         </th>
         <th>
         </th>
        </tr>
       </thead>
       <tbody>
        <c:forEach items="${users}" var="user">    
         <tr>
          <td>
           <c:out value="${user.userName}" />
          </td>
          <td>
           <c:out value="${user.firstName}" />
          </td>
          <td>
           <c:out value="${user.lastName}" />
          </td>
          <td>
           <c:out value="${user.emailAddress}" />
          </td>
          <td>
           <c:choose>
            <c:when test="${user.accessLevel == 2}">
             Administrator
            </c:when>
            <c:otherwise>
             User
            </c:otherwise>
           </c:choose>
          </td>
          <td>
           <c:choose>
            <c:when test="${user.accountStatus == 1}">
             Enabled
            </c:when>
            <c:otherwise>
             Disabled
            </c:otherwise>
           </c:choose>
          </td>
          <td>
           <fmt:formatDate value="${user.dateCreated}" type="BOTH" dateStyle="default"/>
          </td>
          <td>
           <form action="${baseUrl}/user/edit/<c:out value="${user.userName}" />" method="GET">
            <input class="action" type="submit" value="Edit" />        
           </form>
           <c:choose>
            <c:when test="${user.userName ne authUserName}">
             <form action="${baseUrl}/user/delete" method="POST">
              <input type="hidden" name="userId" value="<c:out value="${user.userId}" />"/>
              <input class="action" type="submit" value="Delete"/>        
             </form>
			</c:when>
		   </c:choose>
          </td>
         </tr>
        </c:forEach>
       </tbody>
      </c:when>
      <c:otherwise>
       <tr>
        <td>
         No users have been created yet.
        </td>
       </tr>
      </c:otherwise>
     </c:choose>
    </table> 
   </div> <!-- /.fullWidth -->
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
