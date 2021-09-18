package com.poianitibaldizhou.trackme.apigateway.advice;

import com.poianitibaldizhou.trackme.apigateway.exception.SsnNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.util.ExceptionResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Advice for managing errors about user that research of user that are not registered into the system
 */
@ControllerAdvice
public class UserNotFoundAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception SsnNotFoundException is thrown.
     * The issue is an HTTP 404.
     * The body of the advice contains the message of the exception
     *
     * @param e the error which triggers the advice
     * @return an http 404 response that contains the message of the exception
     */
    @ExceptionHandler(SsnNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    ExceptionResponseBody userNotFoundHandler(SsnNotFoundException e) {
        return new ExceptionResponseBody(
                Timestamp.valueOf(LocalDateTime.now()),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.toString(),
                e.getMessage());

    }
}
