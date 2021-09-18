package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.AggregatorOperatorProtocolMessage;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;

import java.util.Map;

/**
 * The utility class regarding AggregatorOperator
 */
public class AggregatorOperatorUtils {

    private AggregatorOperatorUtils(){}

    /**
     * Returns the operator (of queryDsl, useful for dynamic query) from a enum aggregator operator
     *
     * @param aggregatorOperator the aggregator operator to be mapped
     * @return the counterpart operator of the aggregator operator given
     */
    public static Operator getSqlOperator(AggregatorOperator aggregatorOperator) {
        Map<String, Operator> operators = ImmutableMap.<String, Operator>builder()
                .put(AggregatorOperator.COUNT.name(), Ops.AggOps.COUNT_AGG)
                .put(AggregatorOperator.DISTINCT_COUNT.name(), Ops.AggOps.COUNT_DISTINCT_AGG)
                .put(AggregatorOperator.AVG.name(), Ops.AggOps.AVG_AGG)
                .put(AggregatorOperator.MAX.name(), Ops.AggOps.MAX_AGG)
                .put(AggregatorOperator.MIN.name(), Ops.AggOps.MIN_AGG)
                .build();

        return operators.get(aggregatorOperator.name());
    }

    /**
     * Returns the aggregator operator of share data service w.r.t. the one of the protocol
     *
     * @param aggregatorOperatorProtocol the aggregator operator of the protocol to be mapped
     * @return the counterpart aggregator operator of the aggregator operator of the protocol
     */
    public static AggregatorOperator getAggregatorOperator(AggregatorOperatorProtocolMessage aggregatorOperatorProtocol){
        Map<String, AggregatorOperator> operators = ImmutableMap.<String, AggregatorOperator>builder()
                .put(AggregatorOperatorProtocolMessage.COUNT.name(), AggregatorOperator.COUNT)
                .put(AggregatorOperatorProtocolMessage.DISTINCT_COUNT.name(), AggregatorOperator.DISTINCT_COUNT)
                .put(AggregatorOperatorProtocolMessage.AVG.name(), AggregatorOperator.AVG)
                .put(AggregatorOperatorProtocolMessage.MAX.name(), AggregatorOperator.MAX)
                .put(AggregatorOperatorProtocolMessage.MIN.name(), AggregatorOperator.MIN)
                .build();
        return operators.get(aggregatorOperatorProtocol.name());
    }
}
