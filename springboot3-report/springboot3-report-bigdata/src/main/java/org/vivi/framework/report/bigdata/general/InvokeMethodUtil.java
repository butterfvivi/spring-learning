package org.vivi.framework.report.bigdata.general;

import java.lang.reflect.Method;

public class InvokeMethodUtil {

    /**
     * 通用反射调用方法
     *
     * @param bean       要调用的对象实例
     * @param methodName 方法名
     * @param args       参数列表（可变参数）
     * @return 方法返回值
     */
    public static Object invokeMethod(Object bean, String methodName, Object... args) {
        try {
            // 获取所有参数类型
            Class<?>[] paramTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i].getClass();
            }

            // 获取 Method 对象
            Method method = bean.getClass().getMethod(methodName, paramTypes);

            // 调用方法并返回结果
            return method.invoke(bean, args);
        } catch (Exception e) {
            throw new RuntimeException("反射调用方法失败: " + methodName, e);
        }
    }
}
