package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;

/**
 * Exception throw when a specific request has not been found
 */
public class RequestNotFoundException extends RuntimeException {

    /**
     * Creates a new request not found exception
     *
     * @param id identified of the not found request
     */
    public RequestNotFoundException(Long id) {
        super(Constants.REQUEST_ID_NOT_FOUND + Constants.SPACE + id);
    }
}
