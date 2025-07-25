package org.vivi.framework.redisson.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.redisson.service.RedissonLockService;

@Slf4j
@RestController
@RequestMapping("/redisson")
public class RedissonLockController {

    private RedissonLockService redissonLockService;

    public RedissonLockController(RedissonLockService redissonLockService) {
        this.redissonLockService = redissonLockService;
    }

    @GetMapping("/distributeLock-1")
    public void disstributeLock(String key) throws InterruptedException {
        redissonLockService.disstributeLock(key);
    }

    /**
     * 分布式锁
     * @param lockKey
     * @return
     */
    @GetMapping("/distributeLock")
    public void redissonDistributedLock(String lockKey) {
        redissonLockService.redissonDistributedLock(lockKey);
    }

    /**
     * 布隆过滤器
     * @param lockKey
     */
    @GetMapping("/bloomFilter")
    public Boolean redisBloomFilter(String lockKey) {
        return redissonLockService.redisBloomFilter(lockKey);
    }

    /**
     * 限流
     * @param lockKey
     * @return
     */
    @GetMapping("/rateLimiter")
    public String redissonRateLimiter(String lockKey) {
        return redissonLockService.redissonRateLimiter(lockKey);
    }

    /**
     * 可重入锁
     * @param lockKey
     * @return
     */
    @GetMapping("/lock")
    public String redissonLock(String lockKey) {
        return redissonLockService.redissonLock(lockKey);
    }

    /**
     * 队列
     * @param queueKey
     */
    @GetMapping("/queue")
    public void redissonQueue(String queueKey) {
        redissonLockService.redissonQueue(queueKey);
    }

    /**
     * 订阅发布
     * @param topicKey
     * @return
     */
    @GetMapping("/topic")
    public String redissonTopic(String topicKey) {
        return redissonLockService.redissonTopic(topicKey);
    }

    /**
     * map
     * @param mapKey
     */
    @GetMapping("/map")
    public void redissonMap(String mapKey){
        redissonLockService.redissonMap(mapKey);
    }

    @GetMapping("/bucket")
    public void redissonBucket(String setKey,String value){
        redissonLockService.redissonBucket(setKey,value);
    }
}
