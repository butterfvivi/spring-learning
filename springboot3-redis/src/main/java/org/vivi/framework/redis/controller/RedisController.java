package org.vivi.framework.redis.controller;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.redis.common.util.RedisCache;
import org.vivi.framework.redis.model.User;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    public RedisTemplate redisTemplate;


    /**
     * String
     */
    @RequestMapping("/string")
    public String dataString() {
        User vo = new User();
        vo.setId(1001L);
        vo.setName("vivi");
        vo.setEmail("123@126.com");

        redisCache.set("user:1001", vo, 60, TimeUnit.SECONDS);
        System.out.println("set redis success");

        User vos = redisCache.getValue("user:1001");
        System.out.println(vos);
        return JSONUtil.toJsonStr(vos);
    }

    @RequestMapping("/list")
    public String dataList() {
        redisTemplate.opsForList().leftPushAll("dats-list", "list-01", "list-02", "list-03");
        System.out.println("set redis success");
        Object rightPop01 = redisTemplate.opsForList().rightPop("dats-list");
        Object rightPop02 = redisTemplate.opsForList().rightPop("dats-list");

        return "rightPop01: " + rightPop01 + " rightPop02: " + rightPop02;
    }

    /**
     * set
     */
    @RequestMapping("/set")
    public String dataSet() {
        redisTemplate.opsForSet().add("data-set", "set-01", "set-02", "set-03", "set-04");
        System.out.println("set redis success");

        Object pop01 = redisTemplate.opsForSet().pop("data-set");
        Object pop02 = redisTemplate.opsForSet().pop("data-set");

        return  "pop01: " + pop01 + " pop02: " + pop02;
    }

    @RequestMapping("/hash")
    public String dataHash(){
        HashMap<String, String> objectHashMap = new HashMap<>();
        objectHashMap.put("hash-01", "hash-01");
        objectHashMap.put("hash-02", "hash-02");
        objectHashMap.put("hash-03", "hash-03");

        redisTemplate.opsForHash().putAll("data-hash", objectHashMap);
        System.out.println("set redis success");

        Object hashValue01 = redisTemplate.opsForHash().get("data-hash", "hash-01");
        Object hashValue02 = redisTemplate.opsForHash().get("data-hash", "hash-02");
        Object hashValue03 = redisTemplate.opsForHash().get("data-hash", "hash-03");

        return "hashValue01: " + hashValue01 + " hashValue02: " + hashValue02  + " hashValue03: " + hashValue03;
    }


    @RequestMapping("/zset")
    public String dataZset(){
        redisTemplate.opsForZSet().add("data-zset", "zset-01", 100);
        redisTemplate.opsForZSet().add("data-zset", "zset-02", 200);
        redisTemplate.opsForZSet().add("data-zset", "zset-03", 300);
        System.out.println("set redis success");

        Object zsetValue01 = redisTemplate.opsForZSet().popMax("data-zset");
        Object zsetValue02 = redisTemplate.opsForZSet().popMax("data-zset");
        Object zsetValue03 = redisTemplate.opsForZSet().popMax("data-zset");

        return "zsetValue01: " + zsetValue01 + " zsetValue02: " + zsetValue02  + " zsetValue03: " + zsetValue03;
    }
}

