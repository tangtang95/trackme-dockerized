package com.poianitibaldizhou.trackme.individualrequestservice.repository;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing data regarding the third parties
 */
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {
}
