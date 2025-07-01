package org.vivi.framework.task.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
 @Retention(RetentionPolicy.RUNTIME)
 public @interface DistributedLock {

     String value(); // 锁的名称

     long waitTime() default 0; // 等待时间，默认0秒立即返回

     long leaseTime() default 30; // 锁自动释放时间，默认30秒

     TimeUnit timeUnit() default TimeUnit.SECONDS;
 }
