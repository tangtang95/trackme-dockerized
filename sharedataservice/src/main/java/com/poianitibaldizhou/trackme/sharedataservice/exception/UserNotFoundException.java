package com.poianitibaldizhou.trackme.sharedataservice.exception;

import java.util.ResourceBundle;

import static com.poianitibaldizhou.trackme.sharedataservice.util.Constants.RESOURCE_STRING_PATH;
import static com.poianitibaldizhou.trackme.sharedataservice.util.Constants.USER_NOT_FOUND_EXCEPTION_KEY;

/**
 * Exception thrown when a specific user is not found
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor.
     * Creates a new user NOT FOUND exception
     *
     * @param ssn identifier of the NOT FOUND request
     */
    public UserNotFoundException(String ssn) {
        super(ResourceBundle.getBundle(RESOURCE_STRING_PATH).getString(USER_NOT_FOUND_EXCEPTION_KEY) + ssn);
    }

}
