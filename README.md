# Solace Chat Application - BONUS

In this section, you will learn how to load balance Login Requests across a Non-Exclusive Queue that can be round robin'd between multiple instances of auth-servers.

* Create non-exclusive queue on PubSub+ instance (login queue)
* Subscribe login queue to LOGIN/MESSAGE/REQUEST
* Modify LoginMessageReplier.java to consume from login queue
* Start multiple instances of auth-server to see load balancing of login requests

To run the web-app-server, type the following command:

```
cd web-app-server
mvn spring-boot:run
```

To run the auth-server, type the following command:

```
cd auth-server
mvn spring-boot:run
```
