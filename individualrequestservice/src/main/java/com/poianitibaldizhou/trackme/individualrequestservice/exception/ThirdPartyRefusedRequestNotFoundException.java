package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;

/**
 * Exception thrown when a user is blocking a third party of which he had never refused
 * a request
 */
public class ThirdPartyRefusedRequestNotFoundException extends RuntimeException {

    /**
     * Creates a third party not found refused request
     *
     * @param thirdPartyId no requests of the this specified third party customer has been found refused
     *                     by a certain customer
     */
    public ThirdPartyRefusedRequestNotFoundException(Long thirdPartyId) {
        super(Constants.REFUSED_REQUEST_NOT_FOUND + Constants.SPACE + thirdPartyId);
    }
}
