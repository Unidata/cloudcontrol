#Building CloudControl

####Build WAR file using gradle

Use the gradle wrapper (gradlew, gradlew.bat) included in this repository for building CloudControl:
  
    $ ./gradlew war

This will result in the creation of a war file in the <code>build/lib/</code> directory. 

*Note*: by default, the CloudControl web application WAR file is built as: <code>ROOT.war</code>

If you wish to change the name, modify the <code>archiveName</code> value in the  <code><a href="https://github.com/Unidata/cloudcontrol/blob/master/build.gradle">build.gradle</a></code> file.

####Clean-up previous builds

To clean-up any previous builds of CloudControl, run:

    $ ./gradlew clean

####Build the docs

CloudControl uses Doxygen for its documentation.  You will need to have it installed on your system before running the following:

    $ doxygen Doxyfile.cloudcontrol

This will generate the documents in a <code>html/</code> directory.


---



Please send any questions or comments about CloudControl to: <a href="mailto:support@unidata.ucar.edu">support@unidata.ucar.edu</a>
