package com.poianitibaldizhou.trackme.apigateway.repository;

import com.poianitibaldizhou.trackme.apigateway.entity.ThirdPartyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for the third party customers.
 * This is a jpa repository that accesses the persistent data regarding third party customers
 */
public interface ThirdPartyRepository extends JpaRepository<ThirdPartyCustomer, Long>{

    /**
     * Retrieves information related to a third party customer, by searching one that
     * matches with the specified email
     *
     * @param email email of the requested third party customer
     * @return information regarding the third party customer identified by email
     */
    Optional<ThirdPartyCustomer> findByEmail(String email);
}
