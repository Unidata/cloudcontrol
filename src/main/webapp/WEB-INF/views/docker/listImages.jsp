<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
<html>
 <head>
  <title><spring:message code="global.title"/> : <spring:message code="docker.list.images.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
 </head>
 <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>

   <h3><c:out value="${fn:length(images)}" /> <spring:message code="docker.list.images.title"/></h3>
  
   <!-- left -->
   <div class="left">
    <c:choose>
     <c:when test="${error != null}">
      <p class="error"><b><c:out value="${error}" /></b></p>
     </c:when>
    </c:choose>
        
    <p><spring:message code="docker.list.images.message"/></p>
     <table class="list"> 
      <c:choose>
        <c:when test="${fn:length(images) gt 0}">
         <thead>
          <tr>
            <th>
             <spring:message code="docker.image.repoTags"/>
            </th>
            <th>
             <spring:message code="docker.image.id"/>
            </th>
            <th> 
             <spring:message code="docker.image.created"/>
            </th>
            <th>
             <spring:message code="docker.image.virtualSize"/> 
            </th>
            <th>
             <spring:message code="docker.image.size"/> 
            </th>
          </tr>
         </thead>
         <tbody>
          <c:forEach items="${images}" var="image">     
            <tr>
             <td>
              <a href="${baseUrl}/docker/image/<c:out value="${image.id}" />">
              <c:forEach var="tag" items="${image.repoTags}">    
               <c:out value="${tag}" /><br/>
              </c:forEach>
              </a>
             </td>
             <td>
              <a href="${baseUrl}/docker/image/<c:out value="${image.id}" />">
               <c:out value="${image.id}" />
              </a>
             </td>
             <td>
              <a href="${baseUrl}/docker/image/<c:out value="${image.id}" />">
               <c:out value="${image.created}" />    
              </a>                 
             </td>
             <td>
              <a href="${baseUrl}/docker/image/<c:out value="${image.id}" />">
               <c:out value="${image.virtualSize}" />    
              </a>                 
             </td>
             <td>
              <a href="${baseUrl}/docker/image/<c:out value="${image.id}" />">
               <c:out value="${image.size}" />      
              </a> 
             </td>
            </tr>
          </c:forEach>
         </tbody>
        </c:when>
        <c:otherwise>
         <tr>
          <td>
           <spring:message code="docker.list.images.none"/>
          </td>
         </tr>
        </c:otherwise>
      </c:choose>
     </table> 
   </div> <!-- /.left -->
   <!-- right -->
   <div class="right">
    <h5><spring:message code="docker.list.images"/></h5>
    <ul>
     <li><a href="${baseUrl}/docker/image/search"><spring:message code="docker.list.images.option.search"/></a></li>
     <li><spring:message code="docker.list.images.option.create"/></li>
     <li><spring:message code="docker.list.images.option.build"/></li>
     <li><spring:message code="docker.list.images.option.pull"/></li>
    </ul>
   </div><!-- /.right -->
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
