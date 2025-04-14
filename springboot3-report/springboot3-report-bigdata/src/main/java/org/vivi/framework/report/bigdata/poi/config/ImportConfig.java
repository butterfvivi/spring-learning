package org.vivi.framework.report.bigdata.poi.config;

import lombok.Builder;
import lombok.Data;
import org.vivi.framework.report.bigdata.poi.model.TitleType;

/**
 * 功能 :
 * sheet初始化配置,用于下一步初始化SheetInfo,初始化完毕,则删除该数据
 */
@Data
@Builder
public class ImportConfig {
    /**导入:sheet数据对象,用于制作表头**/
    private Class<?> clazz;
    /**导入:sheet表头**/
    private String titles;
    /**导入:sheet最大行数限制**/
    @Builder.Default
    private Integer maxRow = 20000;
    /**导入:sheet索引**/
    private Integer sheetIndex;
    /**导入:sheet名称**/
    private String sheetName;
    /**表头类型**/
    private TitleType titleType;
    /**读取指定列的数据**/
    private Integer[] columns;
    /**读取指定行的数据**/
    private Integer[] rows;
}
