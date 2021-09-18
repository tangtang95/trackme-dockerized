package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.GroupRequestProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.IndividualRequestProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.UserProtocolMessage;

public interface InternalCommunicationService {

    /**
     * Handle the group request accepted protocol message obtained by the listener.
     * It saves the group request in the DB
     *
     * @param groupRequestProtocolMessage the protocol message of group request
     */
    void handleGroupRequestAccepted(GroupRequestProtocolMessage groupRequestProtocolMessage);

    /**
     * Handle the individual request accepted protocol message obtained by the listener.
     * It saves the individual request in the DB
     *
     * @param individualRequestProtocolMessage the protocol message of individual request
     */
    void handleIndividualRequestAccepted(IndividualRequestProtocolMessage individualRequestProtocolMessage);

    /**
     * Handle the user created protocol message obtained by the listener
     * It saves the user in the DB
     *
     * @param userProtocolMessage the protocol message of the user
     */
    void handleUserCreated(UserProtocolMessage userProtocolMessage);

    /**
     * Handle the group request created (under_analysis) protocol message obtained by the listener
     * It calculates the number of user involved in the group request and publish it on the message queue
     *
     * @param groupRequestProtocolMessage the protocol message of the group request
     */
    void handleGroupRequestCreated(GroupRequestProtocolMessage groupRequestProtocolMessage);

}
