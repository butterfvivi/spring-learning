package org.vivi.framework.iasync.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vivi.framework.iasyncexcel.starter.EnableAsyncExcel;

@EnableAsyncExcel
@SpringBootApplication
@MapperScan({"org.vivi.framework.iasync.sample.mapper"})
public class IasyncSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(IasyncSampleApplication.class, args);
    }

}
