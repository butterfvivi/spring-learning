
package org.vivi.framework.report.simple.web.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
*
* @description 大屏设计 dto
**/
@Data
public class ReportDashboardObjectDto implements Serializable {

    /** 报表编码 */
    private String reportCode;
    /**
     * 报表编码
     */
    private ReportDashboardDto dashboard;

    /**
     * 大屏画布中的组件
     */
    private List<ReportDashboardWidgetDto> widgets;

}
