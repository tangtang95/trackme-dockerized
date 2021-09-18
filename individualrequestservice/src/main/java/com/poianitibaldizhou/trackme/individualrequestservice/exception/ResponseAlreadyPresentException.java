package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;

/**
 * Exception thrown when an user is trying to add a response to a request that as already a response associated
 */
public class ResponseAlreadyPresentException extends RuntimeException {

    /**
     * Creates a new exception signaling that a response is already present for that request
     *
     * @param requestID id of the request for which a response is already present
     */
    public ResponseAlreadyPresentException(Long requestID) {
        super(Constants.RESPONSE_ALREADY_PRESENT + Constants.SPACE + requestID);
    }
}
