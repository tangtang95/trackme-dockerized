package com.poianitibaldizhou.trackme.grouprequestservice.service;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.NumberOfUserInvolvedProtocolMessage;

import java.util.List;

public interface InternalCommunicationService {

    /**
     * Handle the number of user involved data protocol message obtained by the listener.
     * It can publish a new message of group request accepted to the message broker
     *
     * @param protocolMessage the protocol message of group request
     */
    void handleNumberOfUserInvolvedData(NumberOfUserInvolvedProtocolMessage protocolMessage);

    /**
     * Send a group request message through the message broker with the help of publishers
     *
     * @param groupRequest the group request to send
     * @param filterStatementList the list of filter statement connected to the group request
     */
    void sendGroupRequestMessage(GroupRequest groupRequest, List<FilterStatement> filterStatementList);
}
