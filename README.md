# bpm-engine
The BPM engine would be used to execute all the processes deployed on it.

# Downloads

1. Download Activiti from http://activiti.org/download.html
2. Install Tomcat server and MySQL server on your local machine
3. Clone this repository

# MySQL setup

1. Create a new database by running command "create database activiti;" from within MySQL console

# Activiti setup

1. Unzip the downloaded Activiti zip file
2. Unzip the activiti-explorer.war file using ""
3. Go to WEB-INF/classes/ within the activiti-rest directory
4. Edit db.properties to point to your MySQL server and database
5. Edit engine.properties and make all properties except 'create.demo.users' as 'false'
6. Copy the entire activiti-rest directory to webapps directory of your Tomcat installation (Generally found at /var/lib/tomcat7/webapps)
7. Start tomcat7 service, if not already running, by issuing "sudo service tomcat7 start" command.
8. Go to localhost:8080/activiti-explorer and login using kermit/kermit
9. Go to 'Manage' -> 'Groups' and create two new groups with id "alc" and "ro" respectively
10. Go to 'Manage' -> 'Users' and create two new users 'vijay' and 'vaibhav'. Add vijay to group alc and vaibhav to group ro.
11. Go to 'Manage' -> 'Deployments' -> 'Upload New' and upload the file 'app/src/test/resources/labor.bpmn20.xml' from the cloned repository.

# bpm-engine setup

1. Copy the egov.properties file in bpm-engine directory to /egov.properties
2. Edit /egov.properties file to point to your MySQL server and your gmail account
3. Run "maven clean install -DskipTests" from bpm-engine directory
4. Copy the generated app-1.0-SNAPSHOT.war file from the 'app/target' directory to the webapps directory of your Tomcat installation
