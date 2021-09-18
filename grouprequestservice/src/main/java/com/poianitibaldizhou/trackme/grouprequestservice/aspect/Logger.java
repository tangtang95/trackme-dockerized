package com.poianitibaldizhou.trackme.grouprequestservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * General logger of the system
 */
@Slf4j
@Component
@Aspect
public class Logger {

    /**
     * Logs the method regarding the calls of services: both upload response service and individual request
     * manager service are logged here
     *
     * @param joinPoint the join point in which the call happened
     * @return the object which the service call should return
     * @throws Throwable Throwable propagated exception from the service call
     */
    @Around("execution(public * com.poianitibaldizhou.trackme.grouprequestservice.service.*.*(..))")
    public Object logUploadResponseServiceCall(ProceedingJoinPoint joinPoint) throws Throwable {
        Object retValue;
        log.info("Before method: " + joinPoint.getSignature().toString());
        try {
            retValue = joinPoint.proceed();
            log.info("After method: " + joinPoint.getSignature().toString());
        } catch(Throwable throwable) {
            log.info("Exception: " + joinPoint.getSignature().toString() + " caused by: " + throwable.toString());
            throw throwable;
        }

        return retValue;
    }

}

