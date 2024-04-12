package airline.tickets.aspect;

import airline.tickets.counter.RequestCounter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RequestCounterAspect {
    RequestCounter requestCounterService;

    @Pointcut("@annotation(RequestCounterAnnotation)")
    public void methodsWithRequestCounterAnnotation() {

    }

    @Before("methodsWithRequestCounterAnnotation")
    public void logRequestCounterIncrement(final JoinPoint joinPoint) {
        requestCounterService.increment();
        //loggggg in LoggingAspect
    }

}
