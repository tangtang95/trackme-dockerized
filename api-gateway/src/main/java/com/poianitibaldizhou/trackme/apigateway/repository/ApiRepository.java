package com.poianitibaldizhou.trackme.apigateway.repository;

import com.poianitibaldizhou.trackme.apigateway.entity.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * Repository for the api.
 * This is a jpa repository that accesses the persistent data regarding available api
 */
public interface ApiRepository extends JpaRepository<Api, Integer> {

    /**
     * Find all the api that are accessible by means of certain http method
     *
     * @param httpMethod http method that matches all the returned api
     * @return list of api that matches the specified http method
     */
    List<Api> findAllByHttpMethod(HttpMethod httpMethod);
}
