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
 * Excel context configuration for registering beans in the child container
 * Uses ComponentScan and @ExcelHandle annotation to scan extension beans in the main project
 */
@Configuration
@ComponentScan({"org.vivi.framework.iasyncexcel.starter.context"})
@Import({ExcelContextRegistrar.class})
public class ExcelContextConfiguration {

    /**
     * Default storage service implementation
     * Can be overridden by defining a custom IStorageService bean
     */
    @Bean
    @ConditionalOnMissingBean(IStorageService.class)
    public IStorageService storageService() {
        return new ServerLocalStorageService();
    }

    /**
     * Import task support implementation
     */
    @Bean
    @ConditionalOnBean({IStorageService.class, TaskService.class})
    public ImportTaskSupport importSupport(TaskService taskService, IStorageService storageService) {
        return new AsyncImportTaskSupport(storageService, taskService);
    }

    /**
     * Export task support implementation
     */
    @Bean
    @ConditionalOnBean({IStorageService.class, TaskService.class})
    public ExportTaskSupport exportSupport(TaskService taskService, IStorageService storageService) {
        return new AsyncExportTaskSupport(storageService, taskService);
    }

}
