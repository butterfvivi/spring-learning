package org.vivi.framework.iasyncexcel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.vivi.framework.iasyncexcel.starter.EnableAsyncExcel;

@EnableAsyncExcel
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class IasyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(IasyncApplication.class,args);
    }
}
