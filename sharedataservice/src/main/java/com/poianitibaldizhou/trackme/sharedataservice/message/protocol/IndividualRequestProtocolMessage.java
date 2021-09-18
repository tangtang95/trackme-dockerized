package com.poianitibaldizhou.trackme.sharedataservice.message.protocol;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.IndividualRequestStatusProtocolMessage;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.stream.Stream;

@Data
public class IndividualRequestProtocolMessage {

    private Long id;
    private Long thirdPartyId;
    private String thirdPartyName;
    private IndividualRequestStatusProtocolMessage status;
    private Timestamp creationTimestamp;
    private Date startDate;
    private Date endDate;
    private String userSsn;
    private String motivationText;

    /**
     * Check if the protocol message is a valid one: all the attribute of the class should be non-null
     *
     * @return true if all attribute are non-null, false otherwise
     */
    public static boolean validateMessage(IndividualRequestProtocolMessage protocolMessage){
        return  Stream.of(protocolMessage.id, protocolMessage.thirdPartyId, protocolMessage.status,
                protocolMessage.thirdPartyName, protocolMessage.creationTimestamp, protocolMessage.startDate,
                protocolMessage.endDate, protocolMessage.userSsn, protocolMessage.motivationText)
                .noneMatch(Objects::isNull);
    }

}
