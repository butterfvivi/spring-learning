package org.vivi.framework.lock.manager;

/**
 * 锁获取异常
 * 当无法获取分布式锁时抛出的异常
 */
public class LockAcquisitionException extends RuntimeException {

    private final String lockKey;
    private final String strategy;
    private final long attemptTime;

    public LockAcquisitionException(String message) {
        super(message);
        this.lockKey = null;
        this.strategy = null;
        this.attemptTime = System.currentTimeMillis();
    }

    public LockAcquisitionException(String message, String lockKey) {
        super(message);
        this.lockKey = lockKey;
        this.strategy = null;
        this.attemptTime = System.currentTimeMillis();
    }

    public LockAcquisitionException(String message, String lockKey, String strategy) {
        super(message);
        this.lockKey = lockKey;
        this.strategy = strategy;
        this.attemptTime = System.currentTimeMillis();
    }

    public LockAcquisitionException(String message, Throwable cause) {
        super(message, cause);
        this.lockKey = null;
        this.strategy = null;
        this.attemptTime = System.currentTimeMillis();
    }

    public LockAcquisitionException(String message, String lockKey, Throwable cause) {
        super(message, cause);
        this.lockKey = lockKey;
        this.strategy = null;
        this.attemptTime = System.currentTimeMillis();
    }

    public LockAcquisitionException(String message, String lockKey, String strategy, Throwable cause) {
        super(message, cause);
        this.lockKey = lockKey;
        this.strategy = strategy;
        this.attemptTime = System.currentTimeMillis();
    }

    /**
     * 获取锁的键
     */
    public String getLockKey() {
        return lockKey;
    }

    /**
     * 获取锁策略
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * 获取尝试时间
     */
    public long getAttemptTime() {
        return attemptTime;
    }

    /**
     * 获取异常详细信息
     */
    public String getDetailedMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("锁获取失败: ").append(getMessage());

        if (lockKey != null) {
            sb.append(", lockKey: ").append(lockKey);
        }

        if (strategy != null) {
            sb.append(", strategy: ").append(strategy);
        }

        sb.append(", attemptTime: ").append(attemptTime);

        return sb.toString();
    }
}