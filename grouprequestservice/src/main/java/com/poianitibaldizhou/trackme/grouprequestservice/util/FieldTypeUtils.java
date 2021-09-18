package com.poianitibaldizhou.trackme.grouprequestservice.util;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.enumerator.FieldTypeProtocolMessage;

import java.util.Map;

/**
 * The utility class regarding FieldTypeProtocolMessage
 */
public class FieldTypeUtils {

    private FieldTypeUtils() {}

    /**
     * Returns the field type of the protocol w.r.t. the field type of this service
     *
     * @param fieldType the field type of this service
     * @return the counterpart field type of the protocol
     */
    public static FieldTypeProtocolMessage getFieldTypeOfProtocol(FieldType fieldType){
        Map<FieldType, FieldTypeProtocolMessage> fieldTypeMap =
                ImmutableMap.<FieldType, FieldTypeProtocolMessage>builder()
                .put(FieldType.BIRTH_CITY, FieldTypeProtocolMessage.BIRTH_CITY)
                .put(FieldType.BIRTH_YEAR, FieldTypeProtocolMessage.BIRTH_YEAR)
                .put(FieldType.BLOOD_OXYGEN_LEVEL, FieldTypeProtocolMessage.BLOOD_OXYGEN_LEVEL)
                .put(FieldType.HEART_BEAT, FieldTypeProtocolMessage.HEART_BEAT)
                .put(FieldType.HEALTH_TIMESTAMP, FieldTypeProtocolMessage.HEALTH_TIMESTAMP)
                .put(FieldType.LATITUDE, FieldTypeProtocolMessage.LATITUDE)
                .put(FieldType.LONGITUDE, FieldTypeProtocolMessage.LONGITUDE)
                .put(FieldType.POSITION_TIMESTAMP, FieldTypeProtocolMessage.POSITION_TIMESTAMP)
                .put(FieldType.PRESSURE_MAX, FieldTypeProtocolMessage.PRESSURE_MAX)
                .put(FieldType.PRESSURE_MIN, FieldTypeProtocolMessage.PRESSURE_MIN)
                .put(FieldType.BIRTH_NATION, FieldTypeProtocolMessage.BIRTH_NATION)
                .build();
        return fieldTypeMap.get(fieldType);
    }
}
