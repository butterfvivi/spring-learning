package org.vivi.framework.report.bigdata.paging1.funtion;

import java.util.function.Function;

public interface ExportFunction<T, R> extends Function<T, R> {

    /**
     * 获取数据总条数
     */
    Long getCount();

}
