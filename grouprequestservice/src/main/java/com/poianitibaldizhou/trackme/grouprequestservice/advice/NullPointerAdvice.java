package com.poianitibaldizhou.trackme.grouprequestservice.advice;

import com.poianitibaldizhou.trackme.grouprequestservice.util.Constants;
import com.poianitibaldizhou.trackme.grouprequestservice.util.ExceptionResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Advice for managing error about null pointer exception: they may be caused by missing parameters in the
 * the body of requests
 */
@ControllerAdvice
public class NullPointerAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception NullPointerException is thrown.
     * The issue is an HTTP 400.
     * The body of the advice contains the message of the exception
     *
     * @param e error that triggers the advice
     * @return http 400 that contains the message of the exception
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ExceptionResponseBody nullPointerHandler(NullPointerException e) {
        return new ExceptionResponseBody(
                Timestamp.valueOf(LocalDateTime.now()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                Constants.INVALID_OPERATION);
    }


}
