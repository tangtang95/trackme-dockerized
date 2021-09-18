package com.trackme.trackmeapplication.service.util;

import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.service.exception.InvalidHealthDataException;

public interface HealthDataInspector {

    /**
     * Return if the health data given is grave for the health condition or not
     *
     * @param healthData the health data to be checked
     * @return true if the condition is grave based on the health data, false otherwise
     * @throws InvalidHealthDataException if the health data given is invalid (e.g. blood oxygen level not a percentage)
     */
    boolean isGraveCondition(HealthData healthData) throws InvalidHealthDataException;

}
