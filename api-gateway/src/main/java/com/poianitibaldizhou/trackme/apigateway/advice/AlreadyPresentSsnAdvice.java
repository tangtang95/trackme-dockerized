package com.poianitibaldizhou.trackme.apigateway.advice;

import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentSsnException;
import com.poianitibaldizhou.trackme.apigateway.util.ExceptionResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Advice for managing errors about user registration: in particular, an user with a certain ssn is already
 * registered
 */
@ControllerAdvice
public class AlreadyPresentSsnAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception AlreadyPresentSsnException is thrown.
     * The issue is an HTTP 400.
     * The body of the advice contains the message of the exception
     *
     * @param e the error which triggers the advice
     * @return an http 400 response that contains the message of the exception
     */
    @ExceptionHandler(AlreadyPresentSsnException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ExceptionResponseBody alreadyPresentSsnHandler(AlreadyPresentSsnException e) {
        return new ExceptionResponseBody(
                Timestamp.valueOf(LocalDateTime.now()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                e.getMessage());

    }
}
