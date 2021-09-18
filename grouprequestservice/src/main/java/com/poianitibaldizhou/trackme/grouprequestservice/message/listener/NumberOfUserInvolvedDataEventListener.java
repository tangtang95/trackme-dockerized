package com.poianitibaldizhou.trackme.grouprequestservice.message.listener;

import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.NumberOfUserInvolvedProtocolMessage;

public interface NumberOfUserInvolvedDataEventListener {

    /**
     * Called when a message regarding number of user involved data generated has been found on the message broker
     * (rabbit-mq). Then it calls the internal communication service to handle the object
     *
     * @param protocolMessage the number of user involved protocol message from the message broker
     */
    void onNumberOfUserInvolvedDataGenerated(NumberOfUserInvolvedProtocolMessage protocolMessage);

}
