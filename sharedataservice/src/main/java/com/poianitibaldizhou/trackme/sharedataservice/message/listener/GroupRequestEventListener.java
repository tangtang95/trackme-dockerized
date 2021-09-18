package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.GroupRequestProtocolMessage;

public interface GroupRequestEventListener {

    /**
     * Called when a message regarding group request created has been found on the message broker (rabbit-mq).
     * Then it calls the internal communication service to handle the object
     *
     * @param groupRequestProtocol the group request protocol message from the message broker
     */
    void onGroupRequestCreated(GroupRequestProtocolMessage groupRequestProtocol);

    /**
     * Called when a message regarding group request accepted has been found on the message broker (rabbit-mq).
     * Then it calls the internal communication service to handle the object
     *
     * @param groupRequestProtocol the group request protocol message from the message broker
     */
    void onGroupRequestAccepted(GroupRequestProtocolMessage groupRequestProtocol);

}
