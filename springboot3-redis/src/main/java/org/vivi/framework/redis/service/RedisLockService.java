package org.vivi.framework.redis.service;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RedisLockService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public Boolean lock(String lock,long timeout){
        return redisTemplate.opsForValue().setIfAbsent(lock,lock,timeout, TimeUnit.SECONDS);
    }

    public void unLock(String lock){
        Object obj = redisTemplate.opsForValue().get(lock);
        if (!Objects.isNull(obj) && Objects.equals(obj.toString(),lock)){
            redisTemplate.delete(lock);
        }
    }
}
