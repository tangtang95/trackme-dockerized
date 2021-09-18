package com.poianitibaldizhou.trackme.apigateway.util;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Response body of exception genereted by api
 */
@Data
public class ExceptionResponseBody {

    private Timestamp timestamp;

    private int status;

    private String error;

    private String message;

    /**
     * Creates an exception response body
     *
     * @param timestamp timestamp of the exception
     * @param status status code of the exception (e.g. 404 NOT FOUND)
     * @param error error
     * @param message message of the error
     */
    public ExceptionResponseBody(Timestamp timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    /**
     * Empty constructor
     */
    public ExceptionResponseBody() {

    }
}
