package com.poianitibaldizhou.trackme.sharedataservice.exception;

import java.util.ResourceBundle;

import static com.poianitibaldizhou.trackme.sharedataservice.util.Constants.INDIVIDUAL_REQUEST_NOT_FOUND_EXCEPTION_KEY;
import static com.poianitibaldizhou.trackme.sharedataservice.util.Constants.RESOURCE_STRING_PATH;

/**
 * Exception thrown when a specific individual request is not found
 */
public class IndividualRequestNotFoundException extends RuntimeException {

    /**
     * Constructor.
     * Creates a new individual request NOT FOUND exception
     *
     * @param requestId identifier of the NOT FOUND request
     */
    public IndividualRequestNotFoundException(Long requestId) {
        super(ResourceBundle.getBundle(RESOURCE_STRING_PATH)
                .getString(INDIVIDUAL_REQUEST_NOT_FOUND_EXCEPTION_KEY) + requestId);
    }
}
