package com.iitrab.hrtool.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Class with created pointcuts.
 */
@Aspect
class AopExpressions {

    /**
     * Pointcut for all public methods in class with annotation @Service.
     */
    @Pointcut("within(@org.springframework.stereotype.Service *) && execution(public * * (..))")
    void forAllPublicServiceMethods(){};
}
