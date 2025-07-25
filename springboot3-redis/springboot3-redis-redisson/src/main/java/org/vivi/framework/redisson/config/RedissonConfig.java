package org.vivi.framework.redisson.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * 初始化 redisson 配置客户端
 */
@Slf4j
@Configuration
public class RedissonConfig {

    @Autowired
    private RedissonProperties redissonProperties;

    public RedissonClient redissonClient() throws IOException {
        Config config = Config.fromYAML(redissonProperties.getConfig());
        Redisson.create(config);
        log.info("redisson client init success");
        return Redisson.create(config);
    }

}
