
package org.vivi.framework.report.simple.entity.datasettransform;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
* @description 数据集数据转换 entity
**/
@TableName(keepGlobalPrefix=true, value="gaea_report_data_set_transform")
@Data
public class DataSetTransform {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**数据集编码*/
    private String setCode;

    /**数据转换类型，DIC_NAME=TRANSFORM_TYPE; js，javaBean，字典转换*/
    private String transformType;

    /**数据转换script,处理逻辑*/
    private String transformScript;

    /**排序,执行数据转换顺序*/
    private Integer orderNum;

    /**0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG*/
    private Integer enableFlag;

    /**0--未删除 1--已删除 DIC_NAME=DELETE_FLAG*/
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
