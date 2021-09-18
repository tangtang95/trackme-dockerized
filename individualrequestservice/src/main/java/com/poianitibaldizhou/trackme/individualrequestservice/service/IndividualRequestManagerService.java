package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;

import java.util.List;

/**
 * Interface provided to offer the service that manages that individual request
 */
public interface IndividualRequestManagerService {

    /**
     * Retrieves the list of the request performed by a third party customer.
     * In case in which no request has been performed by the third party, an empty list is returned. Indeed,
     * no exception are thrown (neither runtime)
     *
     * @param thirdPartyID request retrieved belongs to the third party customer identified with this id
     * @return list of request performed by a certain third party customer
     */
    List<IndividualRequest> getThirdPartyRequests(Long thirdPartyID);

    /**
     * Retrieves the list of the pending request of a certain user.
     * Note that this will throw a runtime exception in case the specified is not registered into the system
     *
     * @param user request retrieved are related to the user identified by this ssn
     * @return list of request related to the specified user, that have PENDING as status
     */
    List<IndividualRequest> getUserPendingRequests(User user);

    /**
     * This adds a new request. The request will be signed as pending, in the case in which the user has not blocked
     * the third party customer, otherwise it will get instantly refused.
     * Note that this can throw a runtime exception, that is UserNotFoundException, in the case in which the third party
     * customer is trying to make a request of a user that is not registered into the system.
     *
     * @param newRequest request that will be added
     * @return added request (with the correct status: this means that no injection of request with a specific
     *         status is possible
     */
    IndividualRequest addRequest(IndividualRequest newRequest);

    /**
     * This method return a request based on its id.
     * Useful for enhancing the HAL approach.
     *
     * @param id id of the requested individual request
     * @return individual request identified by an id
     */
    IndividualRequest getRequestById(Long id);
}
