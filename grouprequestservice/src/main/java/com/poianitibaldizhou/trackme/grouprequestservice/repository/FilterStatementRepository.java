package com.poianitibaldizhou.trackme.grouprequestservice.repository;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for the filter statements
 * This is a jpa repository that accesses the persistent data regarding filter statements
 */
public interface FilterStatementRepository extends JpaRepository<FilterStatement, Long>{

    /**
     * This retrieves all the filter statements related with to a certain group request
     *
     * @param id id of the group request to which the statements are attached
     * @return list of filter statements related to the specified group requested
     */
    List<FilterStatement> findAllByGroupRequest_Id(Long id);
}
