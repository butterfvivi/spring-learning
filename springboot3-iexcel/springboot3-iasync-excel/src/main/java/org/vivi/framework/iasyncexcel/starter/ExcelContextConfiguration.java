package org.vivi.framework.iasyncexcel.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.vivi.framework.iasyncexcel.core.service.IStorageService;
import org.vivi.framework.iasyncexcel.core.service.TaskService;
import org.vivi.framework.iasyncexcel.core.support.AsyncExportTaskSupport;
import org.vivi.framework.iasyncexcel.core.support.AsyncImportTaskSupport;
import org.vivi.framework.iasyncexcel.core.support.ExportTaskSupport;
import org.vivi.framework.iasyncexcel.core.support.ImportTaskSupport;
import org.vivi.framework.iasyncexcel.starter.context.service.ServerLocalStorageService;

/**
 * 注册给子容器的bean通过ComponentScan 与 自定义@ExcelHandle注解扫描写在主项目中的扩展bean
 */
@Configuration
@ComponentScan({"org.vivi.framework.iasyncexcel.starter.context"})
@Import({ExcelContextRegistrar.class})
public class ExcelContextConfiguration {


    /**暴露给外部扩展
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IStorageService.class)
    IStorageService storageService(){
        return new ServerLocalStorageService();
    }


    @Bean
    @ConditionalOnBean({IStorageService.class, TaskService.class})
    ImportTaskSupport importSupport(TaskService taskService,IStorageService storageService){
        return new AsyncImportTaskSupport(storageService,taskService);
    }


    @Bean
    @ConditionalOnBean({IStorageService.class,TaskService.class})
    ExportTaskSupport exportSupport(TaskService taskService,IStorageService storageService){
        return new AsyncExportTaskSupport(storageService,taskService);
    }

}
