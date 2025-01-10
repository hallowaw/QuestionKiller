package site.yanbin.allGlobal.allexception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.yanbin.common.result.Result;

@RestControllerAdvice // 全局异常处理器  @RestControllerAdvice = @ControllerAdvice + @ResponseBody
// @ControllerAdvice 相当于是@Companent 创建全局异常处理对象交给spring 容器
// @ResponseBody 写出去数据给前端。 响应给浏览器的前端
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class) // 配置要拦截所有异常
    public Result handle(Exception e) {
        log.error("服务器异常：异常的原因是：{}", e.getMessage());
        return Result.error("对不起，很遗憾，本次操作出现问题，给您带来困扰，请尝试！");
        //   return Result.error(e.getMessage());
    }
}
