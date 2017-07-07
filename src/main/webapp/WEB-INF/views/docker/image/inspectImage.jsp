<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>
<%-- This view is to show the results of an "Inspect Image" command.  (admin and authenticated users). --%>
     <script>
        $(document).ready(function() {
            $("table tbody tr:nth-child(odd)").addClass("odd");
        });
     </script>

     <c:choose>
      <c:when test="${error ne null}">
     <c:out value="${error}" />
    </c:when>
    <c:otherwise>
     <table class="list">
      <tbody>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="The <b>repository</b> can be a combination of a <b>Docker Hub account name</b> and an <b>image name</b>.  The <b>tag</b> is typically a <b>version name</b> or an <b>identifying implementation</b> of the image.">
          Repository/Tag
        </div>
        </td>
        <td>
         <c:forEach var="tag" items="${image.repoTags}">
          <c:out value="${tag}" /><br/>
         </c:forEach>
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="This is the <b>unique identifier</b> of the image.">
          ID
         </div>
        </td>
        <td>
        <c:out value="${image.id}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="A link to the <b>identifier</b> of the <b>parent image</b>. It is very common for an image to have a defined parent.">
          Parent ID
         </div>
        </td>
        <td>
         <c:out value="${image.parentId}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="The <b>date and time</b> the image was created.">
          Date Created
         </div>
        </td>
        <td>
         <c:out value="${image.created}"/>
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="The <b>size</b> of the image.">
          Virtual Size
         </div>
        </td>
        <td>
         <c:out value="${image.virtualSize}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="The <b>size</b> of the image.">
         Size
         </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.size}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="The version of Docker <b>used to create the image</b> is stored in this value.">
          Docker Version
         </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.dockerVersion}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="The <b>person or organization</b> who authored the image.">
          Author
         </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.author}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="Type of <b>system</b> of the server the image runs on (e.g.: 64-bit, x64, or x86_64).">
          Architecture
         </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.arch}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="Type of <b>operating system</b> of the server the image uses (e.g: linux).">
          Operating System
         </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.os}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="Any  <b>comments</b> about the image provided by the author.">
          Comment
         </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.comment}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="A container identifier is for an image not a container. This container identifier is a <b>temporary container created when the image was built</b>. Docker will create a container during the image construction process, and this identifier is stored in the image data.">
          Container
         </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.container}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="Images that use the Version 2 Registry API or later format have a <b>content-addressable identifier called a digest</b>.">
          Repository Digests
         </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.repoDigests}" />
        </td>
       </tr>
       <tr>
        <td>
         <div class="tooltip" class="tooltip" title="Information about the Docker <b>graph driver</b>, used to store information about the Image.">
          Graph Driver
         </div>
        </td>
        <td>
         <c:out value="${inspectImageResponse.graphDriver}" />
        </td>
       </tr>
      </tbody>
     </table>
    </c:otherwise>
   </c:choose>
