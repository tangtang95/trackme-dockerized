package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;

/**
 * Interface provided to offer the service that is necessary to the user to send data
 */
public interface SendDataService {

    /**
     * User's API method: Adds a new health data of the user {userId} in the database.
     * In case the userId cannot be found, a RunTimeException is thrown and a handler will return a NOT FOUND
     * http response
     *
     * @param userId the social security number of the user's healthData
     * @param healthData the new health data to be saved
     * @return the healthData itself if successful, otherwise throw a runtime exception (UserNotFoundException)
     */
    HealthData sendHealthData(String userId, HealthData healthData);

    /**
     * User's API method: Adds a new position data of the user in the database
     * In case the userId cannot be found, a RunTimeException is thrown and a handler will return a NOT FOUND
     * http response
     *
     * @param userId the social security number of the user's positionData
     * @param positionData the new position data to be saved
     * @return the positionData itself if successful, otherwise throw a runtime exception (UserNotFoundException)
     */
    PositionData sendPositionData(String userId, PositionData positionData);

    /**
     * User's API method: Adds new list of health and position data of the user in the database
     * In case where the userId cannot be found, a RunTimeException is thrown and a handler will return a NOT FOUND
     * http response
     *
     * @param userId the social security number of the user's positionData
     * @param data the new blocks of data to be saved
     * @return the block of data itself if successful, otherwise throw a runtime exception (UserNotFoundException)
     */
    DataWrapper sendClusterOfData(String userId, DataWrapper data);
}
