# Solace Chat Application Web Server
This project use Spring-Boot to start up a WebServer and contains a skeleton web app for chat. Download the solclientjs javascript libraries from http://dev.solace.com or https://www.npmjs.com/package/solclientjs.

* Place solclientjs /lib under src/main/resources/static/
* Add REST POST to solaceauth.js

To run the web-server, type the following command:

```
mvn spring-boot:run
```

* Navigate to http://localhost:8081/ to bring up the chat application
* See new login page
* Attempt to login with any username and password, see login failure
