package org.vivi.framework.report.simple.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

public class ApplicationContextUtils implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;

    }

    /**
     * 根据名称和类型获取SpringBean
     *
     * @param name
     * @param requireType
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> requireType) {
        return applicationContext.getBean(name, requireType);
    }

    /**
     * 根据名称和类型获取SpringBean
     *
     * @param requireType
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> requireType) {
        return applicationContext.getBean(requireType);
    }


    /**
     * 判断bean是否存在
     * @param beanname
     * @return
     */
    public static boolean containsBean(String beanname){
        if(StringUtils.isBlank(beanname)){
            return false;
        }
        return applicationContext.containsBean(beanname);
    }

    /**
     * 发布事件
     *
     * @param applicationEvent 事件
     */
    public static void publishEvent(ApplicationEvent applicationEvent) {
        //设置事件发布异步执行
        /*SimpleApplicationEventMulticaster applicationEventMulticaster =
                applicationContext.getBean(GAEA_ASYN_APPLICATION_EVENT_MULTICASTER_BEAN_NAME, SimpleApplicationEventMulticaster.class);
        applicationEventMulticaster.multicastEvent(applicationEvent);*/
        applicationContext.getBean(SimpleApplicationEventMulticaster.class).multicastEvent(applicationEvent);
    }


    /**
     * 发布同步事件
     *
     * @param applicationEvent 事件
     */
    public static void publishSynEvent(ApplicationEvent applicationEvent) {
        //设置事件发布异步执行
        applicationContext.publishEvent(applicationEvent);
    }
}

