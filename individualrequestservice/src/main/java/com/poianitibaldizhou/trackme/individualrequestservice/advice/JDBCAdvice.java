package com.poianitibaldizhou.trackme.individualrequestservice.advice;

import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ExceptionResponseBody;
import org.hibernate.JDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Advice for handling exception regarding hibernate
 */
@ControllerAdvice
public class JDBCAdvice {

    /**
     * An advice signaled into the body of the response that activates
     * only when the exception JDBCException is thrown.
     * The issue is an HTTP 400.
     * The body of the advice contains the message of the exception
     *
     * @param e error that triggers the advice
     * @return http 400 that contains the message of the exception
     */
    @ExceptionHandler(JDBCException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ExceptionResponseBody badData(JDBCException e) {
        return new ExceptionResponseBody(
                Timestamp.valueOf(LocalDateTime.now()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                Constants.INVALID_OPERATION);
    }
}
