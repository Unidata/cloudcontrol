# CloudControl    <img src="https://github.com/Unidata/cloudcontrol/blob/master/src/main/webapp/resources/images/header.png" alt="CloudControl   "/>

[![Travis Build Status](https://travis-ci.org/Unidata/cloudcontrol.svg?branch=master)](https://travis-ci.org/Unidata/cloudcontrol)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/8628/badge.svg)](https://scan.coverity.com/projects/cloudcontrol)
       
### About CloudControl   

CloudControl is a web-based dashboard which provides administrative controls to deploy and manage Docker images in the cloud.  In particular, the web interface makes it easy for users unfamiliar with Docker to utilize Docker docker-based technologies. Users of CloudControl do not need to possess in-depth knowledge of Docker commands.

What sets CloudControl apart from other Docker/Cloud technologies is user management layer with users, roles, and permissions.  The CloudControl administrator can customize the user environment to grant access to predetermined images and Docker engines, and configure/limit the Docker commands the user is allowed to perform.
	 
CloudControl    is built on/utilizes the following OpenSource technologies:

* <a href="http://www.docker.com/">Docker</a>, <a href="http://docs.docker.com/machine/install-machine/">Docker Machine</a>, <a href="http://docs.docker.com/engine/reference/api/docker_remote_api/">Docker Remote API</a>
* <a href="https://github.com/docker-java/docker-java">docker-java client</a>
* <a href="http://projects.spring.io/spring-framework/">Spring Framework</a>, <a href="http://projects.spring.io/spring-security/">Spring Security</a>
* <a href="https://db.apache.org/derby/">Apache Derby</a>
* <a href="https://jquery.com/">JQuery</a>

CloudControl    is early in the development stage and is Proof-Of-Concept.  The Docker-based technologies it relies on are constantly evolving requiring frequent refactoring. Therefore, this software should be considered a pre-alpha release.  Please read the <a href="https://github.com/Unidata/cloudcontrol/blob/master/LICENSE">license</a> before use.

Send questions or comments about this project to: <a href="mailto:support@unidata.ucar.edu">support@unidata.ucar.edu</a>
