package com.poianitibaldizhou.trackme.grouprequestservice.repository;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for the group requests
 * This is a jpa repository that accesses the persistent data regarding group requests
 */
public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long>{

    /**
     * This retrieves all the group requests performed by a certain third party id
     *
     * @param thirdPartyId id of the third party customer that performed the list of
     *                     return requests
     * @return list of all the group requests performed by the specified third party customer
     */
    List<GroupRequest> findAllByThirdPartyId(Long thirdPartyId);
}
