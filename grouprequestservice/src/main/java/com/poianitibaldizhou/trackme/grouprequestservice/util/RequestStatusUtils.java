package com.poianitibaldizhou.trackme.grouprequestservice.util;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.enumerator.GroupRequestStatusProtocolMessage;

import java.util.Map;

/**
 * The utility class regarding RequestStatus
 */
public class RequestStatusUtils {

    private RequestStatusUtils() {}

    /**
     * Returns the group request status of the protocol w.r.t. the request status of this service
     *
     * @param requestStatus the group request status of this service
     * @return the counterpart group request status of the protocol
     */
    public static GroupRequestStatusProtocolMessage getGroupRequestStatusOfProtocol(RequestStatus requestStatus){
        Map<RequestStatus, GroupRequestStatusProtocolMessage> requestStatusMap = ImmutableMap.<RequestStatus, GroupRequestStatusProtocolMessage>builder()
                .put(RequestStatus.ACCEPTED, GroupRequestStatusProtocolMessage.ACCEPTED)
                .put(RequestStatus.REFUSED, GroupRequestStatusProtocolMessage.REFUSED)
                .put(RequestStatus.UNDER_ANALYSIS, GroupRequestStatusProtocolMessage.UNDER_ANALYSIS)
                .build();
        return requestStatusMap.get(requestStatus);
    }

}
