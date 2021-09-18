package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;

/**
 * Exception thrown when a user is trying to add an individual request with start date after the end date
 */
public class IncompatibleDateException extends RuntimeException {

    /**
     * Creates a new incompatible date exception
     */
    public IncompatibleDateException() {
        super(Constants.INCOMPATIBLE_DATE);
    }
}
