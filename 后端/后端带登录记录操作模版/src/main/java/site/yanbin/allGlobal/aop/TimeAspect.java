package site.yanbin.allGlobal.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//这个切面类方法怎么写呢？

//要使用这个注解我们需要添加依赖
@Component
@Slf4j
@Aspect
public class TimeAspect {
    //然后的话就是找到自己要切入的对象
    //我这里是所有实现类的方法
    @Around("execution(* site.yanbin.service..*.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object obj = pjp.proceed();
        log.info("耗时：{}", System.currentTimeMillis() - start);
        return obj;
    }
}

