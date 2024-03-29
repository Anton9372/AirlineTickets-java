package airline.tickets.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* airline.tickets.service.*.*(..))"
            + " || execution(* airline.tickets.controller.*.*(..))")
    public void allMethods() {
    }

    @Pointcut("@annotation(AspectAnnotation)")
    public void methodsWithAspectAnnotation() {

    }

    @Before("methodsWithAspectAnnotation()")
    public void logMethodCall(JoinPoint joinPoint) {
        logInfo(joinPoint, "Method called");
    }

    @AfterReturning(pointcut = "methodsWithAspectAnnotation()", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        logInfo(joinPoint, "Method return", "returned: " + result);
    }

    @AfterThrowing(pointcut = "allMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        logInfo(joinPoint, "Exception in", "cause: " + exception.getMessage());
    }

    @Around("methodsWithAspectAnnotation()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        logInfo(joinPoint, "Method executed", "in " + executionTime + "ms");
        return proceed;
    }

    private void logInfo(JoinPoint joinPoint, String message, String additionalInfo) {
        Object[] args = joinPoint.getArgs();
        String fullClassName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.info("{}: {}.{} with args: {} {}", message, fullClassName, methodName, Arrays.toString(args),
                additionalInfo);
    }

    private void logInfo(JoinPoint joinPoint, String message) {
        logInfo(joinPoint, message, "");
    }

}

/*
@Component
@Aspect
@Slf4j
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* airline.tickets.service.*.*(..))"
            + " || execution(* airline.tickets.controller.*.*(..))"
            + " || execution(* airline.tickets.cache.*.*(..))")
    public void allMethods() {
    }

    @Pointcut("@annotation(AspectAnnotation)")
    public void methodsWithAspectAnnotation() {

    }

    @Before("methodsWithAspectAnnotation()")
    public void logMethodCall(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String fullClassName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        LOGGER.info("Method called: {}.{} with args: {}", fullClassName, methodName, Arrays.toString(args));
    }

    @AfterReturning(pointcut = "methodsWithAspectAnnotation()", returning = "result")
    public void logMethodReturn(JoinPoint joinPoint, Object result) {
        String fullClassName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        LOGGER.info("Method return: {}.{} returned {}", fullClassName, methodName, result);
    }

    @AfterThrowing(pointcut = "allMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String fullClassName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        LOGGER.error("Exception in: {}.{} cause: {}", fullClassName, methodName, exception.getMessage());
    }

    @Around("methodsWithAspectAnnotation()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        String fullClassName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        LOGGER.info("Method: {}.{} executed in {}ms", fullClassName, methodName, executionTime);
        return proceed;
    }
}
*/
