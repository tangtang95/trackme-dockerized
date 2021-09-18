package com.poianitibaldizhou.trackme.grouprequestservice.util;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.enumerator.RequestTypeProtocolMessage;

import java.util.Map;

/**
 * The utility class regarding RequestType
 */
public class RequestTypeUtils {

    private RequestTypeUtils() {}

    /**
     * Returns the request type of the protocol w.r.t. the request type of this service
     *
     * @param requestType the request type of this service
     * @return the counterpart request type of the protocol
     */
    public static RequestTypeProtocolMessage getRequestTypeOfProtocol(RequestType requestType){
        Map<RequestType, RequestTypeProtocolMessage> requestTypeMap = ImmutableMap.<RequestType, RequestTypeProtocolMessage>builder()
                .put(RequestType.ALL, RequestTypeProtocolMessage.ALL)
                .put(RequestType.BIRTH_CITY, RequestTypeProtocolMessage.BIRTH_CITY)
                .put(RequestType.BIRTH_YEAR, RequestTypeProtocolMessage.BIRTH_YEAR)
                .put(RequestType.BLOOD_OXYGEN_LEVEL, RequestTypeProtocolMessage.BLOOD_OXYGEN_LEVEL)
                .put(RequestType.HEART_BEAT, RequestTypeProtocolMessage.HEART_BEAT)
                .put(RequestType.PRESSURE_MAX, RequestTypeProtocolMessage.PRESSURE_MAX)
                .put(RequestType.PRESSURE_MIN, RequestTypeProtocolMessage.PRESSURE_MIN)
                .put(RequestType.USER_SSN, RequestTypeProtocolMessage.USER_SSN)
                .build();
        return requestTypeMap.get(requestType);
    }

}
