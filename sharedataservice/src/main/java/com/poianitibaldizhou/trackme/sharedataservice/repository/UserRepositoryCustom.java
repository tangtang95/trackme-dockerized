package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.sharedataservice.util.RequestType;

import java.util.List;

/**
 * Custom repository for accessing aggregate data regarding a group request or an individual request on users
 */
public interface UserRepositoryCustom {

    /**
     * Retrieves the aggregated data of the aggregator operator over the request type with all the filters applied
     *
     *
     * @param aggregatorOperator the aggregator operator of the query asked
     * @param requestType the type of request: the field on which the aggregator operator is applied
     * @param filters the list of filters defining the constraints of the query
     * @return a number aggregated value of all the data regarding the group request
     */
    Double getAggregatedData(AggregatorOperator aggregatorOperator, RequestType requestType, List<FilterStatement> filters);

}
