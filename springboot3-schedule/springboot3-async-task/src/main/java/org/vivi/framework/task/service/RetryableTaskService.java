package org.vivi.framework.task.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 带有Spring Retry机制的任务服务
 */
@Service
public class RetryableTaskService {

    private final AtomicInteger attemptCount = new AtomicInteger(0);

    /**
     * 🤖 带有重试机制的业务方法
     *
     * @Retryable:
     * - value = {RuntimeException.class}: 遇到 RuntimeException 时进行重试
     * - maxAttempts = 3: 最多重试3次（第一次执行 + 2次重试）
     * - backoff = @Backoff(delay = 2000, multiplier = 1.5):
     * - delay = 2000: 第一次重试延迟2秒
     * - multiplier = 1.5: 每次重试延迟时间是上次的1.5倍（指数退避）
     */
    @Retryable(value = {RuntimeException.class}, maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public String performRiskyOperation(String input) {
        int currentAttempt = attemptCount.incrementAndGet();
        System.out.println("⏳ " + Thread.currentThread().getName() + " -> 尝试执行任务：'" + input + "'，当前是第 " + currentAttempt + " 次尝试，时间: " + LocalTime.now());

        // 模拟失败场景：前两次失败，第三次成功
        if (currentAttempt < 3) {
            throw new RuntimeException("模拟任务执行失败，需要重试！");
        }

        System.out.println("🎉 " + Thread.currentThread().getName() + " -> 任务 '" + input + "' 第 " + currentAttempt + " 次尝试成功！");
        attemptCount.set(0); // 重置计数器，以便下次调用能再次模拟失败
        return "任务成功完成!";
    }

    /**
     * 🚨 恢复方法 (Fallback Method)
     * 当所有重试都失败后，会调用此方法进行恢复或兜底处理。
     * 参数类型和返回类型需要与 @Retryable 方法匹配，并且第一个参数是导致重试失败的异常。
     */
    @Recover
    public String recoverFromRiskyOperation(RuntimeException e, String input) {
        System.err.println("💔 " + Thread.currentThread().getName() + " -> 任务 '" + input + "' 经过多次重试后仍然失败！最终失败原因: " + e.getMessage() + "。进行恢复处理...");
        // 在这里可以进行告警、记录日志、将任务发送到死信队列等操作
        return "任务最终失败，已进入恢复流程。";
    }

    // 可以在定时任务中调用这个重试方法
    // @Scheduled(fixedRate = 10000)
    // public void triggerRetryableTask() {
    //     System.out.println("\n--- 触发带有重试的定时任务 ---");
    //     try {
    //         String result = performRiskyOperation("重要数据处理");
    //         System.out.println("任务调度完成，结果: " + result);
    //     } catch (Exception e) {
    //         System.err.println("任务调度异常: " + e.getMessage());
    //     }
    // }
}
