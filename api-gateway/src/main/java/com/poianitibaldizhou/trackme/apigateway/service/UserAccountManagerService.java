package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.User;

/**
 * Interfaces provided to offer the services that manage the accounts of users
 */
public interface UserAccountManagerService {

    /**
     * Retrieves a user based on a ssn, which is the id of the entity.
     * This requires, to be successful, that an user with a certain ssn exists
     *
     * @param ssn ssn that identifies the requested user
     * @return user identified by the ssn
     */
    User getUserBySsn(String ssn);

    /**
     * Retrieves a user based on a username.
     * This requires, to be successful, that a user with a certain username exists
     *
     * @param username username that identifies the requested user
     * @return user identified by username
     */
    User getUserByUserName(String username);

    /**
     * Adds an user to the system: a person is allowed to register into the system as a user by providing
     * a user-name, a password, his credentials, his social security number and
     * The required credentials are the following: first name, last name, birth date, birth city and birth nation
     *
     * @return the registered user into the system
     */
    User registerUser(User user);

}
