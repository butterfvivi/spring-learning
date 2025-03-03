package org.vivi.framework.report.simple;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("org.vivi.framework.report.simple.mapper")
@SpringBootApplication
public class ReportSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportSimpleApplication.class, args);
    }

}
