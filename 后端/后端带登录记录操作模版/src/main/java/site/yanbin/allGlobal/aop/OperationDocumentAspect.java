package site.yanbin.allGlobal.aop;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.yanbin.common.entity.OperateLog;
import site.yanbin.mapper.OperateLogMapper;
import site.yanbin.allGlobal.util.JWTUtils;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
@Aspect
@Component
public class OperationDocumentAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JWTUtils jwtUtils;

    @Around("execution(* site.yanbin.controller.*.*(..))") // 修改为Controller层包路径
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取类名、方法名、参数等信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        Object[] methodParams = joinPoint.getArgs();

        // 操作时间和耗时计算
        LocalDateTime operateTime = LocalDateTime.now();
        long startTime = System.currentTimeMillis();

        // 执行方法
        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            long costTime = System.currentTimeMillis() - startTime;

            // 获取返回值
            String returnValue = result != null ? result.toString() : "void";

            // 通过解密令牌来获取用户的id
            String token = request.getHeader("token");
            Integer operateUserId = null;
            if (token != null && !token.isEmpty()) {
                try {
                    Claims claims = jwtUtils.parseJWT(token);
                    operateUserId = (Integer) claims.get("id");
                } catch (Exception e) {
                    // 处理解析令牌时的异常
                    System.err.println("令牌解析失败：" + e.getMessage());
                }
            }

            // 记录日志
            OperateLog log = new OperateLog();
            log.setOperateUser(operateUserId);
            log.setOperateTime(operateTime);
            log.setClassName(className);
            log.setMethodName(methodName);
            log.setMethodParams(Arrays.toString(methodParams));
            log.setReturnValue(returnValue);
            log.setCostTime(costTime);

            // 保存日志（持久化到数据库）
            saveOperationLog(log);
        }

        return result;
    }

    // 保存操作日志的方法
    private void saveOperationLog(OperateLog log) {
        // 将日志持久化到数据库或其他存储系统
        operateLogMapper.insert(log);
        System.out.println("操作日志：" + log);
    }
}

