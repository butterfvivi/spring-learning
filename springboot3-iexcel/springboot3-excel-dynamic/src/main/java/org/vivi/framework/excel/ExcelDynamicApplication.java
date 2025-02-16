package org.vivi.framework.excel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ExcelDynamicApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelDynamicApplication.class, args);
    }

}
