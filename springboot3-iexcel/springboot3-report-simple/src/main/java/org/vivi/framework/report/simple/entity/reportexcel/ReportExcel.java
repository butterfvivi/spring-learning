package org.vivi.framework.report.simple.entity.reportexcel;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName(value = "gaea_report_excel")
@Data
public class ReportExcel {

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

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 版本号
     * @Version:乐观锁,，需要添加mybatis plus插件optimisticLockerInterceptor
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, update="%s+1")
    @Version
    private Integer version;
}
