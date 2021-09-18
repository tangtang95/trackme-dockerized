package com.poianitibaldizhou.trackme.apigateway.message.protocol;

import lombok.Data;

import java.sql.Date;
import java.util.Objects;
import java.util.stream.Stream;

@Data
public class UserProtocolMessage {

    private String ssn;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String birthCity;
    private String birthNation;

    /**
     * Check if the protocol message is a valid one: all the attribute of the class should be non-null
     *
     * @return true if all attribute are non-null, false otherwise
     */
    public static boolean validateMessage(UserProtocolMessage protocolMessage){
        return  Stream.of(protocolMessage.ssn, protocolMessage.firstName, protocolMessage.lastName,
                protocolMessage.birthCity, protocolMessage.birthDate, protocolMessage.birthNation)
                .noneMatch(Objects::isNull);
    }


}
