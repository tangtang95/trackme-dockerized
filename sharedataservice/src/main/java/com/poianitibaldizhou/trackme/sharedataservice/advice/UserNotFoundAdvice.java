package com.poianitibaldizhou.trackme.sharedataservice.advice;

import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.util.ExceptionResponseBody;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Advice for managing errors about users not found
 */
@ControllerAdvice
public class UserNotFoundAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception UserNotFoundException is thrown.
     * The issue is an HTTP 404.
     * The body of the advice contains the message of the exception
     *
     * @param ex the error which triggers the advice
     * @return an http 404 response that contains the message of the exception
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionResponseBody userNotFoundHandler(UserNotFoundException ex) {
        return new ExceptionResponseBody(
                Timestamp.valueOf(LocalDateTime.now()),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.toString(),
                ex.getMessage());
    }

}
