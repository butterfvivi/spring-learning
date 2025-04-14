package org.vivi.framework.report.bigdata.entity.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;


@Data
public class SheetInfo {
    private Version version;
    /**当前sheet是否涉及使用模板文件**/
    private boolean useTemplate;
    /**sheet中开始写数据的行**/
    private Integer beginRow = 0;
    /**sheet中最大行数限制**/
    private Integer maxRowNum = beginRow + 20000;
    /**默认行高**/
    private Integer rowHeight = 20 * 25;
    /**该sheet是否是激活的sheet**/
    private boolean isActive;
    /**该sheet在workbook中的索引位置**/
    private Integer index;
    /**该sheet在workbook中的名字**/
    private String name;
    /**word中的sheet对象**/
    private Sheet sheet;
    /**sheet的表头赋值类型**/
    private TitleType titleType;
    /**表头行**/
    private Row headerRow;
    /**数据对象**/
    private Class<?> clazz;
    /**特殊表头所在行**/
    private Integer unionLine = 0;
    /**特殊表头宽度**/
    private Integer unionWith = 0;
    /**特殊表头内容**/
    private String description;
    /**读取指定列的数据**/
    private List<Integer> columns = Lists.newArrayList();
    /**读取指定行的数据**/
    private List<Integer> rows = Lists.newArrayList();
    /**表头:用于控制表头字段顺序**/
    private List<String> headerNames = Lists.newArrayList();
    /**表头信息**/
    private Map<String, HeaderInfo> headerInfos = Maps.newHashMap();
    /**sheet每一行的错误记录**/
    private Map<Integer, RowErrorInfo> errorInfos = Maps.newHashMap();
    /**sheet中,数据**/
    private List datas = Lists.newArrayList();
    /**列宽度收集**/
    private Map<Integer, Integer> columnWidths = Maps.newHashMap();

    /**奇数行样式**/
    private List<CellStyle> oddStyle = Lists.newArrayList();
    /**偶数行样式**/
    private List<CellStyle> evenStyle = Lists.newArrayList();
}
