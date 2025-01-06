package hello.example.porthub.config.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)") // 모든 @GetMapping 메서드에 적용
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed(); // 실제 메서드 실행
        long endTime = System.nanoTime();
        System.out.println(joinPoint.getSignature() + " executed in " + (endTime - startTime) / 1_000_000 + " ms");
        return result;
    }
}
