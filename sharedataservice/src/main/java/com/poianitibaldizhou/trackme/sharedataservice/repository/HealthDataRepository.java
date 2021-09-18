package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Timestamp;
import java.util.List;

/**
 * Repository for accessing data regarding the health data of a user
 */
public interface HealthDataRepository extends JpaRepository<HealthData, Long> {

    /**
     * Retrieves all the health data regarding the user between startTimestamp and endTimestamp
     *
     * @param user the user's owning the health data
     * @param startTimestamp the start timestamp
     * @param endTimestamp the end timestamp
     * @return a list of health data regarding the user between startTimestamp and endTimestamp
     */
    List<HealthData> findAllByUserAndTimestampBetween(User user, Timestamp startTimestamp, Timestamp endTimestamp);

}
