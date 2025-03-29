package org.vivi.framework.ireport.demo.common.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * aop常用的一些工具
 */
public class IAopUtils {


    /**
     * 获取切点
     * **/
    public static MethodSignature getMethodSignature(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature;
    }


    /**
     * 获取指定切点的执行的方法对象
     *
     * @param joinPoint
     * @return
     * @author mashuai
     */

    public static Method getMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature =getMethodSignature(joinPoint);
        Method method = methodSignature.getMethod();
        return method;
    }



}
