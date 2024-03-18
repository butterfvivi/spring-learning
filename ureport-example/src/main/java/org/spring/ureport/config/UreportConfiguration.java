package org.spring.ureport.config;

import com.bstek.ureport.console.UReportServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration(proxyBeanMethods = false)
@ImportResource(UreportConfiguration.CONTEXT)
public class UreportConfiguration {
    static final String CONTEXT = "classpath:ureport-console-context.xml";

    @Bean
    public ServletRegistrationBean<UReportServlet> buildUreportServlet() {
        return new ServletRegistrationBean<>(new UReportServlet(), "/ureport/*");
    }

//    @Bean
//    public ServletRegistrationBean<UReportServlet> buildUReportServlet(UReportServlet uReportServlet) {
//        ServletRegistrationBean<UReportServlet> registrationBean = new ServletRegistrationBean<>(uReportServlet);
//        registrationBean.addUrlMappings("/ureport/*");
//        return registrationBean;
//    }


}
