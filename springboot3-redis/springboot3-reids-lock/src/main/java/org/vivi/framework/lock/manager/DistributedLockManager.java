package org.vivi.framework.lock.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vivi.framework.lock.metrics.LockStatistics;
import org.vivi.framework.lock.model.LockResult;
import org.vivi.framework.lock.monitor.LockHealthChecker;
import org.vivi.framework.lock.monitor.LockMonitor;
import org.vivi.framework.lock.service.LockStrategy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁管理器
 * 提供统一的分布式锁获取和释放接口
 */
@Slf4j
@Component
public class DistributedLockManager {

    @Autowired
    private LockStrategy lockStrategy;

    @Autowired
    private LockMonitor lockMonitor;

    @Autowired
    private LockHealthChecker healthChecker;

    // 锁持有者映射，用于实现可重入锁
    private final ConcurrentHashMap<String, LockHolder> lockHolders = new ConcurrentHashMap<>();

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey 锁的键
     * @param timeout 超时时间
     * @return 锁结果
     */
    public LockResult tryLock(String lockKey, long timeout) {
        return tryLock(lockKey, timeout,TimeUnit.SECONDS, false);
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey 锁的键
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @return 锁结果
     */
    public LockResult tryLock(String lockKey, long timeout, TimeUnit timeUnit) {
        return tryLock(lockKey, timeout, timeUnit, false);
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey 锁的键
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @param reentrant 是否可重入
     * @return 锁结果
     */
    public LockResult tryLock(String lockKey, long timeout, TimeUnit timeUnit, boolean reentrant) {
        long startTime = System.currentTimeMillis();
        String normalizedKey = LockNamingConvention.normalizeKey(lockKey);

        try {
            // 检查健康状态
            if (!healthChecker.isHealthy()) {
                log.warn("Lock service is not healthy, lock acquisition may fail");
            }

            // 检查可重入锁
            if (reentrant && isReentrantLock(normalizedKey)) {
                LockHolder holder = lockHolders.get(normalizedKey);
                holder.incrementCount();
                log.debug("Reentrant lock acquired for key: {}, count: {}", normalizedKey, holder.getCount());
                return LockResult.success(normalizedKey, timeout);
            }

            // 尝试获取锁
            LockResult result = lockStrategy.tryLock(normalizedKey, timeout, timeUnit);

            if (result.isSuccess()) {
                // 记录锁持有者
                if (reentrant) {
                    lockHolders.put(normalizedKey, new LockHolder(result.getLockValue(), 1));
                }

                // 记录监控指标
                lockMonitor.recordLockAttempt(normalizedKey, lockStrategy.getStrategyName(), true, System.currentTimeMillis() - startTime);
                log.info("Lock acquired successfully for key: {}, lockId: {}", normalizedKey, result.getLockValue());
            } else {
                lockMonitor.recordLockAttempt(normalizedKey, lockStrategy.getStrategyName(), false, System.currentTimeMillis() - startTime);
                log.warn("Failed to acquire lock for key: {}, reason: {}", normalizedKey, result.getErrorMessage());
            }

            return result;

        } catch (Exception e) {
            lockMonitor.recordLockError(normalizedKey, lockStrategy.getStrategyName(), e);
            log.error("Exception occurred while acquiring lock for key: {}", normalizedKey, e);
            return LockResult.failure(e.getMessage());
        }
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey 锁的键
     * @param lockId 锁ID
     * @return 是否成功释放
     */
    public boolean unlock(String lockKey, String lockId) {
        return unlock(lockKey, lockId, false);
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey 锁的键
     * @param lockId 锁ID
     * @param reentrant 是否可重入
     * @return 是否成功释放
     */
    public boolean unlock(String lockKey, String lockId, boolean reentrant) {
        String normalizedKey = LockNamingConvention.normalizeKey(lockKey);

        try {
            // 检查可重入锁
            if (reentrant && isReentrantLock(normalizedKey)) {
                LockHolder holder = lockHolders.get(normalizedKey);
                if (holder != null && holder.getLockId().equals(lockId)) {
                    holder.decrementCount();
                    if (holder.getCount() <= 0) {
                        // 计数器归零，真正释放锁
                        lockHolders.remove(normalizedKey);
                        boolean result = lockStrategy.unlock(normalizedKey);
                        lockMonitor.recordUnlock(normalizedKey, lockStrategy.getStrategyName(), result);
                        if (result) {
                            log.info("Reentrant lock released for key: {}, lockId: {}", normalizedKey, lockId);
                        }
                        return result;
                    } else {
                        log.debug("Reentrant lock count decreased for key: {}, count: {}", normalizedKey, holder.getCount());
                        return true;
                    }
                }
            }

            // 直接释放锁
            boolean result = lockStrategy.unlock(normalizedKey);
            lockMonitor.recordUnlock(normalizedKey, lockStrategy.getStrategyName(), result);
            if (result) {
                lockHolders.remove(normalizedKey);
                log.info("Lock released successfully for key: {}, lockId: {}", normalizedKey, lockId);
            } else {
                log.warn("Failed to release lock for key: {}, lockId: {}", normalizedKey, lockId);
            }

            return result;

        } catch (Exception e) {
            lockMonitor.recordUnlockError(normalizedKey, lockStrategy.getStrategyName(), e);
            log.error("Exception occurred while releasing lock for key: {}", normalizedKey, e);
            return false;
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
     * 检查锁是否被持有
     *
     * @param lockKey 锁的键
     * @return 是否被持有
     */
    public boolean isLocked(String lockKey) {
        String normalizedKey = LockNamingConvention.normalizeKey(lockKey);
        return lockStrategy.isLocked(normalizedKey);
    }

    /**
     * 强制释放锁（管理员操作）
     *
     * @param lockKey 锁的键
     * @return 是否成功释放
     */
    public boolean forceUnlock(String lockKey) {
        String normalizedKey = LockNamingConvention.normalizeKey(lockKey);
        try {
            boolean result = lockStrategy.forceUnlock(normalizedKey);
            lockMonitor.recordUnlock(normalizedKey, lockStrategy.getStrategyName(), result);
            if (result) {
                lockHolders.remove(normalizedKey);
                log.warn("Lock force released for key: {}", normalizedKey);
            }
            return result;
        } catch (Exception e) {
            lockMonitor.recordUnlockError(normalizedKey, lockStrategy.getStrategyName(), e);
            log.error("Exception occurred while force releasing lock for key: {}", normalizedKey, e);
            return false;
        }
    }

    /**
     * 获取锁统计信息
     *
     * @return 锁统计信息
     */
    public LockStatistics getStatistics() {
        return lockMonitor.getStatistics();
    }

    /**
     * 检查是否为可重入锁
     */
    private boolean isReentrantLock(String lockKey) {
        LockHolder holder = lockHolders.get(lockKey);
        return holder != null && holder.getCount() > 0;
    }

    /**
     * 锁持有者内部类
     */
    private static class LockHolder {
        private final String lockId;
        private int count;

        public LockHolder(String lockId, int count) {
            this.lockId = lockId;
            this.count = count;
        }

        public String getLockId() {
            return lockId;
        }

        public int getCount() {
            return count;
        }

        public void incrementCount() {
            this.count++;
        }

        public void decrementCount() {
            this.count--;
        }
    }
}