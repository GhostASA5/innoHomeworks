package org.project.student.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class.getName());

    @Around("@annotation(LogSpendTime)")
    public Object around(ProceedingJoinPoint joinPoint) {
        logger.debug("Calling from class {} method {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()
        );

        Object[] args = joinPoint.getArgs();
        logger.debug("Method args: {}", Arrays.toString(args));

        Object result;
        long startTime = System.currentTimeMillis();

        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Exception in method: {}", throwable.getMessage());
            throw new RuntimeException("Exception in method: " + throwable.getMessage(), throwable);
        }

        long endTime = System.currentTimeMillis();
        logger.debug("Method result: {}", result);
        logger.debug("Method execution time: {} ms", endTime - startTime);

        return result;
    }
}
