# Solace Chat Application - Developer Exercise 2
This project use Spring-Boot to start up a WebServer and contains a skeleton web app for chat. 

* Create queue for application to connect to, add topic subscription
* Run web-server and send messages to topic added to queue
* In application-properties.js, fill in sReceiveQueue property with queue name (see application-properties-template.js for reference)
* Complete queue consumer code in pubsubplusbroker.js

To run the web-server, type the following command:

```
cd web-app-server
mvn spring-boot:run
```

* Navigate to http://localhost:8081/ to bring up the chat application
* See messages are received upon connection, and queue is empty in PubSub+ instance
