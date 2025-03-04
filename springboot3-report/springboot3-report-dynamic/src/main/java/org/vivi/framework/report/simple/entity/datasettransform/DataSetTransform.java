
package org.vivi.framework.report.simple.entity.datasettransform;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.vivi.framework.report.simple.entity.BaseEntity;

import java.util.Date;

/**
* @description 数据集数据转换 entity
**/
@TableName(keepGlobalPrefix=true, value="report_data_set_transform")
@Data
public class DataSetTransform extends BaseEntity {


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

}
