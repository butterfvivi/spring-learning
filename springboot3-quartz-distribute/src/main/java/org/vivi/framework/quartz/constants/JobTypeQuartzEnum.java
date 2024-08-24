package org.vivi.framework.quartz.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobTypeQuartzEnum {

    /**
     * 反射java类
     */
    JAVA("1", "反射java类"),

    /**
     * spring bean 的方式
     */
    SPRING_BEAN("2", "spring bean容器实例"),

    /**
     * rest 调用
     */
    REST("3", "rest调用"),

    /**
     * jar
     */
    JAR("4", "jar调用");

    /**
     * 类型
     */
    private final String type;

    /**
     * 描述
     */
    private final String description;
}
