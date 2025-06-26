package org.vivi.framework.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.redis.service.RedisLockService;

@RestController
@RequestMapping("/redis/lock")
public class RedisLockController {

    @Autowired
    private RedisLockService  redisLockService;

}
