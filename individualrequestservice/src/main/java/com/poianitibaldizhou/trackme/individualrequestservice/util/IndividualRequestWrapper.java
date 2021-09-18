package com.poianitibaldizhou.trackme.individualrequestservice.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class IndividualRequestWrapper {

    @JsonIgnore
    private Long id;

    private String userSsn;

    @JsonIgnore
    private Long thirdPartyId;

    private IndividualRequestStatus status;
    private Timestamp timestamp;
    private Date startDate;
    private Date endDate;
    private String thirdPartyName;

    private String motivation;

    public static IndividualRequestWrapper convertIntoWrapper(@NonNull IndividualRequest individualRequest){
        IndividualRequestWrapper individualRequestWrapper = new IndividualRequestWrapper();
        individualRequestWrapper.setId(individualRequest.getId());
        individualRequestWrapper.setUserSsn(individualRequest.getUser().getSsn());
        individualRequestWrapper.setThirdPartyId(individualRequest.getThirdParty().getId());

        individualRequestWrapper.setStatus(individualRequest.getStatus());
        individualRequestWrapper.setTimestamp(individualRequest.getTimestamp());
        individualRequestWrapper.setStartDate(individualRequest.getStartDate());
        individualRequestWrapper.setEndDate(individualRequest.getEndDate());
        individualRequestWrapper.setThirdPartyName(individualRequest.getThirdParty().getIdentifierName());
        individualRequestWrapper.setMotivation(individualRequest.getMotivation());
        return individualRequestWrapper;
    }

}
