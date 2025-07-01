package org.vivi.framework.task.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 带有分布式锁的定时任务示例
 */
@Component
public class DistributedScheduledTask {

    private final RedissonClient redissonClient;
    private final AtomicInteger executionCount = new AtomicInteger(0);

    public DistributedScheduledTask(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 🚀 使用 Redisson 分布式锁确保任务唯一执行
     * 只有成功获取到 "my-unique-task-lock" 锁的节点才会执行任务。
     */
    @Scheduled(fixedRate = 5000) // 每5秒尝试执行一次
    public void executeUniqueTask() {
        // 定义锁的名称，确保在所有节点上一致
        String lockKey = "my-unique-task-lock";
        RLock lock = redissonClient.getLock(lockKey);

        // 尝试获取锁，设置最长等待时间和锁的过期时间
        // waitTime: 尝试获取锁的最大等待时间，超过这个时间还没获取到就放弃
        // leaseTime: 锁的持有时间，超过这个时间锁会自动释放。
        //            如果设置为 -1，则启用 Redisson 的看门狗机制，自动续期，直到业务执行完成。
        try {
            boolean acquired = lock.tryLock(0, 30, TimeUnit.SECONDS); // 立即尝试获取，如果获取不到则放弃，获取到后锁最长持有30秒
            if (acquired) {
                try {
                    int count = executionCount.incrementAndGet();
                    System.out.println("✅ " + Thread.currentThread().getName() + " -> 成功获取锁并执行定时任务，第 " + count + " 次...");
                    // 模拟耗时业务逻辑
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println("🎉 " + Thread.currentThread().getName() + " -> 定时任务执行完毕，释放锁。");
                } finally {
                    // 确保锁在任务执行完毕后被释放，即使任务抛出异常也要释放
                    if (lock.isLocked() && lock.isHeldByCurrentThread()) { // 再次检查是否被当前线程持有，以防万一
                        lock.unlock();
                    }
                }
            } else {
                System.out.println("❌ " + Thread.currentThread().getName() + " -> 未能获取到锁，跳过任务执行。");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("💔 " + Thread.currentThread().getName() + " -> 获取锁时被中断: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("🚨 " + Thread.currentThread().getName() + " -> 任务执行过程中发生异常: " + e.getMessage());
            // 异常时也确保锁被释放
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

