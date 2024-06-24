package org.vivi.framework.redis.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.redis.common.util.RedisCache;
import org.vivi.framework.redis.model.User;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private RedisCache redisCache;

    /**
     * 写入redis
     */
    @RequestMapping("/set")
    public void setRedis() {
        User vo = new User();
        vo.setId(1001L);
        vo.setName("vivi");
        vo.setEmail("123@126.com");

        redisCache.set("user:1001", vo, 60, TimeUnit.SECONDS);
        System.out.println("set redis success");
    }

    /**
     * 读取redis
     */
    @RequestMapping("/get")
    public String getRedis() {
        User vo = redisCache.getValue("user:1001");
        System.out.println(vo);
        return JSONUtil.toJsonStr(vo);
    }
}

