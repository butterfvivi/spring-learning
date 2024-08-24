package org.vivi.framework.quartz.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AllArgsConstructor
public class InitQuartzJob implements InitializingBean {


    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
