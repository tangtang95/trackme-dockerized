package com.trackme.trackmeapplication.request.individualRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trackme.trackmeapplication.request.RequestStatus;

import java.io.Serializable;

/**
 * Individual request wrapper for parsing the response message
 *
 * @author Mattia Tibaldi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndividualRequestWrapper implements Serializable {

    //attributes
    private String userSsn;
    private String status;
    private String timestamp;
    private String thirdPartyName;
    private String startDate;
    private String endDate;
    private String motivation;

    private String responseLink;

    /**
     * Constructor
     */
    public IndividualRequestWrapper(){}

    /**
     * Setter method.
     *
     * @param responseLink response link to set
     */
    public void setResponseLink(String responseLink) {
        this.responseLink = responseLink;
    }

    //getter methods
    public RequestStatus getStatus() {
        return RequestStatus.valueOf(status);
    }

    public String getTimestamp() {
        return timestamp.substring(0,10);
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getMotivation() {
        return motivation;
    }

    String extractResponseLink(){
        return responseLink;
    }

    String getUserSsn(){return userSsn;}

}
