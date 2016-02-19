<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
    <title><spring:message code="global.title"/> : <c:out value="${user.fullName}" /> <spring:message code="user.view.title"/> </title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h1><c:out value="${user.fullName}" /> <spring:message code="user.view.title"/> </h1>
       
    <c:choose>
     <c:when test="${error != null}">
      <p class="error"><b><c:out value="${error}" /></b></p>
     </c:when>
    </c:choose>

     <ul class="format thirds">
      <li>      
       <div class="profileKey">
        <spring:message code="userName"/>:
       </div>
       <div class="profileValue">
        <c:out value="${user.userName}" />
       </div>
       <div class="profileKey">    
        <spring:message code="fullName"/>:
       </div>
       <div class="profileValue"> 
        <c:out value="${user.fullName}" />
       </div>
       <div class="profileKey">          
        <spring:message code="emailAddress"/>:
       </div>
       <div class="profileValue">         
        <c:out value="${user.emailAddress}" />
       </div>
      </li>
         
      <sec:authorize access="hasRole('ROLE_ADMIN')">     
       <li>
        <div class="profileKey">
         <spring:message code="accessLevel"/>:
        </div>
        <div class="profileValue">
         <c:choose>
          <c:when test="${user.accessLevel == 2}">
           Administrator
          </c:when>
          <c:otherwise>
           User
          </c:otherwise>
         </c:choose>
        </div>         
        <div class="profileKey">
         <spring:message code="accountStatus"/>:
        </div>
        <div class="profileValue">
         <c:choose>
          <c:when test="${user.accountStatus == 1}">
           Enabled
          </c:when>
          <c:otherwise>
           Disabled
          </c:otherwise>
         </c:choose>
        </div>
       </li>
      </sec:authorize>
      <li>
       <div class="profileKey">
        <spring:message code="dateCreated"/>:
       </div>
       <div class="profileValue">
        <fmt:formatDate value="${user.dateCreated}" type="BOTH" dateStyle="default"/>
       </div>
       <div class="profileKey">
        <spring:message code="dateModified"/>:
       </div>
       <div class="profileValue">
        <fmt:formatDate value="${user.dateModified}" type="BOTH" dateStyle="default"/>
       </div>
      </li>
     </ul> 
     <form action="${baseUrl}/user/edit/<c:out value="${user.userName}" />" method="GET">
      <input type="submit" value="<spring:message code="user.edit.title"/>" />        
     </form>
     <form action="${baseUrl}/user/password/<c:out value="${user.userName}" />" method="GET">
      <input type="submit" value="<spring:message code="user.password.title"/>" />    
     </form>
     <c:choose>
      <c:when test="${user.userName ne authUserName}">
       <form action="${baseUrl}/user/delete/<c:out value="${user.userName}" />" method="GET">
        <input type="submit" value="<spring:message code="user.delete.title"/>" />     
       </form>
      </c:when>
     </c:choose>


<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
