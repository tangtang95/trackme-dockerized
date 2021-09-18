package com.poianitibaldizhou.trackme.sharedataservice.util;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.ComparisonSymbolProtocolMessage;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;

import java.util.Map;

/**
 * The utility class regarding ComparisonSymbol
 */
public class ComparisonSymbolUtils {

    private ComparisonSymbolUtils(){}

    /**
     * Returns the counterpart operator (of queryDsl) about the comparisonSymbol given
     *
     * @param comparisonSymbol the comparison symbol to be mapped
     * @return the counter part operator of the comparisonSymbol
     */
    public static Operator getSqlOperator(ComparisonSymbol comparisonSymbol) {
        Map<String, Operator> operators = ImmutableMap.<String, Operator>builder()
                .put(ComparisonSymbol.EQUALS.name(), Ops.EQ)
                .put(ComparisonSymbol.NOT_EQUALS.name(), Ops.NE)
                .put(ComparisonSymbol.LESS.name(), Ops.LT)
                .put(ComparisonSymbol.GREATER.name(), Ops.GT)
                .put(ComparisonSymbol.LIKE.name(), Ops.LIKE)
                .build();

        return operators.get(comparisonSymbol.name());
    }

    /**
     * Returns the counterpart comparison symbols of this service about the comparisonSymbol of the protocol
     *
     * @param comparisonSymbolProtocol the comparison symbol of the protocol to be mapped
     * @return the counterpart comparison symbol from a comparison symbol of the protocol
     */
    public static ComparisonSymbol getComparisonSymbol(ComparisonSymbolProtocolMessage comparisonSymbolProtocol){
        Map<ComparisonSymbolProtocolMessage, ComparisonSymbol> comparisonSymbolsMap =
                ImmutableMap.<ComparisonSymbolProtocolMessage, ComparisonSymbol>builder()
                .put(ComparisonSymbolProtocolMessage.EQUALS, ComparisonSymbol.EQUALS)
                .put(ComparisonSymbolProtocolMessage.NOT_EQUALS, ComparisonSymbol.NOT_EQUALS)
                .put(ComparisonSymbolProtocolMessage.LESS, ComparisonSymbol.LESS)
                .put(ComparisonSymbolProtocolMessage.GREATER, ComparisonSymbol.GREATER)
                .put(ComparisonSymbolProtocolMessage.LIKE, ComparisonSymbol.LIKE)
                .build();

        return comparisonSymbolsMap.get(comparisonSymbolProtocol);
    }

}
