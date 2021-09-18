package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Timestamp;
import java.util.List;

/**
 * Repository for accessing data regarding the position data of a user
 */
public interface PositionDataRepository extends JpaRepository<PositionData, Long> {

    /**
     * Retrieves all the position data regarding the user between startTimestamp and endTimestamp
     *
     * @param user the user's owning the position data
     * @param startTimestamp the start timestamp
     * @param endTimestamp the end timestamp
     * @return a list of position data regarding the user between startTimestamp and endTimestamp
     */
    List<PositionData> findAllByUserAndTimestampBetween(User user, Timestamp startTimestamp, Timestamp endTimestamp);

}
