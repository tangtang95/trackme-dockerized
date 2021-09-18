package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.User;

import java.util.Optional;

/**
 * Interfaces for a service that manages all the operation regarding the login / logout of the users
 */
public interface UserAuthenticationService extends AuthenticationService {
    /**
     * Logs in with the given username and password
     *
     * @param username username of the user who is trying to userLogin
     * @param password password of the user who is trying to userLogin
     * @return an {@link Optional} of a user when userLogin succeeds
     */
    Optional<String> userLogin(String username, String password);

    /**
     * Finds a user by the token
     *
     * @param token token of the user
     * @return an {@link Optional} of a user that is identified by token
     */
    Optional<User> findUserByToken(String token);

    /**
     * Logs out the given user
     *
     * @param user user to userLogout
     */
    void userLogout(User user);
}
