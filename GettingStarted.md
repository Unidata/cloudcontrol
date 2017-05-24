# Getting Started With CloudControl

Want to check out this project? Great!  Use the following steps to get going:

#### 1. Accessing Docker Engine

You will need to have access to an instance Docker Engine running on some machine somewhere (local or remote).  

If you are <a href="https://docs.docker.com/engine/installation/">installing Docker</a> from scratch, Docker runs a variety of <a href="https://docs.docker.com/engine/installation/">platforms</a>.  Follow the appropriate Docker <a href="https://docs.docker.com/engine/installation/">installation instructions</a> for your operating system.

CloudControl communicates with a remote instance of Docker over specified ports (usually 2375 or 2376 by default).  Be sure those ports are open on the machine running Docker Machine.  (Consult your systems administrator if you are unfamiliar with modifying firewall rules, etc.)

NOTE: CloudControl uses this super awesome <a href="https://github.com/docker-java/docker-java">Java Docker API client</a> which follows a precedence of where it gets its configs from.  You enter the Docker client settings in the web application, but if the Docker system environment variables  (<code>DOCKER_HOST</code> and <code>DOCKER_CERT_PATH</code>) are visible to the user running the servlet container, the Java Docker API client will glom onto the environment variable settings and use them first.

#### 2. <a href="https://github.com/Unidata/cloudcontrol">Download</a> CloudControl source code from GitHub.
 
#### 3. Modify <code>applications.properties</code>
Find the <code>src/main/webapp/WEB-INF/classes/applications.properties</code> file and make any modifications to the <code>cloudcontrol.home</code> as needed.
 
A good idea is to place <code>cloudcontrol.home</code> in the default servlet container home directory. (This directory needs to survive any upgrades to the WAR file, so be mindful of where it resides.)
 
#### 4. Modify <code>messages.properties</code>
Find the <code>src/main/webapp/WEB-INF/classes/messages.properties</code> file and make any modifications to the  <code>global.title</code> and <code>help.message</code> values as needed.  This will customize the main title on the website and the help message displayed during errors. 
 
Leave the validation contents of this file alone.
 
#### 5. Modify the content of <code>src/main/webapp/WEB-INF/views/EDIT</code> to customize various aspects of the website.

* <code>aboutContent.jspf</code> contains information about a project (presumably) and will be displayed in the left column before login.
 
* <code>footerContent.jspf</code> contains content displayed at the bottom of the page.
 
* <code>gettingStartedContent.jspf</code> contains information about how to get started with the project (presumably) and will be displayed in the right column before login.
 
* <code>infoContent.jspf</code> contains links that will be displayed at the top-right corner of the website (question mark dropdown), and in the user navigation menu.
 
#### 6. <a href="https://github.com/Unidata/cloudcontrol/blob/master/BUILD.md">Build</a> the WAR file.

Follow the instructions in the <a href="https://github.com/Unidata/cloudcontrol/blob/master/BUILD.md">BUILD</a> file and then deploy the WAR file to your servlet container.
 
#### 7. Test away!

Some functionality doesn't exist yet, but it will shortly. Default login for the admin is as follows:
 
    login: admin
    password: changeme

  
Feel free to send us comments at <a href="mailto:support@unidata.ucar.edu">support@unidata.ucar.edu</a>

 Be sure to check out the <a href="https://github.com/Unidata/cloudcontrol/issues">GitHub issues</a> associated with this project for an idea of the TODO list to see if something on your want-list is there. 



