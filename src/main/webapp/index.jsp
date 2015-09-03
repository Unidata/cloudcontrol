<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<!DOCTYPE HTML>
 <html>
  <head>
   <title><spring:message code="global.title"/></title>
<%@ include file="/WEB-INF/views/jspf/resources.jspf" %>
  </head>
  <body> 
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
      <c:choose>
       <c:when test="${loggedIn}">
<p><a href="${baseUrl}/user/${authUserName}"><spring:message code="link.account.title"/> ${authUserName}</a></p>
       </c:when>
      </c:choose>
    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec suscipit malesuada lacus, in pharetra lectus suscipit eu. Donec aliquet vitae ex ac auctor. Quisque finibus hendrerit ultrices. Donec egestas pellentesque commodo. Ut tincidunt ullamcorper sem eu porta. In a augue arcu. Vestibulum nisl ex, sagittis a diam eget, accumsan dignissim augue. Integer sit amet mattis felis, id suscipit nibh. Pellentesque malesuada, est quis laoreet ornare, leo sapien fringilla odio, ac accumsan mauris lorem vitae neque. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Etiam congue nunc ante. Etiam placerat eros et nunc imperdiet congue. Sed tempus accumsan ex, eu fringilla sem sagittis eu. Mauris eget urna nisi. Vivamus tempor maximus ultricies. Sed sit amet nisi dui.</p>
<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>
  </body>
 </html>
