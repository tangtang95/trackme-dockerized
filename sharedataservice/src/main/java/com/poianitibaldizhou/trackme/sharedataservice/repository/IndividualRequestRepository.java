package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.IndividualRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository for accessing data regarding the individual request of a third party
 */
public interface IndividualRequestRepository extends JpaRepository<IndividualRequest, Long> {

    /**
     * Returns if exists, the individual request by its id and the third party id
     *
     * @param requestId the id of the individual request
     * @param thirdPartyId the id of the third party that made the request
     * @return an optional containing the individual request of requestId and thirdPartyId if exists
     */
    Optional<IndividualRequest> findByIdAndThirdPartyId(Long requestId, Long thirdPartyId);

}
