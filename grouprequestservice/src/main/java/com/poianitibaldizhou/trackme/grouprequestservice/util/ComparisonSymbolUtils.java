package com.poianitibaldizhou.trackme.grouprequestservice.util;

import com.google.common.collect.ImmutableMap;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.enumerator.ComparisonSymbolProtocolMessage;

import java.util.Map;

/**
 * The utility class regarding ComparisonSymbol
 */
public class ComparisonSymbolUtils {

    private ComparisonSymbolUtils(){}

    /**
     * Returns the counterpart comparison symbols of the protocol about the comparisonSymbol of this service
     *
     * @param comparisonSymbol the comparison symbol of this service
     * @return the counterpart comparison symbol of the protocol
     */
    public static ComparisonSymbolProtocolMessage getComparisonSymbolOfProtocol(ComparisonSymbol comparisonSymbol){
        Map<ComparisonSymbol, ComparisonSymbolProtocolMessage> comparisonSymbolsMap =
                ImmutableMap.<ComparisonSymbol, ComparisonSymbolProtocolMessage>builder()
                .put(ComparisonSymbol.EQUALS, ComparisonSymbolProtocolMessage.EQUALS)
                .put(ComparisonSymbol.NOT_EQUALS, ComparisonSymbolProtocolMessage.NOT_EQUALS)
                .put(ComparisonSymbol.LESS, ComparisonSymbolProtocolMessage.LESS)
                .put(ComparisonSymbol.GREATER, ComparisonSymbolProtocolMessage.GREATER)
                .put(ComparisonSymbol.LIKE, ComparisonSymbolProtocolMessage.LIKE)
                .build();

        return comparisonSymbolsMap.get(comparisonSymbol);
    }

}
