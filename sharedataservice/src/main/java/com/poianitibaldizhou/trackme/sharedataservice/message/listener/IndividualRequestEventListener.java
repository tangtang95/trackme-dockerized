package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.IndividualRequestProtocolMessage;

public interface IndividualRequestEventListener {

    /**
     * Called when a message regarding individual request accepted has been found on the message broker (rabbit-mq).
     * Then it calls the internal communication service to handle the object
     *
     * @param individualRequestProtocol the individual request protocol message from the message broker
     */
    void onIndividualRequestAccepted(IndividualRequestProtocolMessage individualRequestProtocol);

}
