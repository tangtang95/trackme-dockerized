package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;

import java.util.Optional;

/**
 * Interfaces for a service that manages all the operation regarding the login / logout of third parties
 */
public interface ThirdPartyAuthenticationService extends AuthenticationService {

    /**
     * Login a third party into the system
     *
     * @param email email of the third party who is trying to login
     * @param password password of the third party who is trying to login
     * @return {@link Optional} containing a possible token
     */
    Optional<String> thirdPartyLogin(String email, String password);

    /**
     * Find a third party related with a certain token
     *
     * @param token token that may identify some customer
     * @return {@link Optional} containing that may contain the third party related with that token
     */
    Optional<ThirdPartyCustomer> findThirdPartyByToken(String token);

    /**
     * Logout a third party from the system
     *
     * @param thirdPartyCustomer third party that will loguout
     */
    void thirdPartyLogout(ThirdPartyCustomer thirdPartyCustomer);
}
