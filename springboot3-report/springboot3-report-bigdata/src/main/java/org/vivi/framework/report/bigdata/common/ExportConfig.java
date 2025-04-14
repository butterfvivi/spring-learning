package org.vivi.framework.report.bigdata.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * sheet初始化配置,用于下一步初始化SheetInfo,初始化完毕,则删除该数据
 */
@Data
@Builder
public class ExportConfig {
    /**导出:sheet数据对象,用于制作表头**/
    private Class<?> clazz;
    /**导出:sheet表头**/
    private String titles;
    /**导出:sheet最大行数限制**/
    @Builder.Default
    private Integer maxRow = 20000;
    /**导出:使用模板导出数据**/
    private Integer templateIndex;
    /**导出:sheet名称**/
    private String sheetName;
    /**导出:sheet补充说明:放到表头以上**/
    private String sheetDescription;
    /**导出:sheet数据列表**/
    @Builder.Default
    private List datas = Lists.newArrayList();
    /**导出:字段的下拉枚举值key:字段名,value:枚举列表**/
    @Builder.Default
    private Map<String,List<?>> dropDowns = Maps.newHashMap();
}
