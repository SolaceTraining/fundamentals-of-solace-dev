# Solace Chat Application Web Server
This project use Spring-Boot to start up a WebServer and contains a web app for chat.

The web-app is hosted under \src\main\resources\static\

The Spring Boot server side code is hosted under \src\main\java\

You will need to install this dependency into your local maven repo so that your web-app
server can access it by running:

```
mvn install
```

To run the web-server, type the following command:
```
mvn spring-boot:run
```

Navigate to http://localhost:8081/ to bring up the chat application