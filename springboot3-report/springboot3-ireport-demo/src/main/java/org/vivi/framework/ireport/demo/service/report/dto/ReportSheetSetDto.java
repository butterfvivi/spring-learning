package org.vivi.framework.ireport.demo.service.report.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;
import org.vivi.framework.ireport.demo.report.config.IExportConfig;

import java.util.List;
import java.util.Map;

@Data
public class ReportSheetSetDto {

    /**
     *  cellDatas : 单元格数据
     */
    private List<T> cellDatas;

    private String targetParam;

    /**
     * export dynamic head list
     */
    private List<String> headList;

    /**
     * exported configuration
     */
    private IExportConfig config;

    /**
     *  sqlMaps : 本次查询每个数据集对应的sql语句
     */
    private String reportSqls;

    /**
     * additional parameters
     */
    private Map<String, Object> params;

    /**
     *  config : sheet页面的配置信息，例如行宽行高
     */
    private Map<String, Object> sheetConfig;

    /**
     *  paginationMap : 分页信息
     */
    private Map<String, Map<String, Integer>> pagination;


    private String styleService;


    /** sheet_index - sheet页唯一索引 */
    private String titleName;

    /**
     *  sheetName : sheet名称
     */
    private String sheetName;

    /**
     *  sheetOrder : sheet顺序
     */
    private Integer sheetOrder;


    /** calc_chain - 计算链，有公式的单元格信息 */
    private List<JSONObject> calcChain;

}
