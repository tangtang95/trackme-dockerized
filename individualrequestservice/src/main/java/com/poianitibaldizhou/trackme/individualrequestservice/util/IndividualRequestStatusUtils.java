package com.poianitibaldizhou.trackme.individualrequestservice.util;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.enumerator.IndividualRequestStatusProtocolMessage;

import java.util.Map;

public class IndividualRequestStatusUtils {

    private IndividualRequestStatusUtils() {}

    /**
     * Return the counterpart protocol message of individual request status of this service
     *
     * @param individualRequestStatus the individual request status to be converted
     * @return the counterpart protocol message of individual request status of this service
     */
    public static IndividualRequestStatusProtocolMessage getIndividualRequestStatusOfProtocol(IndividualRequestStatus individualRequestStatus){
        Map<IndividualRequestStatus, IndividualRequestStatusProtocolMessage> requestStatusMap = ImmutableMap
                .<IndividualRequestStatus, IndividualRequestStatusProtocolMessage>builder()
                .put(IndividualRequestStatus.ACCEPTED, IndividualRequestStatusProtocolMessage.ACCEPTED)
                .put(IndividualRequestStatus.REFUSED, IndividualRequestStatusProtocolMessage.REFUSED)
                .put(IndividualRequestStatus.PENDING, IndividualRequestStatusProtocolMessage.PENDING)
                .build();
        return requestStatusMap.get(individualRequestStatus);
    }
}
