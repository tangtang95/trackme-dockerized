package com.poianitibaldizhou.trackme.individualrequestservice.repository;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdPartyKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing data regarding the blocked third party customer
 */
public interface BlockedThirdPartyRepository extends JpaRepository<BlockedThirdParty, BlockedThirdPartyKey> {

}
