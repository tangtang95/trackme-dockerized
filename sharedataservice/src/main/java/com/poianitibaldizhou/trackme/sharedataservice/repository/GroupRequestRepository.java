package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for accessing data regarding the group request of a third party
 */
public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {

    /**
     * Returns if exists, the group request by its id and the third party id
     *
     * @param requestId the id of the group request
     * @param thirdPartyId the id of the third party that made the request
     * @return an optional containing the group request of requestId and thirdPartyId if exists
     */
    Optional<GroupRequest> findByIdAndThirdPartyId(Long requestId, Long thirdPartyId);

}
