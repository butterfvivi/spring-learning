
package org.vivi.framework.report.simple.modules.datasource.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
*
* @description 数据源 dto
**/
@Data
public class DataSourceDto implements Serializable {

    private Long id;

    /** 数据源编码 */
     private String sourceCode;

    /** 数据源名称 */
     private String sourceName;

    /** 数据源描述 */
     private String sourceDesc;

    /** 数据源类型 DIC_NAME=SOURCE_TYPE; mysql，orace，sqlserver，elasticsearch，接口，javaBean，数据源类型字典中item-extend动态生成表单 */
     private String sourceType;

    /** 数据源连接配置json：关系库{ jdbcUrl:'', username:'', password:'','driverName':''}ES-sql{ apiUrl:'http://127.0.0.1:9092/_xpack/sql?format=json','method':'POST','body':'{"query":"select 1"}' }  接口{ apiUrl:'http://ip:port/url', method:'' } javaBean{ beanNamw:'xxx' } */
     private String sourceConfig;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
     private Integer enableFlag;

    /** 0--未删除 1--已删除 DIC_NAME=DELETE_FLAG */
     private Integer deleteFlag;

    /**************************************************************/
    /**关系型数据库jdbcUrl */
    private String jdbcUrl;

    /** 关系型数据库用户名 */
    private String username;

    /** 关系型数据库密码 */
    private String password;

    /** 关系型数据库驱动类 */
    private String driverName;

    /** 关系型数据库sql */
    private String sql;

    /** http requestUrl */
    private String apiUrl;

    /** http method */
    private String method;

    /** http header */
    private String header;

    /** http 请求体 */
    private String body;

    /** 动态查询sql或者接口中的请求体 */
    private String dynSentence;

    /** 传入的自定义参数，解决url中存在的动态参数*/
    private Map<String, Object> contextData;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    private String updateBy;


    /**
     * 前端展示
     */
    private String updateByView;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 版本号
     */
    private Integer version;

}
