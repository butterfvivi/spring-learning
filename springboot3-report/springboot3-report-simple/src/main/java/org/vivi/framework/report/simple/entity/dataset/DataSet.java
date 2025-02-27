
package org.vivi.framework.report.simple.entity.dataset;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
* @description 数据集 entity
**/
@TableName(keepGlobalPrefix=true, value="report_data_set")
@Data
public class DataSet {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /** 数据集编码 */
    private String setCode;

    /** 数据集名称 */
    private String setName;

    /** 数据集描述 */
    private String setDesc;

    /** 数据集类型 */
    private String setType;

    /** 数据源编码 */
    private String sourceCode;

    /** 动态查询sql或者接口中的请求体 */
    private String dynSentence;

    /** 结果案例 */
    private String caseResult;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
    private Integer enableFlag;

    /** 0--未删除 1--已删除 DIC_NAME=DELETE_FLAG */
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
