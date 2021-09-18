package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.UserProtocolMessage;

public interface UserEventListener {

    /**
     * Called when a message regarding user created has been found on the message broker (rabbit-mq).
     * Then it calls the internal communication service to handle the object
     *
     * @param userProtocol the user protocol message from the message broker
     */
    void onUserCreated(UserProtocolMessage userProtocol);

}
