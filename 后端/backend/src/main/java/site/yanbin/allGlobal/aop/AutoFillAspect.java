package site.yanbin.allGlobal.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import site.yanbin.allGlobal.annotation.AutoFill;
import site.yanbin.common.constant.AutoFillConstant;
import site.yanbin.common.context.BaseContext;
import site.yanbin.common.enumeration.OperationType;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* site.yanbin.mapper.*.*(..)) && @annotation(site.yanbin.allGlobal.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段自动填充...");

        //获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value();//获得数据库操作类型

        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }

        Object entity = args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        if(operationType == OperationType.INSERT){
            // 为4个公共字段赋值
            try {
                Method setCreatedAt = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATED_AT, LocalDateTime.class);
                Method setCreatedBy = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATED_BY, Long.class);
                Method setUpdatedAt = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATED_AT, LocalDateTime.class);
                Method setUpdatedBy = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATED_BY, Long.class);

                // 通过反射为对象属性赋值
                setCreatedAt.invoke(entity, now);
                setCreatedBy.invoke(entity, currentId);
                setUpdatedAt.invoke(entity, now);
                setUpdatedBy.invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType == OperationType.UPDATE){
            // 为2个公共字段赋值
            try {
                Method setUpdatedAt = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATED_AT, LocalDateTime.class);
                Method setUpdatedBy = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATED_BY, Long.class);

                // 通过反射为对象属性赋值
                setUpdatedAt.invoke(entity, now);
                setUpdatedBy.invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}




