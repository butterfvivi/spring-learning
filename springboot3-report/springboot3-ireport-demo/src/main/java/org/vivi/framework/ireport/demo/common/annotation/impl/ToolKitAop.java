package org.vivi.framework.ireport.demo.common.annotation.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.vivi.framework.ireport.demo.common.utils.IAopUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
public class ToolKitAop {
    private static Map<String, Object> map = new HashMap<>();
    private static List list = new ArrayList();

    /**
     * 切入点
     */
    @Pointcut("@annotation(org.vivi.framework.ireport.demo.common.annotation.IToolKit)) ")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            //判断是否包含了特殊参数：HttpServletResponse
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                if (arg instanceof HttpServletResponse) {
                    HttpServletResponse response = (HttpServletResponse) arg;
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                }
            }
            Object proceed = joinPoint.proceed();
            Method method = IAopUtils.getMethod(joinPoint);
            //根据返回值类型，返回对应的空对象
            Class<?> returnType = method.getReturnType();
            if (returnType.getName().equals("void")) return proceed;
            if (proceed instanceof List) {
                return list;
            } else if (proceed instanceof Map) {
                return map;
            } else if (proceed instanceof Object) {
                return null;
            }
            return map;
        } catch (Throwable throwable) {
            return map;
        }
    }
}

