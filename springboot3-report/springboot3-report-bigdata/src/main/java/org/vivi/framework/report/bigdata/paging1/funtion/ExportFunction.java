package org.vivi.framework.report.bigdata.paging1.funtion;

import java.util.function.Function;

/**
 * @author xuteng
 * @apiNote ss
 * @date 2024-2-2 8:57
 */

public interface ExportFunction<T, R> extends Function<T, R> {
    /**
     * 获取数据总条数
     *
     * @return java.lang.Long
     * @date 2024-2-2 8:59
     */

    Long getCount();

}
