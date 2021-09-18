package com.poianitibaldizhou.trackme.apigateway.advice;

import com.poianitibaldizhou.trackme.apigateway.exception.ThirdPartyCustomerNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.util.ExceptionResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Advice for managing errors about research of third party customer that are not registered into the system
 */
@ControllerAdvice
public class ThirdPartyCustomerNotFoundAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception ThirdPartyCustomerNotFound is thrown.
     * The issue is an HTTP 404.
     * The body of the advice contains the message of the exception
     *
     * @param e the error which triggers the advice
     * @return an http 404 response that contains the message of the exception
     */
    @ExceptionHandler(ThirdPartyCustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    ExceptionResponseBody thirdPartyCustomerNotFoundHandler(ThirdPartyCustomerNotFoundException e) {
        return new ExceptionResponseBody(
                Timestamp.valueOf(LocalDateTime.now()),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.toString(),
                e.getMessage());

    }
}
