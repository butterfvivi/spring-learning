
package org.vivi.framework.report.simple.modules.datasource.entity.param;

import lombok.Data;

import java.io.Serializable;


/**
* @desc DataSource 数据集查询输入类
**/
@Data
public class DataSourceParam implements Serializable{

    /** 数据源名称 */
    private String sourceName;

    /** 数据源编码 */
    private String sourceCode;

    /** 数据源类型 DIC_NAME=SOURCE_TYPE; mysql，orace，sqlserver，elasticsearch，接口，javaBean，数据源类型字典中item-extend动态生成表单 */
    private String sourceType;
}
