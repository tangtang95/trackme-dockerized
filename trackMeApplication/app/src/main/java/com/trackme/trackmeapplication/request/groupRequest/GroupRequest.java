package com.trackme.trackmeapplication.request.groupRequest;

import java.io.Serializable;

/**
 * Group request object.
 *
 * @author Mattia Tibaldi
 */
public class GroupRequest implements Serializable {

    /*private attributes*/
    private String aggregatorOperator;
    private String requestType;


    /**
     * Constructor. The client use this constructor to build the group request message to sent to
     * server.
     *
     * @param aggregatorOperator aggregatorOperator chosen
     * @param requestType request type chosen
     */
    GroupRequest(String aggregatorOperator, String requestType) {
        this.requestType = requestType;
        this.aggregatorOperator = aggregatorOperator;
    }

    //getter methods
    public String getAggregatorOperator() {
        return aggregatorOperator;
    }

    public String getRequestType() {
        return requestType;
    }
}
