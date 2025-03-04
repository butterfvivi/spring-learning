package org.vivi.framework.report.simple.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity {

    /**
     * 数据ID
     */
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

    /**
     * 运行参数，导入导出时，该条数据，对应在excel中的索引号
     */
    @TableField(exist = false)
    @JsonIgnore
    private Integer rowIndex;
}
