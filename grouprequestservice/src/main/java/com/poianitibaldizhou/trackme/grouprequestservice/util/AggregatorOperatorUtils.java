package com.poianitibaldizhou.trackme.grouprequestservice.util;


import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.enumerator.AggregatorOperatorProtocolMessage;

import java.util.Map;

/**
 * The utility class regarding AggregatorOperator
 */
public class AggregatorOperatorUtils {

    private AggregatorOperatorUtils(){}

    /**
     * Returns the aggregator operator of the protocol w.r.t. the aggregator operator of this service
     *
     * @param aggregatorOperator the aggregator operator of this service
     * @return the counterpart aggregator operator of the protocol
     */
    public static AggregatorOperatorProtocolMessage getAggregatorOperatorOfProtocol(AggregatorOperator aggregatorOperator){
        Map<AggregatorOperator, AggregatorOperatorProtocolMessage> operators = ImmutableMap.<AggregatorOperator, AggregatorOperatorProtocolMessage>builder()
                .put(AggregatorOperator.COUNT, AggregatorOperatorProtocolMessage.COUNT)
                .put(AggregatorOperator.DISTINCT_COUNT, AggregatorOperatorProtocolMessage.DISTINCT_COUNT)
                .put(AggregatorOperator.AVG, AggregatorOperatorProtocolMessage.AVG)
                .put(AggregatorOperator.MAX, AggregatorOperatorProtocolMessage.MAX)
                .put(AggregatorOperator.MIN, AggregatorOperatorProtocolMessage.MIN)
                .build();
        return operators.get(aggregatorOperator);
    }
}
