package com.poianitibaldizhou.trackme.individualrequestservice.repository;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for the individual requests.
 * This is a jpa repository that accesses the persistent data regarding individual requests
 */
public interface IndividualRequestRepository extends JpaRepository<IndividualRequest, Long> {

    /**
     * Return the list of all the individual requests that are related to a certain third party customer
     *
     * @param id identifier of a certain third party customer
     * @return list of requests related to the specified third party customer
     */
    List<IndividualRequest> findAllByThirdParty_Id(Long id);

    /**
     * Return the list of all the individual requests with a certain status that are related to a certain user,
     * identified by the ssn.
     *
     * @param user identifier of a certain user
     * @param status all the returned request are characterized by this status
     * @return list of requests related to the specified user and with a certain status
     */
    List<IndividualRequest> findAllByUserAndStatus(User user, IndividualRequestStatus status);
}
