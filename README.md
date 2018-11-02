# Introducing an auth-server

In this section, we are introducing an Authorization Server that handles auth requests from the UI.

The WebApp Server is now moved to the web-app-server folder and the Authorization Server will be moved under the auth-server folder. In addition, a solace-chat-common module has been added which will contain common objects for both modules.

To install the solace-chat-common module, navigate to solace-chat-cmmon and run the following command:

```
mvn install
```