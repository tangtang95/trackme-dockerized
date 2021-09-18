package com.trackme.trackmeapplication.request.groupRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trackme.trackmeapplication.request.RequestStatus;

import java.io.Serializable;

/**
 * Group request object.
 *
 * @author Mattia Tibaldi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupRequestWrapper implements Serializable {

    /*private attributes*/
    private String creationTimestamp;
    private String aggregatorOperator;
    private String requestType;
    private String status;

    private String selfLink;

    /**
     * Constructor
     */
    public GroupRequestWrapper(){}

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    /*Getter method*/
    public String getCreationTimestamp() {
        return creationTimestamp.substring(0,10);
    }

    public String getAggregatorOperator() {
        return aggregatorOperator;
    }

    public String getRequestType() {
        return requestType;
    }

    public RequestStatus getStatus() {
        return RequestStatus.valueOf(status);
    }

    String extractGroupRequestLink() { return selfLink; }
}
