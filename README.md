# Solace Chat Application - Developer Exercise 4

In this section, we are introducing an Authorization Server that handles auth requests from the UI.

An Authorization Server will be introduced under the auth-server folder. In addition, a solace-chat-common module has been added which will contain common objects for both the web-app-server and the auth-server.

To install the solace-chat-common module, run the following commands:

```
cd solace-chat-common
mvn install
```

* Fill in PubSub+ instance REST connection details in application.properties in web-app-server directory
* Copy and rename application-template.properties to application.properties in auth-server directory
* Fill in PubSub+ instance Solace messaging connection details in application.properties in auth-server directory
* Add code in SolaceCloudProxy.java to send POST to LOGIN/MESSAGE/REQUEST topic in Solace PubSub+ instance

To run the web-server, type the following command:

```
mvn clean install
mvn spring-boot:run
```

* Connect subscriber to PubSub+ instance (eg Solace PubSub+ Cloud 'Try Me' tab), listening on topic LOGIN/MESSAGE/REQUEST
* Attempt login, and see message is received by subscriber
