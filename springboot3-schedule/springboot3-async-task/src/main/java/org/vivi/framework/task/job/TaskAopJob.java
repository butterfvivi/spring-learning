package org.vivi.framework.task.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.vivi.framework.task.aspect.DistributedLock;

public class TaskAopJob {

    // 3. 在任务方法上使用注解
 @Scheduled(fixedRate = 5000)
 @DistributedLock("my-unique-task-lock")
 public void executeUniqueTaskAop() {
    // 业务逻辑
    System.out.println("✅ " + Thread.currentThread().getName() + " -> 成功获取锁并执行定时任务...");
 }

}
