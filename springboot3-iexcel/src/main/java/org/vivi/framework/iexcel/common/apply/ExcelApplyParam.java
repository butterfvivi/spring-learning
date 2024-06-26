package org.vivi.framework.iexcel.common.apply;


import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;
import org.vivi.framework.iexcel.common.apply.loader.ApplyContextLoader;
import org.vivi.framework.iexcel.common.context.ApplyContext;
import org.vivi.framework.iexcel.common.context.holder.ContextHolder;
import org.vivi.framework.iexcel.common.context.holder.ContextHolderBuilder;


@Getter
@Setter
public class ExcelApplyParam {

    private String key;

    private ExcelApplier excelApplier;

    private ExcelApplyHandler excelApplyHandler;

    private ApplyContextLoader contextLoader;

    private ContextHolder<String, ApplyContext> contextHolder;

    ExcelApplyParam(String key,
                    ExcelApplier excelApplier,
                    ExcelApplyHandler excelApplyHandler,
                    ApplyContextLoader contextLoader,
                    ContextHolder<String, ApplyContext> contextHolder) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(excelApplier, "ExcelApplier must not be null!");
        Assert.notNull(contextLoader, "ContextLoader must not be null!");

        this.key = key;
        this.excelApplier = excelApplier;
        this.excelApplyHandler = excelApplyHandler;
        this.contextLoader = contextLoader;
        this.contextHolder = contextHolder;
    }

    public static ExcelApplyParamBuilder builder() {
        return new ExcelApplyParamBuilder();
    }


    public static class ExcelApplyParamBuilder {
        private String key;
        private ExcelApplier excelApplier;
        private ExcelApplyHandler excelApplyHandler;
        private ApplyContextLoader contextLoader;
        private ContextHolder<String, ApplyContext> contextHolder = ContextHolderBuilder.<ApplyContext>create().build();

        ExcelApplyParamBuilder() {
        }

        public ExcelApplyParamBuilder key(String key) {
            this.key = key;
            return this;
        }

        public ExcelApplyParamBuilder excelApplier(ExcelApplier excelApplier) {
            this.excelApplier = excelApplier;
            return this;
        }

        public ExcelApplyParamBuilder excelApplyHandler(ExcelApplyHandler excelApplyHandler) {
            this.excelApplyHandler = excelApplyHandler;
            return this;
        }

        public ExcelApplyParamBuilder contextLoader(ApplyContextLoader contextLoader) {
            this.contextLoader = contextLoader;
            return this;
        }

        public ExcelApplyParamBuilder contextHolder(ContextHolder<String, ApplyContext> contextHolder) {
            this.contextHolder = contextHolder;
            return this;
        }

        public ExcelApplyParam build() {
            return new ExcelApplyParam(key, excelApplier, excelApplyHandler, contextLoader, contextHolder);
        }
    }
}
