package org.vivi.framework.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import java.util.concurrent.ThreadPoolExecutor; // JDK自带的ThreadPoolExecutor

/**
 * 🚀 定时任务与异步任务的线程池配置类
 * 旨在通过精细化配置，实现任务的高效调度、并发控制与优雅停机。
 */
@Configuration
@EnableScheduling // 启用Spring的定时任务功能
@EnableAsync      // 启用Spring的异步方法执行功能 (如果需要@Async)
public class TaskExecutorConfig implements SchedulingConfigurer {

    /**
     * 1. 核心：为 @Scheduled 任务配置专属的 TaskScheduler (基于 ThreadPoolExecutor)
     * 这个调度器将负责所有被 @Scheduled 注解标记的任务的实际执行。
     *
     * @return 配置好的 ThreadPoolTaskScheduler 实例
     */
    @Bean(destroyMethod = "shutdown") // !!! 重点：确保JVM关闭时，线程池能优雅关闭
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        // 核心线程池大小：根据任务性质调整，这里设置为CPU核心数加一，通常适用于混合型或CPU密集型任务
        // 对于I/O密集型任务，可以适当调大。
        scheduler.setPoolSize(Runtime.getRuntime().availableProcessors() + 1);
        // 线程名称前缀：便于日志追踪和问题排查 🔍
        scheduler.setThreadNamePrefix("scheduled-task-pool-");
        // 设置拒绝策略：当线程池和队列都满时，由提交任务的线程来执行，提供反压能力，防止任务丢失
        // 也可以选择自定义 RejectedExecutionHandler 来实现更复杂的策略（如持久化、发送告警）
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 优雅停机：等待所有已提交的任务完成，最长等待60秒
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        // 初始化线程池
        scheduler.initialize();
        System.out.println("🎉 定时任务调度器 ThreadPoolTaskScheduler 初始化完成！");
        return scheduler;
    }

    /**
     * 2. 配置 Spring Scheduling 使用我们自定义的 TaskScheduler
     * 通过实现 SchedulingConfigurer 接口，将自定义的 TaskScheduler 注入到 Spring 的调度机制中。
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }

    /**
     * 3. (可选) 为 @Async 方法配置独立的 TaskExecutor (基于 ThreadPoolExecutor)
     * 如果您的应用中除了定时任务，还有其他通过 @Async 注解实现的异步方法，
     * 建议为它们配置一个独立的线程池，以避免相互影响。
     *
     * @return 配置好的 ThreadPoolTaskExecutor 实例
     */
    @Bean("asyncTaskExecutor") // 为此Bean指定一个名称，以便@Async("asyncTaskExecutor")引用
    public ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数：根据异步任务的并发需求和系统资源确定
        executor.setCorePoolSize(5);
        // 最大线程数：上限，防止线程无限增长导致资源耗尽
        executor.setMaxPoolSize(20);
        // 队列容量：缓冲等待执行的任务。有界队列是防止OOM的关键！
        executor.setQueueCapacity(100);
        // 线程空闲存活时间：当线程数超过核心线程数时，空闲线程的销毁时间
        executor.setKeepAliveSeconds(60);
        // 线程名称前缀：方便异步任务的日志追踪 🔍
        executor.setThreadNamePrefix("async-task-pool-");
        // 拒绝策略：同样使用 CallerRunsPolicy，保证任务不丢失
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 优雅停机：等待所有异步任务完成，最长等待60秒
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        // 初始化线程池
        executor.initialize();
        System.out.println("🎉 异步任务执行器 ThreadPoolTaskExecutor (asyncTaskExecutor) 初始化完成！");
        return executor;
    }

    // 示例：一个使用 @Scheduled 和 @Async 的服务类
    // @Service
    // public class MyTaskService {
    //
    //     private final AtomicInteger scheduledTaskCount = new AtomicInteger(0);
    //     private final AtomicInteger asyncTaskCount = new AtomicInteger(0);
    //
    //     // 使用自定义的调度器执行定时任务
    //     @Scheduled(fixedRate = 5000) // 每5秒执行一次
    //     public void executeScheduledTask() {
    //         int count = scheduledTaskCount.incrementAndGet();
    //         System.out.println(Thread.currentThread().getName() + " -> 执行定时任务，第 " + count + " 次...");
    //         try {
    //             // 模拟耗时操作
    //             TimeUnit.SECONDS.sleep(2);
    //         } catch (InterruptedException e) {
    //             Thread.currentThread().interrupt();
    //         }
    //     }
    //
    //     // 使用自定义的异步执行器执行异步方法
    //     @Async("asyncTaskExecutor") // 指定使用名为 "asyncTaskExecutor" 的线程池
    //     public CompletableFuture<String> performAsyncTask() {
    //         int count = asyncTaskCount.incrementAndGet();
    //         System.out.println(Thread.currentThread().getName() + " -> 执行异步任务，第 " + count + " 次...");
    //         try {
    //             TimeUnit.SECONDS.sleep(3); // 模拟耗时操作
    //         } catch (InterruptedException e) {
    //             Thread.currentThread().interrupt();
    //         }
    //         return CompletableFuture.completedFuture("异步任务完成!");
    //     }
    //
    //     @Scheduled(fixedDelay = 10000) // 每10秒调度一次异步任务
    //     public void triggerAsyncTask() {
    //         System.out.println(Thread.currentThread().getName() + " -> 调度异步任务...");
    //         performAsyncTask().thenAccept(result ->
    //             System.out.println(Thread.currentThread().getName() + " -> 接收到异步任务结果: " + result));
    //     }
    // }
}
