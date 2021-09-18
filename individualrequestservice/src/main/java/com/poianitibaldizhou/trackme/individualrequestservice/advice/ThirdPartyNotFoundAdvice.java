package com.poianitibaldizhou.trackme.individualrequestservice.advice;

import com.poianitibaldizhou.trackme.individualrequestservice.exception.ThirdPartyNotFoundException;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ExceptionResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Advice for managing error about not found third party
 */
@ControllerAdvice
public class ThirdPartyNotFoundAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception ThirdPartyNotFoundException is thrown.
     * The issue is an HTTP 404.
     * The body of the advice contains the message of the exception
     *
     * @param e error that triggers the advice
     * @return http 404 that contains the message of the exception
     */
    @ExceptionHandler(ThirdPartyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    ExceptionResponseBody thirdPartyNotFoundHandler(ThirdPartyNotFoundException e) {
        return new ExceptionResponseBody(
                Timestamp.valueOf(LocalDateTime.now()),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.toString(),
                e.getMessage());
    }
}
