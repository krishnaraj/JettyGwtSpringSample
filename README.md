# JettyGwtSpringSample Web Application

## Building
* Install Gradle - http://www.gradle.org/docs/current/userguide/userguide_single.html#installation
* Install GWT eclipse plugin (optional) - http://www.gwtproject.org/usingeclipse.html#installing
* Run: 'gradle eclipse' - this should create the .project, .classpath files and download the necessary jars
* Run: 'gradle gwtc' - compiles gwt related stuff
* To start the app type 'gradle devmode' or just 'gradle run' 

## Running from eclipse
* To run the application in devmode, create a run configuration with the main class as com.google.gwt.dev.DevMode
* Pass the following as program arguments:
-remoteUI "${gwt_remote_ui_server_port}:${unique_id}" -startupUrl JettyGwtSpringSampleApp.html -logLevel INFO -codeServerPort 9997 -port 8888 -war <project_root>/war com.example.JettyGwtSpringSampleApp -server com.example.devmode.EmbeddedDevMode
* Add 'src' and 'dev-container' folders under 'User Entries' in classpath, should be before the default classpath
