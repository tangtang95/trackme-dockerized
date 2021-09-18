package com.poianitibaldizhou.trackme.grouprequestservice.message.protocol;

import lombok.Data;

import java.util.Objects;
import java.util.stream.Stream;

@Data
public class NumberOfUserInvolvedProtocolMessage {

    private Long groupRequestId;
    private Integer numberOfUserInvolved;

    /**
     * Check if the protocol message is a valid one: all the attribute of the class should be non-null
     *
     * @return true if all attribute are non-null, false otherwise
     */
    public static boolean validateMessage(NumberOfUserInvolvedProtocolMessage protocolMessage){
        return  Stream.of(protocolMessage.groupRequestId, protocolMessage.numberOfUserInvolved)
                .noneMatch(Objects::isNull);
    }

}
