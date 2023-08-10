package song.devlog1.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointCuts {

    @Pointcut("execution(* song.devlog1.controller.*.*(..))")
    public void controllerPointcut() {
    }

    @Pointcut("execution(* song.devlog1.service.*.*(..))")
    public void servicePointcut() {
    }

    @Pointcut("execution(* song.devlog1.repository.*.*(..))")
    public void repositoryPointcut() {
    }
}
