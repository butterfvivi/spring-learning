package org.vivi.framework.redisson.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedissonLockService {

    @Autowired
    private RedissonClient redissonClient;

    // 计数器
    private int count = 2;

    private String prefix = "lock";

    public void disstributeLock(String key) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(50);
        RLock lock = redissonClient.getLock(prefix + ":" + key);
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                // 每个线程都创建自己的锁对象
                // 这是基于 Redis 实现的分布式锁
                try {
                    boolean tryLock = lock.tryLock(10, 50, TimeUnit.SECONDS);
                    lock.lock(10, TimeUnit.SECONDS);
                    if (tryLock &&count > 0) {
                        this.count = this.count - 1;
                        log.info("扣减库存成功！当前库存数: " + count);
                    } else {
                        log.info("库存已经没了");
                    }
                } catch (InterruptedException e) {
                    log.error("分布式锁，出错了");
                } finally {
                    if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                        lock.unlock();
                        log.info("释放锁完成");
                    }
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        log.info("业务count数 = {}", this.count);
    }


    /**
     * redisson分布式锁
     * @param lock
     */
    public void redissonDistributedLock(String lock) {
        RLock rlock = redissonClient.getLock(lock);
        for (int i = 0; i < 20; i++) {
           new Thread(() -> {
               // 生成唯一uuid
               String uuid = UUID.randomUUID().toString();
               log.info("开始创建订单:"+uuid);
               // 获取锁，并设置锁的自动释放时间。
               try {
                   //尝试获取锁
                   //waitTime：等待获取锁的最大时间量；leaseTime：锁的自动释放时间（每个线程占用锁的最大时间）; unit ：时间单位
                   //tryLock(10,30,TimeUnit.SECONDS)：尝试在10秒内获得锁，如果成功，锁将在30秒后自动释放
                   boolean tryLock = rlock.tryLock(10, 50, TimeUnit.SECONDS);

                   if (tryLock){
                       //启动看门狗，锁到期自动续期
                       rlock.lock(15, TimeUnit.MILLISECONDS);
                       //模拟业务逻辑
                       log.info("分布式锁 ，线程 - {} 获取到锁！", uuid);

                       Thread.sleep(1000);
                       log.info("分布式锁，业务逻辑执行完成......");
                   }else {
                       log.error("锁已存在");
                   }

               } catch (InterruptedException e) {
                   log.error("分布式锁，出错了");
               } finally {
                   if (rlock.isLocked() && rlock.isHeldByCurrentThread()) {
                       rlock.unlock();
                       log.info("分布式锁，释放锁完成");
                   }else {
                       log.info("分布式锁，锁已过期......");
                   }
               }
           }).start();
        }
    }

    /**
     * redis布隆过滤器
     * @param bloomKey
     */
    public Boolean redisBloomFilter(String bloomKey) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomKey);
        //初始化布隆过滤器，初始化预期插入的数据量为200，期望误差率为0.01
        bloomFilter.tryInit(200, 0.01);
        // 插入数据
        bloomFilter.add("test-1");
        bloomFilter.add("chao");
        bloomFilter.add("test.com");
        bloomFilter.add(bloomKey);

        //设置过期时间
        bloomFilter.expire(60, TimeUnit.SECONDS);
        // 检查元素是否存在
        boolean contains = bloomFilter.contains("test-1");
        boolean contains2 = bloomFilter.contains("chao");
        boolean contains3 = bloomFilter.contains("test.com");
        boolean contains4 = bloomFilter.contains("test.org");
        log.info("布隆过滤器，元素{}是否存在:{}", "test-1", contains);
        log.info("布隆过滤器，元素{}是否存在:{}", "chao", contains2);
        log.info("布隆过滤器，元素{}是否存在:{}", "test.com", contains3);
        log.info("布隆过滤器，元素{}是否存在:{}", "test.org", contains4);

        return bloomFilter.contains(bloomKey);
    }

    /**
     * redisson限流
     * @param rateLimiterKey
     */
    public String redissonRateLimiter(String rateLimiterKey) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(rateLimiterKey);
        //创建限流器，设置限流策略，例如每秒钟不超过10个请求
        rateLimiter.trySetRate(RateType.OVERALL , 1 , 1, RateIntervalUnit.SECONDS);
        //允许请求次数
        String availCount = "-1";

        //尝试获取许可证，超时时间为0表示不等待
        if (rateLimiter.tryAcquire(0 , 1, TimeUnit.SECONDS)){
            availCount = StrUtil.toString(rateLimiter.availablePermits());
            //如果获取到许可证，执行业务逻辑
            log.info("获取许可证成功，剩余许可证数：{}", availCount);
        }else {
            //如果无法获取许可证，则执行限流逻辑或者抛出异常
            log.info("获取许可证失败，请求被限流，剩余许可证数：{}", availCount);
        }
        return availCount;
    }

    /**
     * redisson 的可重入锁操作
     * @return
     */
    public String redissonLock(String lockKey){
        RLock lock = redissonClient.getLock(lockKey);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                lock.lock();
                try{
                    System.out.println("可重入锁线程" + Thread.currentThread().getName() + "获取锁成功");
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }

            }).start();
        }
        return "success";
    }

    /**
     * Redisson 消息发布订阅操作
     * @param topicKey
     * @return
     */
    public String redissonTopic(String topicKey){
        RTopic topic = redissonClient.getTopic(topicKey);
        String msgId = UUID.fastUUID().toString();

        HashMap<Object, Object> map = new HashMap<>();
        map.put("id", msgId);
        map.put("msg", "hello redisson");
        topic.publish(map);

        long publish = topic.publish(map);
        log.info("发布消息成功，消息ID为：{}，发布数量为：{}", msgId, publish);
        return StrUtil.toString(publish);
    }

    public void redissonQueue(String queueKey) {
        RQueue<String> queue = redissonClient.getQueue(queueKey);
        //向队列中添加值
        queue.add("queue-1");
        queue.add("queue-2");
        //设置过期时间
        queue.expire(10, TimeUnit.SECONDS);

        String poll = queue.poll();
        log.info("从队列中取出的值为：{}", poll);

        //删除数据
        queue.remove();
        //获取队列信息
        RQueue<Object> queueValue = redissonClient.getQueue(queueKey);
        log.info("队列信息：{}", queueValue.toString());
    }

    public void redissonMap(String mapKey){
        //创建Map对象
        RMap<String, String> map = redissonClient.getMap(mapKey);
        //添加键值对
        map.put("key-1", "value-1");
        map.put("key-2", "value-2");
        map.put("key-3", "value-3");

        //设置过期时间
        map.expire(30, TimeUnit.SECONDS);

        String value1 = map.get("key-1");
        log.info("获取的Map值为：{}", value1);

        //删除键值对
        String removeValue = map.remove("key-2");
        log.info("删除的Map值为：{}", removeValue);

        //获取Map信息
        RMap<String, String> mapValue = redissonClient.getMap(mapKey);
        log.info("Map信息：{}", mapValue.toString());
    }

    public String redissonBucket(String key,String value){
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value,30,TimeUnit.SECONDS);
        return redissonClient.getBucket(key).get().toString();
    }
}
