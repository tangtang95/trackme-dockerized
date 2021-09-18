package com.poianitibaldizhou.trackme.individualrequestservice.message.protocol;

import lombok.Data;

import java.sql.Date;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Unique third party protocol message such that there is no confusion between the unique ID which comes from s ThirdParty
 * Customer entity
 */
@Data
public class ThirdPartyProtocolMessage {

    private Long id;

    // Company details
    private String companyName;
    private String address;
    private String dunsNumber;

    // Private details
    private String ssn;
    private String name;
    private String surname;
    private Date birthDate;
    private String birthCity;

    /**
     * Check if the protocol message is a valid private detail one: all the attribute of the private should be non-null
     *
     * @return true if all attribute are non-null, false otherwise
     */
    public static boolean validatePrivateDetailMessage(ThirdPartyProtocolMessage protocolMessage){
        return  Stream.of(protocolMessage.id, protocolMessage.ssn, protocolMessage.name, protocolMessage.surname,
                protocolMessage.birthCity, protocolMessage.birthDate).noneMatch(Objects::isNull);
    }

    /**
     * Check if the protocol message is a valid company detail one: all the attribute of the company should be non-null
     *
     * @return true if all attribute are non-null, false otherwise
     */
    public static boolean validateCompanyDetailMessage(ThirdPartyProtocolMessage protocolMessage){
        return  Stream.of(protocolMessage.id, protocolMessage.companyName, protocolMessage.address,
                protocolMessage.dunsNumber).noneMatch(Objects::isNull);
    }

}
