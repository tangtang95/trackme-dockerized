package com.poianitibaldizhou.trackme.apigateway.exception;

import com.poianitibaldizhou.trackme.apigateway.util.Constants;

/**
 * Exception that signals that a user with a certain ssn can't be found, because it is not registered
 */
public class SsnNotFoundException extends RuntimeException {

    /**
     * Creates an exception when someone is trying to retrieve information regarding a user that is not
     * registered
     *
     * @param ssn ssn that identifies the user that is not present
     */
    public SsnNotFoundException(String ssn) {
        super(Constants.USER_NOT_FOUND + Constants.SPACE + ssn);
    }
}
