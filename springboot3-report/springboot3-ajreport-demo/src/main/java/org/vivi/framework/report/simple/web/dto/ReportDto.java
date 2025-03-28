package org.vivi.framework.report.simple.web.dto;

import lombok.Data;
import org.vivi.framework.report.simple.entity.BaseEntity;

import java.io.Serializable;


@Data
public class ReportDto extends BaseEntity implements Serializable {

    /** 报表名称 */
    private String reportName;

    /** 报表编码 */
    private String reportCode;

    /**数据集编码，以|分割*/
    private String setCodes;

    /** 分组 */
    private String reportGroup;

    /** 备注 */
    private String reportDesc;

    /** 数据集查询参数 */
    private String setParam;

    /** 报表json字符串 */
    private String jsonStr;

    /** 报表类型 */
    private String reportType;

    /** 数据总计 */
    private long total;

    /** 报表缩略图 */
    private String reportImage;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
    private Integer enableFlag;

    /** 0--未删除 1--已删除 DIC_NAME=DELETE_FLAG */
    private Integer deleteFlag;


    /** 报表作者 */
    private String reportAuthor;

    /** 下载次数 */
    private Long downloadCount;

}
