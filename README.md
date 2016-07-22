# bpm-engine
The BPM engine would be used to execute all the processes deployed on it.

# Installation

1. Download Activiti from http://activiti.org/download.html
2. Install Tomcat server, MySQL server, Maven and Postman client on your local machine
3. Clone this repository

# MySQL setup

1. Create a new database by running command "create database activiti;" from within MySQL console
2. Create a new database by running command "create database egov;" from within MySQL console

# Activiti setup

1. Unzip the downloaded Activiti zip file
2. Unzip the activiti-explorer.war file using "jar -xvf activiti-explorer.war" into a new directory activiti-explorer
3. Go to WEB-INF/classes/ within the activiti-explorer directory
4. Edit db.properties to point to your MySQL server and database
	i. Set db=mysql
	ii. Set jdbc.driver=com.mysql.jdbc.Driver
	iii. Set jdbc.url=jdbc:mysql://localhost:3306/activiti
5. Edit engine.properties and make all properties in the demo data section, except 'create.demo.users', as 'false'
6. Copy the entire activiti-explorer directory to webapps directory of your Tomcat installation (Generally found at /var/lib/tomcat7/webapps)
7. Start tomcat7 service, if not already running, by issuing "sudo service tomcat7 start" command.
8. Go to localhost:8080/activiti-explorer and login using kermit/kermit
9. Go to 'Manage' -> 'Groups' and create two new groups with id "alc" and "ro" respectively
10. Go to 'Manage' -> 'Users' and create two new users 'vijay' and 'vaibhav'. Add vijay to group alc and vaibhav to group ro.
11. Go to 'Manage' -> 'Deployments' -> 'Upload New' and upload the file 'app/src/test/resources/labor.bpmn20.xml' from the cloned repository.

# bpm-engine setup

1. Copy the egov.properties file in bpm-engine directory to /egov.properties
2. Edit /egov.properties file to point to your MySQL server and your gmail account
3. Run "maven clean install -DskipTests" from bpm-engine directory
4. Copy the generated bpm-engine.war file from the 'app/target' directory to the webapps directory of your Tomcat installation

# Frontend server setup

1. Edit the Storage section of /egov.properties file to point to a directory on your filesystem
2. Copy the generated frontend.war file from the 'frontend/target' directory to the webapps directory of your Tomcat installation
3. Do a POST request on http://localhost:8080/frontend/user/register with following data
        {
            "firstName": "Admin",
            "lastName" : "Account",
            "phone" : 1234567890,
            "password" : "password"
        }
4. Go to MySQL console, select 'egov' database by giving command 'use egov;' and give the following command 'update users set role="ADMIN";'