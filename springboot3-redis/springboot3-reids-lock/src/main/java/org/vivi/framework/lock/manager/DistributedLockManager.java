package org.vivi.framework.lock.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vivi.framework.lock.model.LockResult;
import org.vivi.framework.lock.monitor.LockHealthChecker;
import org.vivi.framework.lock.monitor.LockMonitor;
import org.vivi.framework.lock.service.LockStrategy;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * 分布式锁管理器
 * 提供统一的分布式锁管理接口，支持多种锁策略
 */
@Slf4j
@Component
public class DistributedLockManager {

    @Autowired
    private Map<String, LockStrategy> lockStrategies;

    @Autowired
    private LockMonitor lockMonitor;

    @Autowired
    private LockHealthChecker lockHealthChecker;

    private static final String DEFAULT_STRATEGY = "redis";

    /**
     * 尝试获取锁（使用默认策略）
     */
    public LockResult tryLock(String lockKey, long expireTime) {
        return tryLock(lockKey, DEFAULT_STRATEGY, expireTime);
    }

    /**
     * 尝试获取锁（指定策略）
     */
    public LockResult tryLock(String lockKey, String strategy, long expireTime) {
        LockStrategy lockStrategy = lockStrategies.get(strategy);
        if (lockStrategy == null) {
            throw new IllegalArgumentException("不支持的锁策略: " + strategy);
        }

        String lockValue = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        try {
            LockResult result = lockStrategy.tryLock(lockKey, lockValue, expireTime);

            // 记录监控信息
            lockMonitor.recordLockAttempt(lockKey, strategy, result.isSuccess(),
                    System.currentTimeMillis() - startTime);

            return result;
        } catch (Exception e) {
            lockMonitor.recordLockError(lockKey, strategy, e);
            throw e;
        }
    }

    /**
     * 释放锁（使用默认策略）
     */
    public boolean unlock(String lockKey, String lockValue) {
        return unlock(lockKey, lockValue, DEFAULT_STRATEGY);
    }

    /**
     * 释放锁（指定策略）
     */
    public boolean unlock(String lockKey, String lockValue, String strategy) {
        LockStrategy lockStrategy = lockStrategies.get(strategy);
        if (lockStrategy == null) {
            throw new IllegalArgumentException("不支持的锁策略: " + strategy);
        }

        try {
            boolean result = lockStrategy.unlock(lockKey, lockValue);
            lockMonitor.recordUnlock(lockKey, strategy, result);
            return result;
        } catch (Exception e) {
            lockMonitor.recordUnlockError(lockKey, strategy, e);
            throw e;
        }
    }

    /**
     * 自动锁执行（带返回值）
     * 推荐使用此方法，自动管理锁的获取和释放
     */
    public <T> T executeWithLock(String lockKey, long expireTime, Supplier<T> supplier) {
        LockResult lockResult = tryLock(lockKey, expireTime);
        if (!lockResult.isSuccess()) {
            throw new LockAcquisitionException("获取锁失败: " + lockKey);
        }

        try {
            return supplier.get();
        } finally {
            unlock(lockKey, lockResult.getLockValue());
        }
    }

    /**
     * 自动锁执行（无返回值）
     * 推荐使用此方法，自动管理锁的获取和释放
     */
    public void executeWithLock(String lockKey, long expireTime, Runnable runnable) {
        LockResult lockResult = tryLock(lockKey, expireTime);
        if (!lockResult.isSuccess()) {
            throw new LockAcquisitionException("获取锁失败: " + lockKey);
        }

        try {
            runnable.run();
        } finally {
            unlock(lockKey, lockResult.getLockValue());
        }
    }

    /**
     * 带重试的锁执行
     */
    public <T> T executeWithRetry(String lockKey, long expireTime,
                                  Supplier<T> supplier, int maxRetries) {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                return executeWithLock(lockKey, expireTime, supplier);
            } catch (LockAcquisitionException e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw e;
                }

                // 指数退避重试
                long delay = (long) Math.pow(2, retryCount) * 100;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("重试被中断", ie);
                }
            }
        }
        throw new RuntimeException("重试次数超限");
    }

    /**
     * 检查锁是否存在
     */
    public boolean isLocked(String lockKey) {
        return isLocked(lockKey, DEFAULT_STRATEGY);
    }

    /**
     * 检查锁是否存在（指定策略）
     */
    public boolean isLocked(String lockKey, String strategy) {
        LockStrategy lockStrategy = lockStrategies.get(strategy);
        if (lockStrategy == null) {
            throw new IllegalArgumentException("不支持的锁策略: " + strategy);
        }

        try {
            return lockStrategy.isLocked(lockKey);
        } catch (UnsupportedOperationException e) {
            log.warn("锁策略不支持检查锁状态: {}", strategy);
            return false;
        }
    }

    /**
     * 获取锁监控器
     */
    public LockMonitor getLockMonitor() {
        return lockMonitor;
    }

    /**
     * 获取锁健康检查器
     */
    public LockHealthChecker getLockHealthChecker() {
        return lockHealthChecker;
    }
}