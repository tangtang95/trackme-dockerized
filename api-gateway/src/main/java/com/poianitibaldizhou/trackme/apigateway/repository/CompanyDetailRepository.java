package com.poianitibaldizhou.trackme.apigateway.repository;

import com.poianitibaldizhou.trackme.apigateway.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for the company details.
 * This is a jpa repository that accesses the persistent data regarding company details of registered third
 * party customers
 */
public interface CompanyDetailRepository extends JpaRepository<CompanyDetail, Long>{

    /**
     * Find company details related with a certain third party customer.
     * Note that it can be that the specified customer is not registered, and, also, that is registered
     * but not as a company
     *
     * @param thirdPartyCustomer requested third party customer
     * @return company details related to thirdPartyCustomer
     */
    Optional<CompanyDetail> findByThirdPartyCustomer(ThirdPartyCustomer thirdPartyCustomer);
}
