package org.vivi.framework.report.simple.modules.report.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.vivi.framework.report.simple.entity.BaseEntity;

@TableName(keepGlobalPrefix=true, value="report")
@Data
public class Report  extends BaseEntity {

    /** 名称 */
    private String reportName;

    /** 报表编码 */
    private String reportCode;

    /** 分组 */
    private String reportGroup;

    /** 报表描述 */
    private String reportDesc;

    /** 报表类型 */
    private String reportType;

    /** 报表缩略图 */
    private String reportImage;

    /** 报表作者 */
    private String reportAuthor;

    /** 下载次数 */
    private Long downloadCount;

    /** "0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG*/
    private Integer enableFlag;

    /** 0--未删除 1--已删除 DIC_NAME=DELETE_FLAG */
    private Integer deleteFlag;

}
