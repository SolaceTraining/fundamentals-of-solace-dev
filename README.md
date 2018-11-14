# Solace Chat Application - Developer Exercise 5

In this section, you will change the REST-ful POST to Solace to a Request-Reply and you will implement the consumer that listens to a request and responds appropriately.

* Modify SolaceCloudProxy.java to send request message
* Modify LoginMessageReplier.java to receive login requests, process them, and send reply message back

To run the web-server, type the following command:

```
cd web-app-server
mvn spring-boot:run
```

To run the web-server, type the following command:

```
cd auth-server
mvn spring-boot:run
```

* Navigate to http://localhost:8081/ to bring up the chat application
* Attempt login with user from HashMapCredentialsRepo.java (eg username: ValidUser password: solace)
* Notice login is successful
