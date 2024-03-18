package org.spring.ureport.config;


import com.bstek.ureport.UReportPropertyPlaceholderConfigurer;
import com.bstek.ureport.provider.report.ReportProvider;
import org.spring.ureport.processor.UReportPropertyPlaceholderConfigurerPlus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 增强默认Ureport 行为
 */
//@ConditionalOnClass(OssTemplate.class)
@Configuration(proxyBeanMethods = false)
public class UreportExtConfig {

//    @Bean
//    public ReportProvider dfsReportProvider(OssTemplate ossTemplate, OssProperties properties) {
//        return new DfsReportProvider(ossTemplate, properties);
//    }

    @Bean
    public UReportPropertyPlaceholderConfigurer uReportPropertyPlaceholderConfigurerPlus() {
        return new UReportPropertyPlaceholderConfigurerPlus();
    }
}
