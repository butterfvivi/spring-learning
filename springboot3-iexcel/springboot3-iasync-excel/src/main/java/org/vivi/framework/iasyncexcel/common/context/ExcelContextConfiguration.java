package org.vivi.framework.iasyncexcel.common.context;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.vivi.framework.iasyncexcel.excel.importer.AsyncImportTaskSupport;
import org.vivi.framework.iasyncexcel.excel.importer.ImportTaskSupport;
import org.vivi.framework.iasyncexcel.service.ExcelTaskService;

/**
 * 注册给子容器的bean通过ComponentScan 与 自定义@ExcelHandle注解扫描写在主项目中的扩展bean
 */
@Configuration
@ComponentScan({"org.vivi.framework.iasyncexcel.common.context"})
@Import(ExcelContextRegistrar.class)
public class ExcelContextConfiguration {

    @Bean
    @ConditionalOnBean({ExcelTaskService.class})
    ImportTaskSupport importSupport(ExcelTaskService taskService){
        return new AsyncImportTaskSupport(taskService);
    }

}
