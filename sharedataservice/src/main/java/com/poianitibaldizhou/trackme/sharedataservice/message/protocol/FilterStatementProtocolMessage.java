package com.poianitibaldizhou.trackme.sharedataservice.message.protocol;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.ComparisonSymbolProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.FieldTypeProtocolMessage;
import lombok.Data;

import java.util.Objects;
import java.util.stream.Stream;

@Data
public class FilterStatementProtocolMessage {

    private FieldTypeProtocolMessage column;
    private String value;
    private ComparisonSymbolProtocolMessage comparisonSymbol;

    /**
     * Check if the protocol message is a valid one: all the attribute of the class should be non-null
     *
     * @return true if all attribute are non-null, false otherwise
     */
    public static boolean validateMessage(FilterStatementProtocolMessage protocolMessage) {
        return  Stream.of(protocolMessage.column, protocolMessage.value,
                protocolMessage.comparisonSymbol).noneMatch(Objects::isNull);
    }
}
