package org.vivi.framework.mybatis.batch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.vivi.framework.mybatis.batch.web.mapper")
public class MybatisBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisBatchApplication.class, args);
    }

}
