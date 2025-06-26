package org.vivi.framework.lock.service;

import org.vivi.framework.lock.model.LockResult;

/**
 * 分布式锁策略接口
 * 定义分布式锁的基本操作
 */
public interface LockStrategy {

    /**
     * 尝试获取锁
     * @param lockKey 锁的键
     * @param lockValue 锁的值（用于标识锁的持有者）
     * @param expireTime 锁的过期时间（毫秒）
     * @return 锁获取结果
     */
    LockResult tryLock(String lockKey, String lockValue, long expireTime);

    /**
     * 释放锁
     * @param lockKey 锁的键
     * @param lockValue 锁的值
     * @return 是否成功释放
     */
    boolean unlock(String lockKey, String lockValue);

    /**
     * 检查锁是否存在
     * @param lockKey 锁的键
     * @return 是否存在
     */
    default boolean isLocked(String lockKey) {
        throw new UnsupportedOperationException("不支持的操作");
    }

    /**
     * 获取锁策略名称
     * @return 策略名称
     */
    default String getStrategyName() {
        return this.getClass().getSimpleName();
    }
}