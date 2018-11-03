
# Solace Chat Application Web Server
This project use Spring-Boot to start up a WebServer and contains a web app for chat.

The web-app is hosted under \src\main\resources\static\

The Spring Boot server side code is hosted under \src\main\java\

Code is added here to proxy REST-ful requests to Solace

To run the web-server, type the following command:
```
mvn spring-boot:run
```

Navigate to http://localhost:8081/ to bring up the chat application