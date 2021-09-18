package com.poianitibaldizhou.trackme.individualrequestservice.exception;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;

/**
 * Exception thrown when a user has not been found (i.e. not registered)
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Creates a new user not found exception
     *
     * @param user non found user
     */
    public UserNotFoundException(User user) {
        super(Constants.USER_NOT_FOUND + Constants.SPACE + user.getSsn());
    }
}
