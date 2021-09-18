package com.poianitibaldizhou.trackme.apigateway.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * General authentication service interface used in the secured access
 */
public interface  AuthenticationService {

    /**
     * Returns user details related with a certain token
     *
     * @param token token of the user
     * @return user details containing information about the entity that may be identified by token
     */
    UserDetails findUserDetailsByToken(String token);

}
