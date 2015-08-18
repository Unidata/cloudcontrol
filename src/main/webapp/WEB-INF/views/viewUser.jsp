<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="user.view.title"/> : <c:out value="${user.userName}" /></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3><spring:message code="user.view.title"/>: <c:out value="${user.userName}" /></h3>
   <p><spring:message code="user.view.message"/></p>

   <c:choose>
    <c:when test="${error != null}">
     <p class="error"><b><c:out value="${error}" /></b></p>
    </c:when>
   </c:choose>

   <table>    
    <thead>
     <tr>
      <th>
       <spring:message code="user.userName"/>
      </th>
      <th>
       <spring:message code="user.fullName"/>
      </th>
      <th>
       <spring:message code="user.emailAddress"/>
      </th>
      <th>
       <spring:message code="user.dateCreated"/> 
      </th>
      <c:choose>
       <c:when test="${loggedIn}">
        <c:choose>
         <c:when test="${user.userName eq authUserName}">
          <th>
           <spring:message code="form.action.title"/>
          </th>
         </c:when>
         <c:otherwise>
          <sec:authorize access="hasRole('ROLE_ADMIN')">
           <th>
            <spring:message code="form.action.title"/>
           </th>
          </sec:authorize>
         </c:otherwise>
        </c:choose>
       </c:when>
      </c:choose> 
     </tr>
    </thead>
    <tbody> 
     <tr>
      <td>
       <c:out value="${user.userName}" />
      </td>
      <td>
       <c:out value="${user.fullName}" />
      </td>
      <td>
       <c:out value="${user.emailAddress}" />
      </td>
      <td>
       <fmt:formatDate value="${user.dateCreated}" type="BOTH" dateStyle="default"/>
      </td>
      <c:choose>
       <c:when test="${loggedIn}">
        <c:choose>
         <c:when test="${user.userName eq authUserName}">
          <td>
           <form action="${baseUrl}/user/update/<c:out value="${user.userName}" />" method="GET">
            <input class="action edit" type="submit" value="<spring:message code="user.update.title"/>" />        
           </form>
           <sec:authorize access="hasRole('ROLE_ADMIN')">
            <form action="${baseUrl}/user/delete" method="POST">
             <input type="hidden" name="userId" value="<c:out value="${user.userId}" />"/>
             <input class="action delete" type="submit" value="<spring:message code="user.delete.title"/>"/>        
            </form>
           </sec:authorize>
          </td>
         </c:when>
         <c:otherwise>
          <sec:authorize access="hasRole('ROLE_ADMIN')">
           <td>
            <form action="${baseUrl}/user/update/<c:out value="${user.userName}" />" method="GET">
             <input class="action edit" type="submit" value="<spring:message code="user.update.title"/>" />        
            </form>
            <form action="${baseUrl}/user/delete" method="POST">
             <input type="hidden" name="userId" value="<c:out value="${user.userId}" />"/>
             <input class="action delete" type="submit" value="<spring:message code="user.delete.title"/>"/>        
            </form>
          </sec:authorize>
         </c:otherwise>
        </c:choose>
       </c:when>
      </c:choose>
     </tr>
    </tbody>
   </table> 

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
