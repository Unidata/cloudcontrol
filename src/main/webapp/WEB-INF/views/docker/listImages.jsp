<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
<html>
 <head>
  <title><spring:message code="global.title"/> : Manage Images in This Docker Instance</title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
 </head>
 <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>

   <h3>Manage Images in This Docker Instance</h3>
  
   <!-- fullWidth -->
   <div class="fullWidth">
    <c:choose>
     <c:when test="${error != null}">
      <p class="error"><b><c:out value="${error}" /></b></p>
     </c:when>
    </c:choose>

     <table class="list"> 
      <c:choose>
        <c:when test="${fn:length(images) gt 0}">
         <thead>
          <tr>
           <th>
            Repository/Tags
           </th>
           <th> 
            Date Created
           </th>
           <th>
            Virtual Size
           </th>
           <th class="hidebg">
           </th>
          </tr>
         </thead>
         <tbody>
          <c:forEach items="${images}" var="image">     
           <tr>
            <td>
             <c:forEach var="tag" items="${image.repoTags}">    
              <c:out value="${tag}" /><br/>
             </c:forEach>
            </td>
            <td>
             <c:set target="${myDate}" property="time" value="${image.created * 1000}"/>    
             <fmt:formatDate type="both" value="${myDate}" />             
            </td>
            <td>
             <c:choose>
              <c:when test="${image.virtualSize > 1000000}">
               <c:set var="vSize" value="${image.virtualSize / 1000000}"/>              
               <fmt:formatNumber value="${vSize}" type="number"/> MB
              </c:when>
              <c:when test="${image.virtualSize > 1000}">
               <c:set var="vSize" value="${image.virtualSize / 1000}"/>              
               <fmt:formatNumber value="${vSize}" type="number"/> KB
              </c:when>
              <c:otherwise>
               <c:out value="${image.virtualSize}" />  B
              </c:otherwise>
             </c:choose>                      
            </td>
			<td>
	         <form action="${baseUrl}/docker/image/<c:out value="${image.id}" />" method="GET">
	          <input class="action inspect" type="submit" value="Inspect" />        
	         </form>
	         <form action="${baseUrl}/docker/image/<c:out value="${image.id}/start" />/" method="GET">
	          <input class="action start" type="submit" value="Start" />        
	         </form>
	         <form action="${baseUrl}/docker/image/<c:out value="${image.id}/history" />/" method="GET">
	          <input class="action history" type="submit" value="History" />        
	         </form>
	         <form action="${baseUrl}/docker/image/<c:out value="${image.id}/container" />/" method="GET">
	          <input class="action container" type="submit" value="Container" />        
	         </form>
	         <form action="${baseUrl}/docker/image/<c:out value="${image.id}/tag" />/" method="GET">
	          <input class="action tag" type="submit" value="Tag" />        
	         </form>
	         <form action="${baseUrl}/docker/image/<c:out value="${image.id}/push" />/" method="GET">
	          <input class="action push" type="submit" value="Push" />        
	         </form>
             <form action="${baseUrl}/docker/image/<c:out value="${image.id}/delete" />/"  method="POST">
              <input class="action delete" type="submit" value="Delete"/>        
             </form>
			</td>
           </tr>
          </c:forEach>
         </tbody>
        </c:when>
        <c:otherwise>
         <tr>
          <td>
           No images are available in this docker instance yet.
          </td>
         </tr>
        </c:otherwise>
      </c:choose>
     </table> 
   </div> <!-- /.fullWidth -->
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
