package com.poianitibaldizhou.trackme.apigateway.exception;

import com.poianitibaldizhou.trackme.apigateway.util.Constants;

/**
 * Exception that signal that a user with a certain ssn is already registered into the system
 */
public class AlreadyPresentSsnException extends RuntimeException {

    /**
     * Creates an exception when someone is trying to register a user with an ssn of
     * a user that is already registered into the system
     *
     * @param ssn this ssn is associated with a user who is already registered into the system
     */
    public AlreadyPresentSsnException(String ssn) {
        super(Constants.ALREADY_PRESENT_SSN + Constants.SPACE + ssn);
    }
}
