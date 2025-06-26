package org.vivi.framework.lock.metrics;

import lombok.Builder;
import lombok.Data;

/**
 * 锁使用指标
 * 记录锁的使用统计信息和性能指标
 */
@Data
@Builder
public class LockMetrics {
    
    /**
     * 锁的键
     */
    private String lockKey;
    
    /**
     * 锁策略
     */
    private String strategy;
    
    /**
     * 总尝试次数
     */
    private long totalAttempts;
    
    /**
     * 成功获取次数
     */
    private long successAttempts;
    
    /**
     * 失败获取次数
     */
    private long failedAttempts;
    
    /**
     * 错误次数
     */
    private long errorCount;
    
    /**
     * 总获取时间
     */
    private long totalAcquireTime;
    
    /**
     * 平均获取时间
     */
    private long averageAcquireTime;
    
    /**
     * 最后获取时间
     */
    private long lastAcquireTime;
    
    /**
     * 最后释放时间
     */
    private long lastReleaseTime;
    
    /**
     * 当前持有者
     */
    private String currentHolder;
    
    /**
     * 最后错误信息
     */
    private String lastError;
    
    /**
     * 最后错误时间
     */
    private long lastErrorTime;
    
    /**
     * 获取成功率
     */
    public double getSuccessRate() {
        return totalAttempts > 0 ? (double) successAttempts / totalAttempts : 0.0;
    }
    
    /**
     * 获取失败率
     */
    public double getFailureRate() {
        return totalAttempts > 0 ? (double) failedAttempts / totalAttempts : 0.0;
    }
    
    /**
     * 获取错误率
     */
    public double getErrorRate() {
        return totalAttempts > 0 ? (double) errorCount / totalAttempts : 0.0;
    }
    
    /**
     * 检查锁是否被持有
     */
    public boolean isHeld() {
        return currentHolder != null && lastReleaseTime < lastAcquireTime;
    }
    
    /**
     * 获取锁持有时间
     */
    public long getHoldTime() {
        if (isHeld()) {
            return System.currentTimeMillis() - lastAcquireTime;
        }
        return 0;
    }
    
    /**
     * 获取最后持有时间
     */
    public long getLastHoldTime() {
        if (lastReleaseTime > 0 && lastAcquireTime > 0) {
            return lastReleaseTime - lastAcquireTime;
        }
        return 0;
    }
    
    /**
     * 检查是否有错误
     */
    public boolean hasError() {
        return errorCount > 0;
    }
    
    /**
     * 获取最后错误距离现在的时间
     */
    public long getLastErrorTimeAgo() {
        return lastErrorTime > 0 ? System.currentTimeMillis() - lastErrorTime : 0;
    }
}