package org.vivi.framework.iasyncexcel.starter;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

public class ExcelImportSelector implements DeferredImportSelector, Ordered {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> importList = new ArrayList<>();
        importList.add(ExcelAutoConfiguration.class.getName());
        importList.add(ExcelService.class.getName());
        return importList.toArray(new String[importList.size()]);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
