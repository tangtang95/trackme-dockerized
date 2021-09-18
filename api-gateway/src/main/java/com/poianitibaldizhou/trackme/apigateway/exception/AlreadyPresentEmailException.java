package com.poianitibaldizhou.trackme.apigateway.exception;

import com.poianitibaldizhou.trackme.apigateway.util.Constants;

/**
 * Exception that signal that a third party customer with a certain email is already registered into the system
 */
public class AlreadyPresentEmailException extends RuntimeException {

    /**
     * Creates an exception when someone is trying to register a third party customer with an email of
     * a third party customer that is already registered into the system
     *
     * @param email this email is associated with a third party customer who is already registered into the system
     */
    public AlreadyPresentEmailException(String email) {
        super(Constants.ALREADY_PRESENT_EMAIL + Constants.SPACE + email);
    }
}
