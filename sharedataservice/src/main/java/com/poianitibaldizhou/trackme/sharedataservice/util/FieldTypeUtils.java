package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.FieldTypeProtocolMessage;

import java.util.Map;

/**
 * The utility class regarding FieldType
 */
public class FieldTypeUtils {

    private FieldTypeUtils() {}

    public static FieldType getFieldType(FieldTypeProtocolMessage fieldTypeProtocol){
        Map<FieldTypeProtocolMessage, FieldType> fieldTypeMap =
                ImmutableMap.<FieldTypeProtocolMessage, FieldType>builder()
                .put(FieldTypeProtocolMessage.BIRTH_CITY, FieldType.BIRTH_CITY)
                .put(FieldTypeProtocolMessage.BIRTH_YEAR, FieldType.BIRTH_YEAR)
                .put(FieldTypeProtocolMessage.BLOOD_OXYGEN_LEVEL, FieldType.BLOOD_OXYGEN_LEVEL)
                .put(FieldTypeProtocolMessage.HEART_BEAT, FieldType.HEART_BEAT)
                .put(FieldTypeProtocolMessage.HEALTH_TIMESTAMP, FieldType.HEALTH_TIMESTAMP)
                .put(FieldTypeProtocolMessage.LATITUDE, FieldType.LATITUDE)
                .put(FieldTypeProtocolMessage.LONGITUDE, FieldType.LONGITUDE)
                .put(FieldTypeProtocolMessage.POSITION_TIMESTAMP, FieldType.POSITION_TIMESTAMP)
                .put(FieldTypeProtocolMessage.PRESSURE_MAX, FieldType.PRESSURE_MAX)
                .put(FieldTypeProtocolMessage.PRESSURE_MIN, FieldType.PRESSURE_MIN)
                .put(FieldTypeProtocolMessage.BIRTH_NATION, FieldType.BIRTH_NATION)
                .build();
        return fieldTypeMap.get(fieldTypeProtocol);
    }
}
