package com.poianitibaldizhou.trackme.apigateway.exception;

import com.poianitibaldizhou.trackme.apigateway.util.Constants;

/**
 * Exception that signals that a user with a certain username is already present into the system
 */
public class AlreadyPresentUsernameException extends RuntimeException{

    /**
     * Creates an exception when someone is trying to register a user with an username of
     * a user that is already registered into the system
     *
     * @param username this username is associated with a user who is already registered into the system
     */
    public AlreadyPresentUsernameException(String username) {
        super(Constants.ALREADY_PRESENT_USERNAME + Constants.SPACE + username);
    }

}
