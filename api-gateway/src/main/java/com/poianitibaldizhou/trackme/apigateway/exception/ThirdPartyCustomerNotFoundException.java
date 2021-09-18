package com.poianitibaldizhou.trackme.apigateway.exception;

import com.poianitibaldizhou.trackme.apigateway.util.Constants;

/**
 * Exception that signals that a third party customer with a certain email can't be found, because it is not registered
 */
public class ThirdPartyCustomerNotFoundException extends RuntimeException {

    /**
     * Creates an exception when someone is trying to retrieve information regarding a third party customer that is not
     * registered
     *
     * @param email email that identifies the third party customer that is not present
     */
    public ThirdPartyCustomerNotFoundException(String email) {
        super(Constants.THIRD_PARTY_NOT_FOUND + Constants.SPACE + email);
    }
}
