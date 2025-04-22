package org.vivi.framework.ireport.demo.service.reportset.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReportSheetSetDto {

    /**
     *  cellDatas : 单元格数据
     */
    private List<Map<String, Object>> cellDatas;

    /**
     *  config : sheet页面的配置信息，例如行宽行高
     */
    private Map<String, Object> config;

    /**
     *  paginationMap : 分页信息
     */
    private Map<String, Map<String, Integer>> pagination;

    /**
     * dynHeader : 动态表头，
     */
    private String dynHeader;


    private String styleService;


    /** sheet_index - sheet页唯一索引 */
    private String titleName;

    /** calc_chain - 计算链，有公式的单元格信息 */
    private List<JSONObject> calcChain;

    /**
     *  sheetName : sheet名称
     */
    private String sheetName;

    /**
     *  sheetOrder : sheet顺序
     */
    private Integer sheetOrder;

    /**
     *  extraCustomCellConfigs : 自定义额外单元格配置
     */
    private Map<String, JSONObject> extraCustomCellConfigs;

}
