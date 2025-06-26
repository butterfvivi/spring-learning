package org.vivi.framework.lock.service;

import org.vivi.framework.lock.model.LockResult;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁策略接口
 * 定义分布式锁的基本操作
 */
public interface LockStrategy {

    /**
     * 尝试获取锁
     *
     * @param lockKey 锁的键
     * @param timeout 超时时间
     * @param timeUnit 时间单位
     * @return 锁结果
     */
    LockResult tryLock(String lockKey, long timeout, TimeUnit timeUnit);

    /**
     * 释放锁
     * @param lockKey 锁的键
     * @return 是否成功释放
     */
    boolean unlock(String lockKey);

    /**
     * 检查锁是否存在
     * @param lockKey 锁的键
     * @return 是否存在
     */
    default boolean isLocked(String lockKey) {
        throw new UnsupportedOperationException("不支持的操作");
    }

    /**
     * 强制释放锁
     *
     * @param lockKey 锁的键
     * @return 是否成功释放
     */
    boolean forceUnlock(String lockKey);

    /**
     * 获取锁策略名称
     * @return 策略名称
     */
    default String getStrategyName() {
        return this.getClass().getSimpleName();
    }
}