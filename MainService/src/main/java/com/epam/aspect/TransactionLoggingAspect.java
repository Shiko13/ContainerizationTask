package com.epam.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Aspect
@Component
public class TransactionLoggingAspect {

    public static final String TRANSACTION_ID_KEY = "transactionId";

    @Around("execution(* com.epam.*.*.*(..)) && !execution(* com.epam.filter.*.*(..))")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        String transactionId = generateTransactionId();
        MDC.put(TRANSACTION_ID_KEY, transactionId);

        String methodName = joinPoint.getSignature().toShortString();

        log.info("Transaction {} - Entering method: {}", transactionId, methodName);

        Object result;

        try {
            result = joinPoint.proceed();
            log.info("Transaction {} - Method {} executed successfully. Response: {}", transactionId, methodName, result);
            return result;
        } catch (Exception e) {
            log.error("Transaction {} - Method {} failed with exception: {}", transactionId, methodName, e.getMessage());
            throw e;
        } finally {
            log.info("Transaction {} - Exiting method: {}", transactionId, methodName);
        }
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}
