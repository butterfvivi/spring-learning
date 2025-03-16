package org.vivi.framework.report.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@MapperScan("org.vivi.framework.report.service.mapper**")
@SpringBootApplication
public class ReportConfigureApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportConfigureApplication.class, args);
    }

}
