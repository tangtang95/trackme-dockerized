package com.poianitibaldizhou.trackme.sharedataservice.service;

import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.sharedataservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatedData;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;

import java.sql.Date;
import java.util.List;

/**
 * Interface provided to offer the service that is necessary to the third party to access data
 */
public interface AccessDataService {

    /**
     * ThirdParty's API method: Retrieves data regarding the individual request.
     * In case the requestId cannot be found, a RunTimeException is thrown and a handler will return a NOT FOUND
     * http response
     *
     *
     * @param thirdPartyId the id of the third party asking for data
     * @param requestId the id of the individual request which the third party wants to retrieve
     * @return the data regarding the individual request
     */
    DataWrapper getIndividualRequestData(Long thirdPartyId, Long requestId);

    /**
     * ThirdParty's API method: Retrieves data regarding the group request.
     * In case the requestId cannot be found, a RunTimeException is thrown and a handler will return a NOT FOUND
     * http response
     *
     *
     * @param thirdPartyId the id of the third party asking for data
     * @param requestId the id of the group request which the third party wants to retrieve
     * @return the data regarding the group request
     */
    AggregatedData getGroupRequestData(Long thirdPartyId, Long requestId);

    /**
     * User's API method: Retrieves its own data from a specific date to another specific date
     *
     * @param userId the social security number of the user in consideration
     * @param from the lower bound of date
     * @param to the upper bound of date
     * @return the data wrapper containing a list of health data and a list of position data regarding the user
     * of userId within the two date (from, to)
     */
    DataWrapper getOwnData(String userId, Date from, Date to);
}
