package org.vivi.framework.lock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.TestPropertySource;
import org.vivi.framework.lock.model.LockResult;
import org.vivi.framework.lock.service.impl.RedisLockStrategy;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis锁策略测试
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.redis.host=localhost",
    "spring.data.redis.port=6379",
    "vivi.redis-lock.enabled=true"
})
class RedisLockStrategyTest {
    
    @Autowired
    private RedisLockStrategy redisLockStrategy;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Test
    void testTryLock() {
        String lockKey = "test:try:lock";
        String lockValue = "test-value";
        long expireTime = 5000;
        
        // 测试获取锁
        LockResult result = redisLockStrategy.tryLock(lockKey, expireTime, TimeUnit.MILLISECONDS);
        assertTrue(result.isSuccess());
        assertEquals(lockValue, result.getLockValue());
        
        // 验证锁在Redis中存在
        assertTrue(redisLockStrategy.isLocked(lockKey));
        
        // 测试重复获取失败
        LockResult result2 = redisLockStrategy.tryLock(lockKey,  expireTime, TimeUnit.MILLISECONDS);
        assertFalse(result2.isSuccess());
    }
    
    @Test
    void testUnlock() {
        String lockKey = "test:unlock:lock";
        String lockValue = "test-value";
        long expireTime = 5000;
        
        // 获取锁
        LockResult result = redisLockStrategy.tryLock(lockKey, expireTime, TimeUnit.MILLISECONDS);
        assertTrue(result.isSuccess());
        
        // 释放锁
        boolean unlockResult = redisLockStrategy.unlock(lockKey);
        assertTrue(unlockResult);
        
        // 验证锁已被释放
        assertFalse(redisLockStrategy.isLocked(lockKey));
    }
    
    @Test
    void testUnlockWithWrongValue() {
        String lockKey = "test:wrong:unlock:lock";
        String lockValue = "test-value";
        String wrongValue = "wrong-value";
        long expireTime = 5000;
        
        // 获取锁
        LockResult result = redisLockStrategy.tryLock(lockKey, expireTime, TimeUnit.MILLISECONDS);
        assertTrue(result.isSuccess());
        
        // 使用错误的值释放锁
        boolean unlockResult = redisLockStrategy.unlock(lockKey);
        assertFalse(unlockResult);
        
        // 验证锁仍然存在
        assertTrue(redisLockStrategy.isLocked(lockKey));
        
        // 使用正确的值释放锁
        unlockResult = redisLockStrategy.unlock(lockKey);
        assertTrue(unlockResult);
    }
    
    @Test
    void testReentrantLock() {
        String lockKey = "test:reentrant:lock";
        String lockValue = "test-value";
        long expireTime = 5000;
        
        // 测试可重入锁获取
        LockResult result = redisLockStrategy.tryReentrantLock(lockKey, lockValue, expireTime);
        assertTrue(result.isSuccess());
        
        // 测试可重入锁释放
        boolean unlockResult = redisLockStrategy.unlockReentrant(lockKey, lockValue);
        assertTrue(unlockResult);
    }
    
    @Test
    void testGetLockTtl() {
        String lockKey = "test:ttl:lock";
        String lockValue = "test-value";
        long expireTime = 5000;
        
        // 获取锁
        LockResult result = redisLockStrategy.tryLock(lockKey, expireTime, TimeUnit.MILLISECONDS);
        assertTrue(result.isSuccess());
        
        // 获取TTL
        long ttl = redisLockStrategy.getLockTtl(lockKey);
        assertTrue(ttl > 0 && ttl <= expireTime);
        
        // 释放锁
        redisLockStrategy.unlock(lockKey);
    }
    
    @Test
    void testGetLockValue() {
        String lockKey = "test:value:lock";
        String lockValue = "test-value";
        long expireTime = 5000;
        
        // 获取锁
        LockResult result = redisLockStrategy.tryLock(lockKey, expireTime, TimeUnit.MILLISECONDS);
        assertTrue(result.isSuccess());
        
        // 获取锁值
        String value = redisLockStrategy.getLockValue(lockKey);
        assertEquals(lockValue, value);
        
        // 释放锁
        redisLockStrategy.unlock(lockKey);
    }
}