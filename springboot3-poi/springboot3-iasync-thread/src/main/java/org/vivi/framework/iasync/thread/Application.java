package org.vivi.framework.iasync.thread;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("org.vivi.framework.iasync.thread.**mapper")
@ComponentScan(basePackages = {"org.vivi.framework.iasync.thread.**"})
@SpringBootApplication(scanBasePackages = {"org.vivi.framework.iasync.thread.**"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}