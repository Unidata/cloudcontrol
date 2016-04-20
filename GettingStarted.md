#Getting Started With Cloud Control

Want to check out this project? Great!  Use the following steps to get going:

#### 1. Confirm Docker is installed and running.

Docker needs to be installed and running on your local system for this to work.  Use the appropriate link below for Docker (or Docker Machine) installation and operating instructions based on your operating system: 

* <a href="https://docs.docker.com/linux/">Linux (Ubuntu)</a>
* <a href="https://docs.docker.com/windows/">Windows</a>
* <a href="https://docs.docker.com/mac/">Mac OS X</a>

WARNING: Cloud Control uses this super awesome <a href="https://github.com/docker-java/docker-java">Java Docker API client</a> which follows a precedence of where it gets its configs from.  You enter the Docker client settings in the web application, but if the Docker system environment variables  (<code>DOCKER_HOST</code> and <code>DOCKER_CERT_PATH</code>) are visible to the user running the servlet container, the Java Docker API client will glom onto the environment variable settings and use them first.

#### 2. <a href="https://github.com/Unidata/cloudcontrol">Download</a> Cloud Control source code from GitHub.
 
#### 3. Modify <code>applications.properties</code>
Find the <code>src/main/webapp/WEB-INF/classes/applications.properties</code> file and make any modifications to the  <code>servletcontainer.home</code> and <code>cloudcontrol.home</code> values as needed.  
 
By default, Cloud Control is configured to place <code>cloudcontrol.home</code> in the servlet container home directory. (This directory needs to survive any upgrades to the WAR file, so be mindful of where it resides.)
 
#### 4. Modify <code>messages.properties</code>
Find the <code>src/main/webapp/WEB-INF/classes/messages.properties</code> file and make any modifications to the  <code>global.title</code> and <code>help.message</code> values as needed.  This will customize the main title on the website and the help message displayed during errors. 
 
Leave the validation contents of this file alone.
 
 
#### 5. Modify the content of <code>src/main/webapp/WEB-INF/views/EDIT</code> to customize various aspects of the website.

* <code>aboutContent.jspf</code> contains information about a project (presumably) and will be displayed in the left column before login.
 
* <code>footerContent.jspf</code> contains content displayed at the bottom of the page.
 
* <code>gettingStartedContent.jspf</code> contains information about how to get started with the project (presumably) and will be displayed in the right column before login.
 
* <code>infoContent.jspf</code> contains links that will be displayed at the top-right corner of the website (question mark dropdown), and in the user navigation menu.
 
#### 6. Build the WAR file.

Run the following command from the Cloud Control installation directory: <code>./gradlew war</code> to produce a WAR file. 

Note: running the above command will build the Cloud Control web application as <code>ROOT.war</code>.  If you wish to change the name, modify the <code>build.gradle</code> file.

It's on the TODO list to embed this app in a servlet container that you can just download and run, negating the need to run a servlet container like Tomcat.  (Not that there's anything wrong with Tomcat.)
 
#### 7. Test away!

Some functionality doesn't exist yet, but it will shortly. Feel free to send us comments at <a href="mailto:support@unidata.ucar.edu">support@unidata.ucar.edu</a>
 
 Be sure to check out the <a href="https://github.com/Unidata/cloudcontrol/issues">GitHub issues</a> associated with this project for an idea of the TODO list to see if something on your want-list is there. 



