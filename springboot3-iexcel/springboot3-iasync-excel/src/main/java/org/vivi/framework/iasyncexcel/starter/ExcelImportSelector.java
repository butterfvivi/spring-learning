package org.vivi.framework.iasyncexcel.starter;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

public class ExcelImportSelector implements DeferredImportSelector, Ordered {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return Arrays.asList(
            ExcelAutoConfiguration.class.getName(),
            ExcelService.class.getName()
        ).toArray(new String[0]);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
