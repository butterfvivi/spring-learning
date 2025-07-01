package org.vivi.framework.task.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

// 2. 定义 AOP 切面
 @Aspect
 @Component
 public class DistributedLockAspect {

     @Autowired
     private RedissonClient redissonClient;

     @Around("@annotation(distributedLock)")
     public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
         String lockKey = distributedLock.value();
         long waitTime = distributedLock.waitTime();
         long leaseTime = distributedLock.leaseTime();
         TimeUnit timeUnit = distributedLock.timeUnit();

         RLock lock = redissonClient.getLock(lockKey);
         boolean acquired = false;
         try {
             acquired = lock.tryLock(waitTime, leaseTime, timeUnit);
             if (acquired) {
                 return joinPoint.proceed(); // 执行原方法
             } else {
                 System.out.println("❌ " + Thread.currentThread().getName() + " -> 未能获取到锁[" + lockKey + "]，跳过任务。");
                 // 可以选择抛出异常，或者静默跳过
                 return null;
             }
         } finally {
             if (acquired) { // 确保是当前线程成功获取的锁才释放
                 if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                     lock.unlock();
                 }
             }
         }
     }
 }