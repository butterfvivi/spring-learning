package org.vivi.framework.ireport.demo.web.dto;

import lombok.Data;
import org.vivi.framework.ireport.demo.service.report.dto.ReportSheetSetDto;

import java.util.List;
import java.util.Map;

@Data
public class GenerateReportDto {

    /**
     *  sheetDatas : 返回数据
     */
    private List<ReportSheetSetDto> sheetDatas;

    /**
     * report id
     */
    private Long rtId;

    private String reportService;
    /**
     * 动态参数
     */
    private Map<String, Object> searchData;

    /**
     *  tplName : 报表名称
     */
    private String reportName;

}
