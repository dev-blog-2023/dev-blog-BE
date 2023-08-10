package song.devlog1.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TimeLogAspect {

    @Aspect
    @Order(1)
    @Component
    @RequiredArgsConstructor
    private static class ControllerTimeLogAspect {

        @Around("song.devlog1.aop.PointCuts.controllerPointcut()")
        public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[Controller Time Log] {}.{}({})",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                    joinPoint.getArgs());

            Object result = null;
            Long startTime = System.currentTimeMillis();
            try {
                result = joinPoint.proceed();
            } catch (Exception e) {
                throw e;
            } finally {
                Long endTime = System.currentTimeMillis();
                Long resultTime = endTime - startTime;
                log.info("[Controller Time Log] result time = {}ms", resultTime);
            }

            return result;
        }
    }

    @Aspect
    @Order(2)
    @Component
    @RequiredArgsConstructor
    private static class ServiceTimeLogAspect {

        @Around("song.devlog1.aop.PointCuts.servicePointcut()")
        public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[Service Time Log] {}.{}({})",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                    joinPoint.getArgs());

            Object result = null;
            Long startTime = System.currentTimeMillis();
            try {
                result = joinPoint.proceed();
            } catch (Exception e) {
                throw e;
            } finally {
                Long endTime = System.currentTimeMillis();
                Long resultTime = endTime - startTime;
                log.info("[Service Time Log] result time = {}ms", resultTime);
            }

            return result;
        }
    }

    @Aspect
    @Order(3)
    @Component
    @RequiredArgsConstructor
    private static class RepositoryTimeLogAspect {

        @Around("song.devlog1.aop.PointCuts.repositoryPointcut()")
        public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[Repository Time Log] {}.{}({})",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                    joinPoint.getArgs());

            Object result = null;
            Long startTime = System.currentTimeMillis();
            try {
                result = joinPoint.proceed();
            } catch (Exception e) {
                throw e;
            } finally {
                Long endTime = System.currentTimeMillis();
                Long resultTime = endTime - startTime;
                log.info("[Repository Time Log] result time = {}ms", resultTime);
            }

            return result;
        }
    }
}
