package com.poianitibaldizhou.trackme.grouprequestservice.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Type of aggregator functions regarding the group request
 */
public enum AggregatorOperator {
    AVG, COUNT, DISTINCT_COUNT, MAX, MIN;

    /**
     * The set operation contains that returns true if THIS is inside the list of aggregators otherwise false
     *
     * @param aggregators the list of aggregators to check on
     * @return true if THIS is inside the list of aggregators otherwise false
     */
    public boolean contains(List<AggregatorOperator> aggregators) {
        return aggregators.stream().filter(this::equals).count() > 0;
    }

    /**
     * Returns the list of aggregator operators which are applicable on a number
     *
     * @return the list of aggregator operators applicable on a number
     */
    public static List<AggregatorOperator> getNumberAggregatorOperators(){
        List<AggregatorOperator> aggregatorOperators = new ArrayList<>();
        aggregatorOperators.add(AVG);
        aggregatorOperators.add(MAX);
        aggregatorOperators.add(MIN);
        return aggregatorOperators;
    }

    /**
     * Returns if an aggregator operator is valid w.r.t. a request type
     *
     * @param operator the aggregator operator to be checked
     * @param requestType the request type to be checked
     * @return true if the aggregator operator is valid w.r.t. the request type otherwise false
     */
    public static boolean isValidOperator(AggregatorOperator operator, RequestType requestType){
        return !operator.contains(AggregatorOperator.getNumberAggregatorOperators()) || requestType.isNumber();
    }
}