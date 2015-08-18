<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="user.list.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3><spring:message code="user.view.title"/>: <c:out value="${user.userName}" /></h3>
   <p><spring:message code="user.view.message"/></p>

   <h3><spring:message code="user.list.title"/></h3>
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
         <spring:message code="user.fullName"/>
        </th>
        <th>
         <spring:message code="user.emailAddress"/>
        </th>
        <th>
         <spring:message code="user.dateCreated"/> 
        </th>
       </tr>
      </thead>
      <tbody>
       <c:forEach items="${users}" var="user">    
        <tr>
         <td>
          <a href="${baseUrl}/user/<c:out value="${user.userName}" />">
           <c:out value="${user.userName}" />
          </a>
         </td>
         <td>
          <a href="${baseUrl}/user/<c:out value="${user.userName}" />">
          <c:out value="${user.fullName}" />
          </a>
         </td>
         <td>
          <a href="${baseUrl}/user/<c:out value="${user.userName}" />">
          <c:out value="${user.emailAddress}" />
          </a>
         </td>
         <td>
          <fmt:formatDate value="${user.dateCreated}" type="BOTH" dateStyle="default"/>
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
