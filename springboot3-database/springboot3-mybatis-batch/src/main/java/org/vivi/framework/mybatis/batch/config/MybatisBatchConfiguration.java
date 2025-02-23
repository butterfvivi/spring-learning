package org.vivi.framework.mybatis.batch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vivi.framework.mybatis.batch.context.BatchInsertBeanProcessor;
import org.vivi.framework.mybatis.batch.plugins.BatchInsertInterceptor;

/**
 * mybatis-batch自动配置
 */
@Configuration
public class MybatisBatchConfiguration {

    /**
     * 装配拦截器
     *
     * @return BatchInsertInterceptor
     */
    @Bean
    public BatchInsertInterceptor batchInsertInterceptor() {
        return new BatchInsertInterceptor();
    }

    /**
     * 装配扫描器
     *
     * @param batchInsertInterceptor 拦截器
     * @return BatchInsertBeanProcessor
     */
    @Bean
    public BatchInsertBeanProcessor batchInsertBeanProcessor(@Autowired BatchInsertInterceptor batchInsertInterceptor) {
        BatchInsertBeanProcessor batchInsertBeanProcessor = new BatchInsertBeanProcessor();
        batchInsertBeanProcessor.setBatchInsertInterceptor(batchInsertInterceptor);
        return batchInsertBeanProcessor;
    }

}
