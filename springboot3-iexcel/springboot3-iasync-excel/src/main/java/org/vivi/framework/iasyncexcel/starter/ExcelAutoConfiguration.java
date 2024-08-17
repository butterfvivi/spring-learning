package org.vivi.framework.iasyncexcel.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vivi.framework.iasyncexcel.starter.context.config.ExcelThreadPool;
import org.vivi.framework.iasyncexcel.starter.context.service.ExcelService;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ExcelAutoConfiguration {

    @Bean
    public SpringExcelContext springExcelContext(){
        SpringExcelContext context = new SpringExcelContext();
        return context;
    }

    @Bean
    public ExcelService excelService(SpringExcelContext springExcelContext, ExcelThreadPool excelThreadPool){
        return new ExcelService(excelThreadPool,springExcelContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExcelThreadPool excelThreadPool(){
        int processors = Runtime.getRuntime().availableProcessors();
        int coreSize = 2;
        int maxSize = 4;
        if (processors > 1){
            coreSize = 2*processors-1;
            maxSize = 4*processors-1;
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                coreSize,
                maxSize,
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(20)
        );
        return new ExcelThreadPool(executor);
    }
}
