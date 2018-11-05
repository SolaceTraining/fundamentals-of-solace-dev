package com.solace.chat.application.auth.server;

import com.google.gson.Gson;
import com.solacesystems.jcsmp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.solace.chat.application.common.*;
import javax.annotation.PostConstruct;


/**
 * The LoginMessageReplier class is responsible for receiving a Login message and validating whether
 * the credentials match against an internal repository
 * @author Thomas Kunnumpurath
 */

@Service
public class LoginMessageReplier {

    //A Solace Message Publisher
    private XMLMessageProducer producer;
    //A Solace Message Listener
    private XMLMessageConsumer consumer;

    //Gson object for serializing/deserializing json objects
    private Gson gson = new Gson();

    //Implementation of the credentials repository 
    @Autowired
    private ICredentialsRepository credentialsRepository;

    //The solace specific properties are defined within application.properties
    @Value("${solace.host}")
    private String solaceHost;

    @Value("${solace.username}")
    private String solaceUsername;

    @Value("${solace.password}")
    private String solacePassword;

    @Value("${solace.vpn}")
    private String solaceVpn;

    //A Solace session object
    private JCSMPSession session;

    //The name of the QUEUE to bind to
    private static String QUEUE_NAME = "LOGIN_QUEUE";

    @PostConstruct
    public void init() {
        // Create a new Session. The Session properties are extracted from the
        // SessionConfiguration that was populated by the command line parser.
        //
        // Note: In other samples, a common method is used to create the Sessions.
        // However, to emphasize the most basic properties for Session creation,
        // this method is directly included in this sample.
        try {
            // Create session from JCSMPProperties. Validation is performed by
            // the API, and it throws InvalidPropertiesException upon failure.

            JCSMPProperties properties = new JCSMPProperties();

            properties.setProperty(JCSMPProperties.HOST, solaceHost);
            properties.setProperty(JCSMPProperties.USERNAME, solaceUsername);

            properties.setProperty(JCSMPProperties.VPN_NAME, solaceVpn);

            properties.setProperty(JCSMPProperties.PASSWORD, solacePassword);

            // With reapply subscriptions enabled, the API maintains a
            // cache of added subscriptions in memory. These subscriptions
            // are automatically reapplied following a channel reconnect.
            properties.setBooleanProperty(JCSMPProperties.REAPPLY_SUBSCRIPTIONS, true);

            // Disable certificate checking
            properties.setBooleanProperty(JCSMPProperties.SSL_VALIDATE_CERTIFICATE, false);

            // Channel properties
            JCSMPChannelProperties cp = (JCSMPChannelProperties) properties
                    .getProperty(JCSMPProperties.CLIENT_CHANNEL_PROPERTIES);


            session = JCSMPFactory.onlyInstance().createSession(properties);
            session.connect();
            producer = session.getMessageProducer(new PrintingPubCallback());

            //Add the Queue Logic Here

        } catch (InvalidPropertiesException ipe) {
            System.err.println("Error during session creation: ");
            ipe.printStackTrace();
            System.exit(0);
        } catch (JCSMPException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    class LoginRequestHandler implements XMLMessageListener {

        //Create a failure reply with no result
        private XMLMessage createReplyMessage(TextMessage request) throws JCSMPException {
            TextMessage replyMessage = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            //Convert the JSon to an Object
            UserObject userObject = gson.fromJson(request.getText(),UserObject.class);

            //Validate the user
            boolean validUser = credentialsRepository.isValidUser(userObject.getUsername(), userObject.getPassword());
            if(validUser)
                System.out.println("Successfully validated a user");
            else
                System.out.println("Authentication failed");
            AuthenticatedObject authenticatedObject = new AuthenticatedObject();
            authenticatedObject.setAuthenticated(validUser);
            replyMessage.setHTTPContentType("application/json");
            replyMessage.setText(gson.toJson(authenticatedObject));
            replyMessage.setApplicationMessageId(request.getApplicationMessageId());
            replyMessage.setDeliverToOne(true);
            replyMessage.setDeliveryMode(DeliveryMode.DIRECT);
            return replyMessage;
        }

        //Reply to a request
        private void sendReply(XMLMessage request, XMLMessage reply) throws JCSMPException {
            producer.sendReply(request, reply);
        }

        public void onReceive(BytesXMLMessage message) {
            System.out.println("Received a login request message, trying to parse it");

            if (message instanceof TextMessage) {
                TextMessage request = (TextMessage) message;
                try {
                    XMLMessage reply = createReplyMessage(request);
                    sendReply(request, reply);
                } catch (JCSMPException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to parse the request message, here's a message dump:" + message.toString());
            }
        }

        @Override
        public void onException(JCSMPException e) {
            System.out.println(e);
        }

    }

    class PrintingPubCallback implements JCSMPStreamingPublishEventHandler {
        public void handleError(String messageID, JCSMPException cause, long timestamp) {
            System.err.println("Error occurred for message: " + messageID);
            cause.printStackTrace();
        }

        // This method is only invoked for persistent and non-persistent
        // messages.
        public void responseReceived(String messageID) {
            System.out.println("Response received for message: " + messageID);
        }
    }
}