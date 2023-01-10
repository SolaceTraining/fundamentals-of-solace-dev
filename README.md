# Solace Chat Application Web Server
This project uses Spring-Boot to start up a WebServer and contains a skeleton web app for chat. Maven is required to run the web-server, and can be downloaded at https://maven.apache.org/download.cgi. The solclientjs libraries can be downloaded from http://dev.solace.com or https://www.npmjs.com/package/solclientjs.

* Download the solclientjs javascript libraries 
* Place the solclientjs /lib directory under web-app-server/src/main/resources/static/
* Copy and rename application-template.properties to application.properties
* Change directory `cd src`

To run the web-server, type the following command:

```
mvn clean install
mvn spring-boot:run
```

* Navigate to http://localhost:8081/ to bring up the chat application; notice the error in the Inspect Elements console
* Copy and rename application-properties-template.js to application-properties.js
* Fill in PubSub+ instance web messaging connection details
* Rerun web-server, and ensure no error message in Inspect Elements console

```
mvn spring-boot:run
```

* Complete publish/subscribe code in pubsubplusbroker.js
* Rerun web-server, send a message

```
mvn spring-boot:run
```
