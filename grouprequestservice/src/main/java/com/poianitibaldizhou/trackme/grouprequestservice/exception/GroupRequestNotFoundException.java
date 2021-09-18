package com.poianitibaldizhou.trackme.grouprequestservice.exception;

import com.poianitibaldizhou.trackme.grouprequestservice.util.Constants;

/**
 * Exception thrown when a specific group request has not been found
 */
public class GroupRequestNotFoundException extends RuntimeException{

    /**
     * Creates a new group request not found exception
     *
     * @param id identified of the not found request
     */
    public GroupRequestNotFoundException(Long id) {
        super(Constants.REQUEST_ID_NOT_FOUND + Constants.SPACE + id);
    }
}
