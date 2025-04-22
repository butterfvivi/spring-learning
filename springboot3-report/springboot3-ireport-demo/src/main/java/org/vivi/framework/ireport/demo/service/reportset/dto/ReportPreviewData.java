package org.vivi.framework.ireport.demo.service.reportset.dto;


import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReportPreviewData {

    /**
     *  sheetDatas : 返回数据
     */
    private List<ReportSheetSetDto> sheetDatas;

    /**
     *  pagination : 分页信息
     */
    private Map<String, Object> pagination;


    /**
     *  tplName : 报表名称
     */
    private String reportlName;

    /**
     *  sqlMaps : 本次查询每个数据集对应的sql语句
     */
    private List<Map<String, String>> reportSqls;

}
