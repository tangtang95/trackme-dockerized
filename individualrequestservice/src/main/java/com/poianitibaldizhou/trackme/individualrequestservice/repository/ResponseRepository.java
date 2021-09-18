package com.poianitibaldizhou.trackme.individualrequestservice.repository;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing data regarding the responses of individual requests
 */
public interface ResponseRepository extends JpaRepository<Response, Long> {
}
