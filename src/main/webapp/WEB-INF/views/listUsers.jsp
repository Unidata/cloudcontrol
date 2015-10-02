<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="user.list.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3><spring:message code="user.list.title"/></h3>

   <!-- fullWidth -->
   <div class="fullWidth">        
    <p><spring:message code="user.list.message"/></p>
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
          <spring:message code="user.userName"/>
         </th>
         <th>
          <spring:message code="user.firstName"/>
         </th>
         <th>
          <spring:message code="user.lastName"/>
         </th>
         <th>
          <spring:message code="user.emailAddress"/>
         </th>
         <th>
          <spring:message code="user.accessLevel"/>
         </th>
         <th>
          <spring:message code="user.accountStatus"/>
         </th>
         <th>
          <spring:message code="user.dateCreated"/> 
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
            <input class="action edit" type="submit" value="<spring:message code="user.edit.form.title"/>" />        
           </form>
           <c:choose>
            <c:when test="${user.userName ne authUserName}">
             <form action="${baseUrl}/user/delete" method="POST">
              <input type="hidden" name="userId" value="<c:out value="${user.userId}" />"/>
              <input class="action delete" type="submit" value="<spring:message code="user.delete.form.title"/>"/>        
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
         <spring:message code="user.list.none"/>
        </td>
       </tr>
      </c:otherwise>
     </c:choose>
    </table> 
   </div> <!-- /.fullWidth -->
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
