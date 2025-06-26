package org.vivi.framework.lock.model;

import lombok.Builder;
import lombok.Data;

/**
 * 锁操作结果
 * 封装锁获取和释放操作的结果信息
 */
@Data
@Builder
public class LockResult {
    
    /**
     * 操作是否成功
     */
    private boolean success;
    
    /**
     * 锁的值（用于后续释放锁）
     */
    private String lockValue;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 获取锁的时间戳
     */
    private long acquireTime;
    
    /**
     * 锁的过期时间
     */
    private long expireTime;
    
    /**
     * 创建成功结果
     */
    public static LockResult success(String lockValue) {
        return LockResult.builder()
            .success(true)
            .lockValue(lockValue)
            .acquireTime(System.currentTimeMillis())
            .build();
    }
    
    /**
     * 创建成功结果（带过期时间）
     */
    public static LockResult success(String lockValue, long expireTime) {
        return LockResult.builder()
            .success(true)
            .lockValue(lockValue)
            .acquireTime(System.currentTimeMillis())
            .expireTime(expireTime)
            .build();
    }
    
    /**
     * 创建失败结果
     */
    public static LockResult failure(String errorMessage) {
        return LockResult.builder()
            .success(false)
            .errorMessage(errorMessage)
            .build();
    }
    
    /**
     * 创建失败结果（默认错误信息）
     */
    public static LockResult failure() {
        return failure("获取锁失败");
    }
    
    /**
     * 检查锁是否已过期
     */
    public boolean isExpired() {
        return expireTime > 0 && System.currentTimeMillis() > expireTime;
    }
    
    /**
     * 获取锁持有时间
     */
    public long getHoldTime() {
        return acquireTime > 0 ? System.currentTimeMillis() - acquireTime : 0;
    }
}