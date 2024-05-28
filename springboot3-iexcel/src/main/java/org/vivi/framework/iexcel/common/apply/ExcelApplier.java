package org.vivi.framework.iexcel.common.apply;


import org.vivi.framework.iexcel.common.context.ApplyContext;


public interface ExcelApplier {

    void apply(Object data, ApplyContext context);
}
