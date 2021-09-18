package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;

/**
 * Exception raised when an user is trying to accept the request of another user
 */
public class NonMatchingUserException extends RuntimeException {

    /**
     * Creates a new non matchin user exception: a user has trying to accept the request of another
     * user, identified by ssn
     *
     * @param ssn can't accept the request of this user
     */
    public NonMatchingUserException(String ssn) {
        super(Constants.NON_MATCHING_USER + Constants.SPACE + ssn);
    }
}
