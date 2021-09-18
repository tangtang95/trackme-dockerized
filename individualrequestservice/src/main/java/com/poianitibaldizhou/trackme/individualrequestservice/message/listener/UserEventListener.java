package com.poianitibaldizhou.trackme.individualrequestservice.message.listener;

import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.UserProtocolMessage;

public interface UserEventListener {

    /**
     * Called when a message regarding user created has been found on the message broker (rabbit-mq).
     * Then it calls the internal communication service to handle the object
     *
     * @param userProtocolMessage the user protocol message from the message broker
     */
    void onUserCreated(UserProtocolMessage userProtocolMessage);

}
