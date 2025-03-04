
package org.vivi.framework.report.simple.web.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class ReportExcelDto implements Serializable {
    /**
     * 报表名称
     */
    private String reportName;

    /**
     * 报表编码
     */
    private String reportCode;

    /**
     * 数据集编码，以|分割
     */
    private String setCodes;

    /**
     * 分组
     */
    private String reportGroup;

    /**
     * 数据集查询参数
     */
    private String setParam;

    /**
     * 报表json字符串
     */
    private String jsonStr;

    /**
     * 报表类型
     */
    private String reportType;

    /**
     * 数据总计
     */
    private long total;

    /**
     * 导出类型
     */
    private String exportType;

    /**
     * 报表总体数据
     */
    private String rowDatas;

}
