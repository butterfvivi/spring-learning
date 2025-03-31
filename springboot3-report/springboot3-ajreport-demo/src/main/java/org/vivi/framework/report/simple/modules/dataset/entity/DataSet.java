
package org.vivi.framework.report.simple.modules.dataset.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.vivi.framework.report.simple.entity.BaseEntity;

/**
* @description 数据集 entity
**/
@TableName(keepGlobalPrefix=true, value="report_data_set")
@Data
public class DataSet extends BaseEntity {

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


}
