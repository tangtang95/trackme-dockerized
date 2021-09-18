package com.poianitibaldizhou.trackme.individualrequestservice.message.listener;

import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.ThirdPartyProtocolMessage;

public interface ThirdPartyEventListener {

    /**
     * Called when a message regarding third party created has been found on the message broker (rabbit-mq).
     * Then it calls the internal communication service to handle the object
     *
     * @param protocolMessage the received message about third party of the protocool
     */
    void onThirdPartyCreated(ThirdPartyProtocolMessage protocolMessage);

}
