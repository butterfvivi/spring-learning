
package org.vivi.framework.report.simple.modules.datasource.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.vivi.framework.report.simple.entity.BaseEntity;

/**
* @description 数据源 entity
**/
@TableName(keepGlobalPrefix=true, value="report_data_source")
@Data
public class DataSource extends BaseEntity {

    /**数据源编码*/
    private String sourceCode;

    /**数据源名称*/
    private String sourceName;

    /**数据源描述*/
    private String sourceDesc;

    /**数据源类型 DIC_NAME=SOURCE_TYPE; mysql，orace，sqlserver，elasticsearch，接口，javaBean，数据源类型字典中item-extend动态生成表单*/
    private String sourceType;

    /**数据源连接配置json：关系库{ jdbcUrl:'', username:'', password:'','driverName':''}ES-sql{ apiUrl:'http://127.0.0.1:9092/_xpack/sql?format=json','method':'POST','body':'{\"query\":\"select 1\"}' }  接口{ apiUrl:'http://ip:port/url', method:'' } javaBean{ beanNamw:'xxx' }*/
    private String sourceConfig;

    /**0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG*/
    private Integer enableFlag;

    /**0--未删除 1--已删除 DIC_NAME=DELETE_FLAG*/
    private Integer deleteFlag;

}
