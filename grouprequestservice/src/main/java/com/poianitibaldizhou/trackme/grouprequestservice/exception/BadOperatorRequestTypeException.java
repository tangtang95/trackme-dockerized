package com.poianitibaldizhou.trackme.grouprequestservice.exception;

import com.poianitibaldizhou.trackme.grouprequestservice.util.Constants;
import com.poianitibaldizhou.trackme.grouprequestservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.grouprequestservice.util.RequestType;

/**
 * Exception thrown when an operator can't be matched with a request type
 */
public class BadOperatorRequestTypeException extends RuntimeException{

    /**
     * Creates a new exception when it is attempted to match an aggregator operator with an invalid request type
     *
     * @param operator aggregator that can't be matched with requestType
     * @param requestType request type that can't be matched with operator
     */
    public BadOperatorRequestTypeException(AggregatorOperator operator, RequestType requestType) {
        super(Constants.BAD_OPERATOR_REQUEST_TYPE + Constants.SPACE + operator.toString() + Constants.SPACE + requestType);
    }
}
