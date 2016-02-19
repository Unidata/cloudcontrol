<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="user.list.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>

   <h1><spring:message code="user.list.title"/></h1>

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
          <spring:message code="userName"/>
         </th>
         <th>
          <spring:message code="fullName"/>
         </th>
         <th>
          <spring:message code="emailAddress"/>
         </th>
         <th>
          <spring:message code="accessLevel"/>
         </th>
         <th>
          <spring:message code="accountStatus"/>
         </th>
         <th class="hidebg">
         </th>
        </tr>
       </thead>
       <tbody>
        <c:forEach items="${users}" var="user">    
         <tr>
          <td>
           <a href="${baseUrl}/user/view/<c:out value="${user.userName}" />"> 
            <c:out value="${user.userName}" />
           </a>
          </td>
          <td>
           <a href="${baseUrl}/user/view/<c:out value="${user.userName}" />"> 
            <c:out value="${user.fullName}" />
           </a>
          </td>
          <td>
           <a href="${baseUrl}/user/view/<c:out value="${user.userName}" />"> 
            <c:out value="${user.emailAddress}" />
           </a>
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
           <form action="${baseUrl}/user/edit/<c:out value="${user.userName}" />" method="GET">
            <input type="submit" value="<spring:message code="crud.edit"/>" />        
           </form>
           <form action="${baseUrl}/user/password/<c:out value="${user.userName}" />" method="GET">
            <input type="submit" value="<spring:message code="user.password.title"/>" />        
           </form>
           <c:choose>
            <c:when test="${user.userName ne authUserName}">
             <form action="${baseUrl}/user/delete/<c:out value="${user.userName}" />" method="GET">
              <input type="submit" value="<spring:message code="crud.delete"/>"/>        
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

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
