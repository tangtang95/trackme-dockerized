package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;

/**
 * Exception thrown when a user is providing a type of response not allowed
 */
public class BadResponseTypeException extends RuntimeException {

    /**
     * Creates a new bad response type exception related with a certain non-existing response type
     *
     * @param responseType non-existing response type provided by the user
     */
    public BadResponseTypeException(String responseType) {
        super(Constants.BAD_RESPONSE_TYPE + Constants.SPACE + responseType);
    }
}
