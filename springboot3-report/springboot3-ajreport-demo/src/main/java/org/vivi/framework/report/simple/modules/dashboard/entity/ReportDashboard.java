
package org.vivi.framework.report.simple.modules.dashboard.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.vivi.framework.report.simple.entity.BaseEntity;

/**
* @description 大屏设计 entity
**/
@TableName(keepGlobalPrefix=true, value="gaea_report_dashboard")
@Data
public class ReportDashboard extends BaseEntity {
    /** 报表编码*/
    private String reportCode;

    /** 看板标题*/
    private String title;

    /** 宽度px*/
    private Long width;

    /** 高度px*/
    private Long height;

    /** 背景色*/
    private String backgroundColor;

    /** 背景图片*/
    private String backgroundImage;

    /** 工作台中的辅助线*/
    private String presetLine;

    /** 自动刷新间隔秒，数据字典REFRESH_TYPE*/
    private Integer refreshSeconds;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG*/
    private Integer enableFlag;

    /**  0--未删除 1--已删除 DIC_NAME=DEL_FLAG*/
    private Integer deleteFlag;

    /** 排序，降序*/
    private Integer sort;

}
