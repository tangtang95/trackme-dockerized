package com.trackme.trackmeapplication.service.exception;

import com.trackme.trackmeapplication.localdb.entity.HealthData;
import com.trackme.trackmeapplication.service.util.Constants;

public class InvalidHealthDataException extends Exception {

    public InvalidHealthDataException(HealthData healthData) {
        super(String.format(Constants.INVALID_HEALTH_DATA_STRING, healthData.toString()));
    }
}
