<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : Viewing User : <c:out value="${user.userName}" /></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
   <h3>Viewing User: <c:out value="${user.userName}" /></h3>
   

   <!-- fullWidth -->
   <div class="fullWidth">       
	   
    <c:choose>
     <c:when test="${error != null}">
      <p class="error"><b><c:out value="${error}" /></b></p>
     </c:when>
    </c:choose>

    <table>    
     <thead>
      <tr>
       <th>
        User Name
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
        Date Created
       </th>
       <th>
       </th>
      </tr>
     </thead>
     <tbody> 
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
        <fmt:formatDate value="${user.dateCreated}" type="BOTH" dateStyle="default"/>
       </td>
       <td>
        <form action="${baseUrl}/user/edit/<c:out value="${user.userName}" />" method="GET">
         <input type="submit" value="Edit" />        
        </form>
	   </td>
      </tr>
     </tbody>
    </table> 
   </div> <!-- /.fullWidth -->

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
