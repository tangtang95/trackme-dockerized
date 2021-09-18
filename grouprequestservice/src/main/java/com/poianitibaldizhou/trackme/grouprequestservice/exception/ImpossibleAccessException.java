package com.poianitibaldizhou.trackme.grouprequestservice.exception;

import com.poianitibaldizhou.trackme.grouprequestservice.util.Constants;

/**
 * Exception that signals that a controller method is being accessed by a non
 * legit third party customer
 */
public class ImpossibleAccessException extends RuntimeException {

    /**
     * Creates an exception that signals that an impossible is being made to a controller method
     */
    public ImpossibleAccessException() {
        super(Constants.IMPOSSIBLE_ACCESS);
    }
}
