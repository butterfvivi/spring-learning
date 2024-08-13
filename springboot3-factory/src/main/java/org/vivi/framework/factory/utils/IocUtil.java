package org.vivi.framework.factory.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 从容器获取bean，让方法可以被静态调用
 */
@Component
public class IocUtil implements ApplicationContextAware {
    //ioc容器
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //移除ioc
        this.applicationContext = applicationContext;
    }

    //根据类.class获取bean
    public static <T> T getBean(Class<T> tClass) throws BeansException {
        return applicationContext.getBean(tClass);
    }

    //根据bean名称获取bean
    public static <T> T getBean(String className) {
        return (T) applicationContext.getBean(className);
    }

    //名称+class对象获取bean
    public static Object getBean(String name, Class cla) throws BeansException {
        return applicationContext.getBean(name, cla);
    }

    //获取指定类型的所有子类bean名称
    public static String[] getBeanNames(Class<?> clazz) {
        return applicationContext.getBeanNamesForType(clazz);
    }

    //获取指定注解，被哪些class使用
    public static <T extends Annotation> Map<String, Object> getBeansWithAnnotation(Class<T> tClass) {
        return applicationContext.getBeansWithAnnotation(tClass);
    }


    /**
     * 反射获取bean对象，保证在spring生命周期内
     */

    public static Object getClassObj(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();
        String firstLowerName = simpleName.substring(0, 1).toLowerCase()
                + simpleName.substring(1);
        //防止cglb代理，无法获取实际需要的bean名称
        if (firstLowerName.contains("$$")) firstLowerName = firstLowerName.split("\\$")[0];
        return IocUtil.getBean(firstLowerName, clazz);
    }

    /**
     * 获取类/方法上某个注解的信息，好处是可以兼容cglb动态代理,否则一旦项目开启了cglb代理，就无法获取类上或方法上的注解信息
     **/
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, @Nullable Class<A> annotationType) {
        return AnnotationUtils.findAnnotation(clazz, annotationType);
    }

    public static <A extends Annotation> A findAnnotation(Method method, @Nullable Class<A> annotationType) {
        return AnnotationUtils.findAnnotation(method, annotationType);
    }

    /**
     * 获取某个接口的实现层对象
     *
     * @param interfacesClass 需要检查的接口类对象
     * @return 对应的接口实现层对象。如果一个接口被多个类实现，将返回null。
     * @author mashuai
     */

    public static <T> T getInterfaceImpl(Class<T> interfacesClass) {
        String[] beanNames = IocUtil.getBeanNames(interfacesClass);
        if (beanNames == null || beanNames.length != 2) {
            return null;
        }
        return (T) IocUtil.getBean(beanNames[0], interfacesClass);
    }


    /**
     * 想容器注入一个bean
     *
     * @param beanName bean名称
     * @param t        实例
     * @return
     * @author mashuai
     */

    public static <T> void registerBean(String beanName, T t) {
        registerBeanM(t, beanName);
    }

    public static <T> void registerBean(T t) {
        registerBeanM(t, t.getClass().getName());
    }

    private static <T> void registerBeanM(T t, String beanName) {
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        beanFactory.registerSingleton(beanName, t);
    }

}