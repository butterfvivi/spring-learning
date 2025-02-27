
package org.vivi.framework.report.simple.entity.datasetparam;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
* @description 数据集动态参数 entity
**/
@TableName(keepGlobalPrefix=true, value="report_data_set_param")
@Data
public class DataSetParam   {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /** 数据集编码 */
    private String setCode;

    /** 参数名 */
    private String paramName;

    /** 参数描述 */
    private String paramDesc;

    /** 参数类型 */
    private String paramType;

    /** 参数示例项 */
    private String sampleItem;

    /** 0--非必填 1--必填 DIC_NAME=REQUIRED_FLAG */
    private Integer requiredFlag;

    /** js校验字段值规则，满足校验返回 true */
    private String validationRules;

    /** 排序 */
    private Integer orderNum;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
    private Integer enableFlag;

    /** 0--未删除 1--已删除 DIC_NAME=DELETE_FLAG" */
    private Integer deleteFlag;

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
