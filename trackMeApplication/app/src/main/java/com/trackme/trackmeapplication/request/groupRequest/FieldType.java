package com.trackme.trackmeapplication.request.groupRequest;

/**
 * Type of field that can be used on a filter statement
 *
 * @author Mattia Tibaldi
 */
public enum FieldType {
    POSITION_TIMESTAMP, HEALTH_TIMESTAMP, LATITUDE,
    LONGITUDE, HEART_BEAT,
    PRESSURE_MIN, PRESSURE_MAX,
    BLOOD_OXYGEN_LEVEL, BIRTH_CITY, BIRTH_NATION, BIRTH_YEAR
}
