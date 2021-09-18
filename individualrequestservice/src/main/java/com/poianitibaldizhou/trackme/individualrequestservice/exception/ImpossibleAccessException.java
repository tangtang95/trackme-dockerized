package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;

/**
 * Exception for handling impossible accesses
 */
public class ImpossibleAccessException extends RuntimeException{

    /**
     * Creates a impossible access exception
     */
    public ImpossibleAccessException() {
        super(Constants.INVALID_CREDENTIAL);
    }
}
