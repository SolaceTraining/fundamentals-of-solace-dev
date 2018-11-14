# Solace Chat Application Web Server
This project uses Spring-Boot to start up a WebServer and contains a skeleton web app for chat. Maven is required to run the web-server, and can be downloaded at https://maven.apache.org/download.cgi

Download the solclientjs javascript libraries from http://dev.solace.com, or retrieve it from https://www.npmjs.com/package/solclientjs, and place the /lib directory under web-app-server/src/main/resources/static/

To run the web-server, type the following command:

```
mvn clean install
mvn spring-boot:run
```

Navigate to http://localhost:8081/ to bring up the chat application
