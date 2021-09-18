package com.poianitibaldizhou.trackme.grouprequestservice.util;

/**
 * Type of object that can be requested from a group request
 */
public enum  RequestType {
    ALL(false),
    USER_SSN(false),
    BIRTH_YEAR(true),
    BIRTH_CITY(false),
    HEART_BEAT(true),
    PRESSURE_MIN(true),
    PRESSURE_MAX(true),
    BLOOD_OXYGEN_LEVEL(true);

    private boolean isNumber;

    /**
     * Constructor.
     * Creates a request type which is defined by a boolean isNumber and an expression fieldPath
     *
     * @param isNumber true if the request type is a number, false otherwise
     */
    private RequestType(boolean isNumber){
        this.isNumber = isNumber;
    }

    /**
     * @return true if the request type is a number, false otherwise
     */
    public boolean isNumber() {
        return isNumber;
    }
}
