package com.iitrab.hrtool.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Logging selected aspects.
 */
@Aspect
@Component
class LoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Logging around all public methods in class with @Service annotation.
     * Before execute method logging all signature.
     * After execute method logging all signature and returned object.
     *
     * @param joinPoint the place where the method is called or an exception is thrown
     * @return object that the intercepted method returns
     * @throws Throwable
     */
    @Around("com.iitrab.hrtool.aspect.AopExpressions.forAllPublicServiceMethods()")
    Object aroundPublicServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        String beforeMethod = joinPoint.getSignature().toString();
        LOG.debug(beforeMethod);

        Object result = joinPoint.proceed();

        String afterMethod = joinPoint.getSignature().toString() + "\nreturned: " + result;
        LOG.debug(afterMethod);

        return result;
    }
}
