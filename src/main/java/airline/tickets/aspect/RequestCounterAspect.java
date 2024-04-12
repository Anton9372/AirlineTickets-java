package airline.tickets.aspect;

import airline.tickets.counter.RequestCounter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class RequestCounterAspect {

    @Pointcut("@annotation(RequestCounterAnnotation)")
    public void methodsWithRequestCounterAnnotation() {

    }

    @Before("methodsWithRequestCounterAnnotation()")
    public void requestCounterIncrementAndLogIt(final JoinPoint joinPoint) {
        RequestCounter.increment();
        log.info("Increment requestCounter from {}.{}()." + "Current value of requestCounter is {}",
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                RequestCounter.getCount());
    }
}
