package site.yanbin.allGlobal.interceptor;

import com.alibaba.fastjson.JSONObject;
import site.yanbin.common.context.BaseContext;
import site.yanbin.allGlobal.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 1、定义拦截器：实现拦截器接口 HandlerInterceptor 从写方法preHandle处理拦截。
 * 2、加上注解 @Component 把拦截器对象交给spring 容器管理。
 * 3、注册拦截器给spring容器，让拦截器对象生效。
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器拦截请求：{}", request.getRequestURI());
        log.info("请求来源：{}", request.getHeader("Origin"));

        // 放行 OPTIONS 请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Max-Age", "3600");
            log.info("OPTIONS 请求直接放行：{}", request.getRequestURI());
            return true;
        }

        // ... 其余代码保持不变


        // 校验 token
        String token = request.getHeader("token");
        try {
            // 解析 JWT 获取 claims
            Map<String, Object> claims = jwtUtils.parseJWT(token);

            // 获取用户ID
            Long userId = Long.valueOf(claims.get("id").toString());

            // 将用户ID保存到 BaseContext 中
            BaseContext.setCurrentId(userId);

            log.info("当前用户ID：{}", userId);
            log.info("拦截器放行请求：{}", request.getRequestURI());

            return true; // 放行
        } catch (Exception e) {
            // jwt令牌非法，返回错误信息
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("msg", "NOT_LOGIN");
            result.put("data", null);

            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write(result.toJSONString());

            log.info("拦截器拦截请求 - token失效：{}", request.getRequestURI());
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除 BaseContext 中的用户ID，防止内存泄漏
        BaseContext.removeCurrentId();
        log.info("清理当前线程的用户ID：{}", request.getRequestURI());
    }
}