package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.RequestTypeProtocolMessage;

import java.util.Map;

/**
 * The utility class regarding RequestType
 */
public class RequestTypeUtils {

    private RequestTypeUtils() {}

    public static RequestType getRequestType(RequestTypeProtocolMessage requestTypeProtocol){
        Map<RequestTypeProtocolMessage, RequestType> requestTypeMap = ImmutableMap.<RequestTypeProtocolMessage, RequestType>builder()
                .put(RequestTypeProtocolMessage.ALL, RequestType.ALL)
                .put(RequestTypeProtocolMessage.BIRTH_CITY, RequestType.BIRTH_CITY)
                .put(RequestTypeProtocolMessage.BIRTH_YEAR, RequestType.BIRTH_YEAR)
                .put(RequestTypeProtocolMessage.BLOOD_OXYGEN_LEVEL, RequestType.BLOOD_OXYGEN_LEVEL)
                .put(RequestTypeProtocolMessage.HEART_BEAT, RequestType.HEART_BEAT)
                .put(RequestTypeProtocolMessage.PRESSURE_MAX, RequestType.PRESSURE_MAX)
                .put(RequestTypeProtocolMessage.PRESSURE_MIN, RequestType.PRESSURE_MIN)
                .put(RequestTypeProtocolMessage.USER_SSN, RequestType.USER_SSN)
                .build();
        return requestTypeMap.get(requestTypeProtocol);
    }

}
