package com.poianitibaldizhou.trackme.apigateway.repository;

import com.poianitibaldizhou.trackme.apigateway.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for the private third party customers details.
 * This is a jpa repository that accesses the persistent data regarding third party customers that are not companies
 */
public interface PrivateThirdPartyDetailRepository extends JpaRepository<PrivateThirdPartyDetail, Long>{

    /**
     * Find private details related with a certain third party customer.
     * Note that it can be that the specified customer is not registered, and, also, that is registered
     * but as a company
     *
     * @param thirdPartyCustomer requested third party customer
     * @return private details related to thirdPartyCustomer
     */
    Optional<PrivateThirdPartyDetail> findByThirdPartyCustomer(ThirdPartyCustomer thirdPartyCustomer);
}
