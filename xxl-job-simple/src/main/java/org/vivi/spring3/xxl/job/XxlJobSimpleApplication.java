package org.vivi.spring3.xxl.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class XxlJobSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(XxlJobSimpleApplication.class, args);
    }

}
