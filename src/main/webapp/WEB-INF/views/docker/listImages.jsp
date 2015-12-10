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
           <sec:authorize access="hasRole('ROLE_ADMIN')">          
            <th>
             Virtual Size
            </th>
           </sec:authorize> 
           <th>
            Status
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
            <sec:authorize access="hasRole('ROLE_ADMIN')">     
             <td>
              <c:choose>
               <c:when test="${image.virtualSize > 1000000}">
                <c:set var="vSize" value="${image.virtualSize / 1000000}"/>             
                 <fmt:formatNumber value="${vSize}" type="number" maxFractionDigits="0" /> MB
               </c:when>
               <c:when test="${image.virtualSize > 1000}">
                <c:set var="vSize" value="${image.virtualSize / 1000}"/>              
                <fmt:formatNumber value="${vSize}" type="number" maxFractionDigits="0" /> KB
               </c:when>
               <c:otherwise>
                <c:out value="${image.virtualSize}" />  B
               </c:otherwise>
              </c:choose>                      
             </td>
		    </sec:authorize> 
            <td>
             <c:choose>
              <c:when test="${fn:length(statusMap) gt 0}">              
               <c:forEach var="status" items="${statusMap}"> 
                <c:choose>
                 <c:when test="${image.id == status.key}">
                  <c:out value="${status.value}" />
                 </c:when>
                 <c:otherwise>
                  Inactive
                 </c:otherwise>
                </c:choose> 
               </c:forEach>
              </c:when>
              <c:otherwise>
               Inactive
              </c:otherwise>
             </c:choose>
            </td>
            <td>
				
             <c:choose>
              <c:when test="${fn:length(statusMap) gt 0}">              
               <c:forEach var="status" items="${statusMap}"> 
                <c:choose>
                 <c:when test="${image.id == status.key}">
                  <a href="http://192.168.99.100:6080/" target="_blank" class="open">
                   View      
                  </a>
                  <form action="${baseUrl}/docker/image/<c:out value="${image.id}/stop" />/" method="GET">
                   <input class="action" type="submit" value="Stop" />        
                  </form>

                 </c:when>
                 <c:otherwise>
                  <form action="${baseUrl}/docker/image/<c:out value="${image.id}/start" />/" method="GET">
                   <input class="action" type="submit" value="Start" />        
                  </form>
                 </c:otherwise>
                </c:choose> 
               </c:forEach>
              </c:when>
              <c:otherwise>
                <form action="${baseUrl}/docker/image/<c:out value="${image.id}/start" />/" method="GET">
                 <input class="action" type="submit" value="Start" />        
                </form>
              </c:otherwise>
             </c:choose>
             <sec:authorize access="hasRole('ROLE_ADMIN')">           
              <form action="${baseUrl}/docker/image/<c:out value="${image.id}" />" method="GET">
               <input class="action" type="submit" value="Inspect" />        
              </form>
             </sec:authorize>
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
