package org.vivi.framework.report.simple.modules.reportexcel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.vivi.framework.report.simple.entity.BaseEntity;

@TableName(value = "report_excel")
@Data
public class ReportExcel extends BaseEntity {

    /** 报表编码 */
    private String reportCode;

    /** 数据集编码，以|分割 */
    private String setCodes;

    /** 数据集查询参数 */
    private String setParam;

    /** 报表json字符串 */
    private String jsonStr;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
    private Integer enableFlag;

    /** 0--未删除 1--已删除 DIC_NAME=DELETE_FLAG */
    private Integer deleteFlag;



}
