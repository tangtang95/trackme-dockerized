package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for accessing data regarding the filter statement of a group request
 */
public interface FilterStatementRepository extends JpaRepository<FilterStatement, Long> {

    /**
     * Returns the list of all the filter statements that are related to a certain group request
     *
     * @param requestId the id of the specific group request
     * @return list of all the filter statements of a certain group request
     */
    List<FilterStatement> findAllByGroupRequest_Id(Long requestId);
}
