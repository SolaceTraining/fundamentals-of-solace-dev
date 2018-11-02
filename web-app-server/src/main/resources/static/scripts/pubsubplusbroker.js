class PubSubPlusBroker {

  /**
   * @author Ush Shukla (Solace Inc.)
   * @since 06-09-2018
   * @class
   * @classdesc PubSubPlusBroker allows students to connect and interact with an instance of PubSub+.
   *            This class is intended to supplement <b>learning exercises</b> associated with the PubSub+ platform,
   *            and, as such, may not be the best example of how to design a general class for interacting with
   *            PubSub+. This class offers several methods:
   *
   *            -Connecting to a broker.
   *            -Authenticating against a contrived authentication service using REST (demonstrates REST Delivery Points)
   *            -Publish & subscribe to a topic on the broker
   */
  constructor() {

	/*Retrieve Broker Parameters from config file*/
    this.sBROKERURL = connectOptions.sBROKERURL;
    this.sVPN = connectOptions.sVPN;
    this.sUSERNAME = connectOptions.sUSERNAME;
    this.sPASSWORD = connectOptions.sPASSWORD;
    this.sPublishTopic = connectOptions.sPublishTopic;
    this.sSubscribeTopic = connectOptions.sSubscribeTopic;
    this.sReceiveQueue = connectOptions.sReceiveQueue;

    /*Topic Subscriber Parameters*/
    this.BLOCK_SUBSCRIBER_TIMEOUT_MS = 10000;
    this.GENERATE_SUBSCRIBE_EVENT = true;

    /*Our various connection objects*/
    this.broker = {};
    this.broker.session = {};
    this.messageConsumer = {};
    /******************************************/

    //standard init
    var factoryProps = new solace.SolclientFactoryProperties();
    factoryProps.profile = solace.SolclientFactoryProfiles.version10;
    solace.SolclientFactory.init(factoryProps);

    //Enable debug logging
    solace.SolclientFactory.setLogLevel(solace.LogLevel.DEBUG);
  }



  /**
   * * Uses a Solace PubSub+ topic to authenticate against a backend RESTful
   * service. This method demonstrates the RDP (REST Delivery Point) feature
   * of PubSub+, which lets an HTTP service be exposed across PubSub+ in a
   * publish/subscribe manner.
   *
   * Note: The credentials need to be entered into the backend service
   * *beforehand*
   *
   * @param {string} sUsername - Username to RESTfully authenticate against the broker
   * @param {string} sPassword - Password associated with the username
   */
  authenticate(sUsername, sPassword) {
    return true;
  }

  /**
   * Connects to a PubSub+ broker instance.
   *
   * @callback oResultCallback
   * @param {oResultCallback} oResultCallback - callback function to execute on success or failure events.
   * @returns Nothing
   */
  connect(oResultCallback) {

    //build the full URI to the broker. Makes for a more informational alert/debug message.
    var sFullURI = this.sUSERNAME + ":" + this.sPASSWORD + "@" + this.sBROKERURL + "/" + this.sVPN;
    console.debug("Establishing session to " + sFullURI);

    try {
      this.broker.session = solace.SolclientFactory.createSession({
        // solace.SessionProperties
        url: this.sBROKERURL,
        vpnName: this.sVPN,
        userName: this.sUSERNAME,
        password: this.sPASSWORD
      });
    } catch (error) {
      console.error(error.message);
    }

    /*
     *Since we want to access our callback function inside our event
     *handlers, we reassign it, thereby
     *forcing it to follow regular scoping rules
     *i.e. we can actually access it, inside the event handler lambda.
     */
    var oResultCallback = oResultCallback;
    var sFullURI = sFullURI;

    //setup an event listener for a successful connection
    this.broker.session.on(solace.SessionEventCode.UP_NOTICE, (sessionEvent) => {
      //notify the user via a debug message & an on-screen alert that
      //our connection succeeded.
      console.debug(sessionEvent);
      oResultCallback(true, "Connected to " + sFullURI);
    });

    //setup an event listener for connection failures
    this.broker.session.on(solace.SessionEventCode.CONNECT_FAILED_ERROR, (sessionEvent) => {
      console.error(sessionEvent);
      oResultCallback(false, "Could not connect to " + sFullURI + " error -> " + sessionEvent);
    });

    /*actually attempt a connection to the broker
     *Note that we don't declare a successful connection here
     *since the API specifically indicates that we should wait for the
     *UP_NOTICE (above) before performing any post-connection logic.
     */
    try {
      console.debug("connecting to broker...");
      this.broker.session.connect();
    } catch (error) {

      /*catch any other errors/exceptions that we did not account for with
       *relevant event handlers. For the sake of user-experience, we don't
       *want to dump these unexpected messages to the UI.
       */
      console.error("Could not connect to broker:" + error.message);
    }
  }


  /**
   * Publishes a message to the broker on a predefined topic.
   *
   * @callback oResultCallback
   *
   * @param {string} sBody - message body to send as a String
   * @param {oResultCallback} oResultCallback - Callback function used to handle the result
   * @returns Nothing
   */
  publish(sBody, oResultCallback) {

    //ensure that we have a session to play with
    if (this.broker.session === null) {
      oResultCallback(false, "No session! You're probably not connected to the broker.");
    } else {

      /*We create a message object, provide it with a destination, and
       *establish the delivery mode. After this, we assign our text message (the "body")
       *to said object*/
      var message = solace.SolclientFactory.createMessage();
      message.setDestination(solace.SolclientFactory.createTopicDestination(this.sPublishTopic));
      message.setDeliveryMode(solace.MessageDeliveryModeType.DIRECT);
      message.setBinaryAttachment(sBody);

      console.debug("Publishing message " + sBody + " to topic " + this.sPublishTopic);

      /*attempt the actual publication of our message object
       *success or failure, issue a message stating the fact.
       */
      try {
        //send the actual message and log a successful send
        this.broker.session.send(message);
        oResultCallback(true, "message published!");
        console.debug("message published");
      } catch (error) {

        //there was an error with the send()
        oResultCallback(false, "Could not publish message to topic. -> " + error.message);
        console.error("Could not publish message to topic. -> " + error.message);
      }
    }
  }


  /**
   * Subscribes to a given topic on the broker.
   * @callback oResultCallback
   *
   * @param {oResultCallback} oResultCallback - Callback function used to handle the result
   * @returns Nothing
   */
  subscribe(oResultCallback) {

    //ensure that we have a session to play with
    if (this.broker.session === null) {
      oResultCallback(false, "No session! You're probably not connected to the broker.");
    } else {
      /*This block establishes our subscription.
       *We defer actually recieving the message to the event
       *handler designated for that purpose.
       */
      try {
        /*
         *@params:
         *-Topic to receive on.
         *-Should we generate an event when the subscription succeeds?
         *-CorrelationKey that is used in events
         *-How long to block the execution thread, in milliseconds
         */
        this.broker.session.subscribe(
          solace.SolclientFactory.createTopic(this.sSubscribeTopic),
          this.GENERATE_SUBSCRIBE_EVENT,
          this.sSubscribeTopic,
          this.BLOCK_SUBSCRIBER_TIMEOUT_MS
        );
      } catch (error) {
        console.error("Could not subscribe to topic. ->" + error.message);
      }
    }

    /*
     *Rescope our callback and topic so we can use them
     *inside our event handler lambda, below.
     */
    var oResultCallback = oResultCallback;
    var sTopic = this.sSubscribeTopic;

    //What to do when subscription succeeds
    this.broker.session.on(solace.SessionEventCode.SUBSCRIPTION_OK, (sessionEvent) => {
      oResultCallback(true, "Successfully subscribed to " + sTopic);
      console.debug("Successfully subscribed to " + sTopic);
    });

    //What to do when a sub fails
    this.broker.session.on(solace.SessionEventCode.SUBSCRIPTION_ERROR, (sessionEvent) => {
      oResultCallback(false, "Could not subscribe to " + sTopic);
    });
  }


  /**
   * Consumes from a given queue on the broker
   * @callback oResultCallback
   *
   * @param {oResultCallback} oResultCallback - Callback function used to handle the result
   * @returns Nothing
   */
  consume(oResultCallback) {

    //ensure that we have a session to play with
    if (this.broker.session === null) {
      oResultCallback(false, "No session! You're probably not connected to the broker.");
    } else {

      /*This block establishes our consumer.
       *We defer actually recieving the message to the event
       *handler designated for that purpose.
       */
      this.messageConsumer = this.broker.session.createMessageConsumer({
        // solace.MessageConsumerProperties
        queueDescriptor: {
          name: this.sReceiveQueue,
          type: solace.QueueType.QUEUE
        },

        //enable auto-acknowledgement so that messages are read off the queue immediately
        acknowledgeMode: solace.MessageConsumerAcknowledgeMode.AUTO,
      });
      try {
        this.messageConsumer.connect()
      } catch (error) {
        console.error("Could not connect to queue. ->" + error.message);
      }
    }

    /*
     *Rescope our callback and topic so we can use them
     *inside our event handler lambda, below.
     */
    var oResultCallback = oResultCallback;
    var sQueue = this.sReceiveQueue;

    //What to do when subscription succeeds
    this.messageConsumer.on(solace.MessageConsumerEventName.UP, () => {
      oResultCallback(true, "Successfully connected to " + sQueue);
      console.debug("Successfully connected to " + sQueue);
    });

    //What to do when a sub fails
    this.messageConsumer.on(solace.MessageConsumerEventName.CONNECT_FAILED_ERROR, () => {
      oResultCallback(false, "Could not subscribe to " + sQueue);
      console.debug("Failed to connect to " + sQueue);
    });
  }


  /**
   * Returns the message received from a topic subscription.
   * @callback oResultCallback
   *
   * @param {oResultCallback} oResultCallback - Callback function used to handle the result
   */
  onTopicMessage(oResultCallback) {

    /*
     *Rescope our callback so we can use it
     *inside our event handler lambda, below.
     */
    var oResultCallback = oResultCallback;

    //register a lambda for when we receive a message.
    this.broker.session.on(solace.SessionEventCode.MESSAGE, (sMessage) => {

      //assign the message to our callback for use by the caller.
      //We dump a more detailed format of the message to the debug log
      oResultCallback(sMessage.getBinaryAttachment());
      console.debug(sMessage.dump());
    });
  }


  /**
   * Consumes messages from a queue and returns them to the caller using
   * the provided callback function.
   *
   * @callback oResultCallback
   *
   * @param {oResultCallback} oResultCallback - Callback function used to handle the result
   */
  onQueueMessage(oResultCallback) {

    //rescope our callback for use in the lambda
    var oResultCallback = oResultCallback;

    console.debug(this.messageConsumer);

    //register a lambda for when we receive a message.
    this.messageConsumer.on(solace.MessageConsumerEventName.MESSAGE, (sMessage) => {

      //assign the message to our callback for use by the caller.
      //We dump a more detailed format of the message to the debug log
      oResultCallback(sMessage.getBinaryAttachment());
      console.debug(sMessage.dump());
    });
  }

} //End class