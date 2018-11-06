/**
 */
$(document).ready(function() {

  /***CALLBACK HANDLERS***/

  //alert handler
  function alertHandler(bResult, sMessage) {

    //create an empty div to be used for alerting
    //assign it our alert message
    var $div = $("<div></div>");
    $div.text(sMessage);

    //style the alert according to whether we
    //succeeded or failed.
    if (bResult) {
      $div.addClass("alert alert-success");
    } else {
      $div.addClass("alert alert-danger");
    }

    //add in our alert and have it fade out
    //in 5s.
    $("#alerts").append($div);
    $div.fadeOut(5000);

    return;
  }


  //call back function to handle intial connections
  function connectHandler(bResult, sMessage) {
    console.debug("In connectHandler");

    alertHandler(bResult, sMessage);
    if (bResult) {
      //we connected fine, now try and subscribe
      broker.subscribe(alertHandler);
    }

    return;
  }

  //handles messages pulled from the broker
  function messageHandler(sMessage) {
    console.debug("message handler called with text: " + sMessage);
    updateChatArea("Broker: " + sMessage);
    return;
  }


  $('#chatInput').keypress(function (e) {
    var key = e.which;
    if(key == 13)  // the enter key code
     {
       $('#buttonSend').click();
       return false;  
     }
   });   

  /***EVENT HANDLERS***/
  /*
   *Called when a user sends a message
   */
  $("#buttonSend").click(function() {
    console.debug("Caught send button event");

    //the text that the user just typed.
    var sChatText = $("#chatInput").val();

    console.debug(sChatText);

    //attempt a publish to the broker topic
    broker.publish(sChatText, alertHandler)
    updateChatArea("me: " + sChatText);

    //reset the text input box
    $("#chatInput").val("");
  })


  /***HELPER METHODS***/
  //updates the chat window with new text
  function updateChatArea(sText) {
    var sCurrentText = $("#chatArea").val() + "\n" + sText;
    $("#chatArea").val(sCurrentText);
  }


  //get an instance of our broker
  var broker = new PubSubPlusBroker();

  //try to connect
  broker.connect(connectHandler);

  //register our callback for when we have a topic message
  broker.onTopicMessage(messageHandler);

}) //end ready
