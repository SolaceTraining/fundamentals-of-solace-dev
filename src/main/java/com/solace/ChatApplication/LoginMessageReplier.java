package com.solace.ChatApplication;

import com.solacesystems.jcsmp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class LoginMessageReplier {

    XMLMessageProducer producer;
    XMLMessageConsumer consumer;

    @Autowired
    ICredentialsRepository credentialsRepository;

    @Value("${solace.host}")
    private String solaceHost;

    @Value("${solace.username}")
    private String solaceUsername;

    @Value("${solace.password}")
    private String solacePassword;

    @Value("${solace.vpn}")
    private String solaceVpn;

    private JCSMPSession session;

    private static String REQUEST_TOPIC = "LOGIN/MESSAGE/REQUEST";

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

        private XMLMessage createReplyMessage(BytesXMLMessage request) throws JCSMPException {
            return null;
        }

        //Reply to a request
        private void sendReply(XMLMessage request, XMLMessage reply) throws JCSMPException {
        }

        public void onReceive(BytesXMLMessage message) {
        }

        @Override
        public void onException(JCSMPException e) {
        }

    }

    class PrintingPubCallback implements JCSMPStreamingPublishEventHandler {
        public void handleError(String messageID, JCSMPException cause, long timestamp) {
               }

        // This method is only invoked for persistent and non-persistent
        // messages.
        public void responseReceived(String messageID) {
        }
    }
}
