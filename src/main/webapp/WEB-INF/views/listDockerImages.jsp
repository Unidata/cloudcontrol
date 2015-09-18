<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/> : <spring:message code="docker.list.images.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>

   <h3><spring:message code="docker.list.images.title"/></h3>
   <p><spring:message code="docker.list.images.message"/></p>


   <c:choose>
    <c:when test="${error != null}">
     <p class="error"><b><c:out value="${error}" /></b></p>
    </c:when>
   </c:choose>
   
    <table class="list"> 
     <c:choose>
      <c:when test="${fn:length(dockerImages) gt 0}">
       <thead>
        <tr>
         <th>
          <spring:message code="docker.image.repository"/>
         </th>
         <th>
          <spring:message code="docker.image.tag"/>
         </th>
         <th>
          <spring:message code="docker.image.imageId"/>
         </th>
         <th> 
          <spring:message code="docker.image.created"/>
         </th>
         <th>
          <spring:message code="docker.image.virtualSize"/> 
         </th>
	 	<th>
          <spring:message code="form.action.title"/> 
         </th>
        </tr>
       </thead>
       <tbody>
        <c:forEach items="${dockerImages}" var="dockerImage">    
         <tr>
          <td>
           <c:out value="${dockerImage.repository}" />
          </td>
          <td>
           <c:out value="${dockerImage.tag}" />
          </td>
          <td>
           <c:out value="${dockerImage.imageId}" />
          </td>
          <td>
           <c:out value="${dockerImage.created}" />	   
          </td>
          <td>
           <c:out value="${dockerImage.virtualSize}" />	   
          </td>
          <td>
		   <input type="submit" value="run" />
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

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
