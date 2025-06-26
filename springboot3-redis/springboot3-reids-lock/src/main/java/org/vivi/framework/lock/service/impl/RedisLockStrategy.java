package org.vivi.framework.lock.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.vivi.framework.lock.model.LockResult;
import org.vivi.framework.lock.service.LockStrategy;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁策略实现
 * 基于Redis的SET NX EX命令实现分布式锁
 */
@Slf4j
@Component("redis")
public class RedisLockStrategy implements LockStrategy {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private static final String LOCK_PREFIX = "distributed-lock:";
    
    /**
     * 释放锁的Lua脚本
     * 确保只有锁的持有者才能释放锁
     */
    private static final String UNLOCK_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "return redis.call('del', KEYS[1]) " +
        "else return 0 end";
    
    /**
     * 可重入锁的Lua脚本
     */
    private static final String REENTRANT_LOCK_SCRIPT = 
        "local key = KEYS[1] " +
        "local value = ARGV[1] " +
        "local expire = ARGV[2] " +
        "local count = redis.call('hget', key, value) " +
        "if count then " +
        "  redis.call('hincrby', key, value, 1) " +
        "  redis.call('expire', key, expire) " +
        "  return 1 " +
        "else " +
        "  local exists = redis.call('hlen', key) " +
        "  if exists == 0 then " +
        "    redis.call('hset', key, value, 1) " +
        "    redis.call('expire', key, expire) " +
        "    return 1 " +
        "  else " +
        "    return 0 " +
        "  end " +
        "end";
    
    /**
     * 可重入锁释放的Lua脚本
     */
    private static final String REENTRANT_UNLOCK_SCRIPT = 
        "local key = KEYS[1] " +
        "local value = ARGV[1] " +
        "local count = redis.call('hget', key, value) " +
        "if count and tonumber(count) > 1 then " +
        "  redis.call('hincrby', key, value, -1) " +
        "  return 1 " +
        "elseif count and tonumber(count) == 1 then " +
        "  redis.call('del', key) " +
        "  return 1 " +
        "else " +
        "  return 0 " +
        "end";
    
    @Override
    public LockResult tryLock(String lockKey, String lockValue, long expireTime) {
        String key = LOCK_PREFIX + lockKey;
        
        try {
            // 使用SET NX EX命令尝试获取锁
            Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, lockValue, expireTime, TimeUnit.MILLISECONDS);
            
            if (Boolean.TRUE.equals(success)) {
                log.debug("Redis锁获取成功: lockKey={}, lockValue={}, expireTime={}ms", 
                    lockKey, lockValue, expireTime);
                return LockResult.success(lockValue, System.currentTimeMillis() + expireTime);
            } else {
                log.debug("Redis锁获取失败: lockKey={}, 锁已被其他客户端持有", lockKey);
                return LockResult.failure("锁已被其他客户端持有");
            }
        } catch (Exception e) {
            log.error("Redis锁获取异常: lockKey={}", lockKey, e);
            return LockResult.failure("Redis锁获取异常: " + e.getMessage());
        }
    }

    @Override
    public boolean unlock(String lockKey, String lockValue) {
        String key = LOCK_PREFIX + lockKey;
        
        try {
            // 使用Lua脚本确保原子性释放锁
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(UNLOCK_SCRIPT);
            script.setResultType(Long.class);
            
            Long result = redisTemplate.execute(script, 
                Collections.singletonList(key), lockValue);
            
            boolean success = Long.valueOf(1).equals(result);
            if (success) {
                log.debug("Redis锁释放成功: lockKey={}, lockValue={}", lockKey, lockValue);
            } else {
                log.warn("Redis锁释放失败: lockKey={}, lockValue={}, 可能不是锁的持有者", 
                    lockKey, lockValue);
            }
            
            return success;
        } catch (Exception e) {
            log.error("Redis锁释放异常: lockKey={}", lockKey, e);
            return false;
        }
    }
    
    @Override
    public boolean isLocked(String lockKey) {
        String key = LOCK_PREFIX + lockKey;
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("检查Redis锁状态异常: lockKey={}", lockKey, e);
            return false;
        }
    }

    /**
     * 尝试获取可重入锁
     */
    public LockResult tryReentrantLock(String lockKey, String lockValue, long expireTime) {
        String key = LOCK_PREFIX + "reentrant:" + lockKey;
        
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(REENTRANT_LOCK_SCRIPT);
            script.setResultType(Long.class);
            
            Long result = redisTemplate.execute(script, 
                Collections.singletonList(key), lockValue, String.valueOf(expireTime / 1000));
            
            if (Long.valueOf(1).equals(result)) {
                log.debug("Redis可重入锁获取成功: lockKey={}, lockValue={}", lockKey, lockValue);
                return LockResult.success(lockValue, System.currentTimeMillis() + expireTime);
            } else {
                log.debug("Redis可重入锁获取失败: lockKey={}", lockKey);
                return LockResult.failure("可重入锁获取失败");
            }
        } catch (Exception e) {
            log.error("Redis可重入锁获取异常: lockKey={}", lockKey, e);
            return LockResult.failure("Redis可重入锁获取异常: " + e.getMessage());
        }
    }
    
    /**
     * 释放可重入锁
     */
    public boolean unlockReentrant(String lockKey, String lockValue) {
        String key = LOCK_PREFIX + "reentrant:" + lockKey;
        
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(REENTRANT_UNLOCK_SCRIPT);
            script.setResultType(Long.class);
            
            Long result = redisTemplate.execute(script, 
                Collections.singletonList(key), lockValue);
            
            boolean success = Long.valueOf(1).equals(result);
            if (success) {
                log.debug("Redis可重入锁释放成功: lockKey={}, lockValue={}", lockKey, lockValue);
            } else {
                log.warn("Redis可重入锁释放失败: lockKey={}, lockValue={}", lockKey, lockValue);
            }
            
            return success;
        } catch (Exception e) {
            log.error("Redis可重入锁释放异常: lockKey={}", lockKey, e);
            return false;
        }
    }
    
    /**
     * 获取锁的剩余过期时间
     */
    public long getLockTtl(String lockKey) {
        String key = LOCK_PREFIX + lockKey;
        try {
            Long ttl = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
            return ttl != null ? ttl : -1;
        } catch (Exception e) {
            log.error("获取Redis锁TTL异常: lockKey={}", lockKey, e);
            return -1;
        }
    }
    
    /**
     * 获取锁的当前值
     */
    public String getLockValue(String lockKey) {
        String key = LOCK_PREFIX + lockKey;
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("获取Redis锁值异常: lockKey={}", lockKey, e);
            return null;
        }
    }
}