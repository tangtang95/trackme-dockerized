package com.poianitibaldizhou.trackme.sharedataservice.exception;

import java.util.ResourceBundle;

import static com.poianitibaldizhou.trackme.sharedataservice.util.Constants.GROUP_REQUEST_NOT_FOUND_EXCEPTION_KEY;
import static com.poianitibaldizhou.trackme.sharedataservice.util.Constants.RESOURCE_STRING_PATH;

/**
 * Exception thrown when a specific group request is not found
 */
public class GroupRequestNotFoundException extends RuntimeException {

    /**
     * Constructor.
     * Creates a new group request NOT FOUND exception
     *
     * @param requestId identifier of the NOT FOUND request
     */
    public GroupRequestNotFoundException(Long requestId) {
        super(ResourceBundle.getBundle(RESOURCE_STRING_PATH)
                .getString(GROUP_REQUEST_NOT_FOUND_EXCEPTION_KEY) + requestId);
    }

}
