# Run CloudControl in Docker

This directory contains the necessary files to build and run CloudControl in  a docker-based tomcat instance.  This is a utility for ease-of-use and may eventually be removed.

The following steps are used to build CloudControl, build the docker container, then run the docker container.

**Note**: Run all commands from the root cloudcontrol/ directory.


#### Build CC

    $ ./gradlew war

#### Build Docker Container

    $ docker build -t unidata/cloudcontrol -f docker/Dockerfile.cloudcontrol .

#### Run Docker Container

    $ docker run --rm -it -P unidata/cloudcontrol

Once the docker container is running, use `docker ps` to see what the port mapping is for Tomcat.
