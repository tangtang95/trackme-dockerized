package com.poianitibaldizhou.trackme.grouprequestservice.message.protocol;

import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.enumerator.AggregatorOperatorProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.enumerator.GroupRequestStatusProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.enumerator.RequestTypeProtocolMessage;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Data
public class GroupRequestProtocolMessage {

    private Long id;
    private Long thirdPartyId;
    private GroupRequestStatusProtocolMessage status;
    private Timestamp creationTimestamp;
    private AggregatorOperatorProtocolMessage aggregatorOperator;
    private RequestTypeProtocolMessage requestType;
    private List<FilterStatementProtocolMessage> filterStatements;

    /**
     * Check if the protocol message is a valid one: all the attribute of the class should be non-null
     *
     * @return true if all attribute are non-null, false otherwise
     */
    public static boolean validateMessage(GroupRequestProtocolMessage protocolMessage){
        return  Stream.of(protocolMessage.id, protocolMessage.thirdPartyId,
                protocolMessage.status, protocolMessage.creationTimestamp, protocolMessage.aggregatorOperator,
                protocolMessage.requestType, protocolMessage.filterStatements).noneMatch(Objects::isNull) &&
                protocolMessage.filterStatements.stream().allMatch(FilterStatementProtocolMessage::validateMessage);
    }

}
