package org.vivi.framework.ireport.demo.web.dto;

import lombok.Data;

@Data
public class DataExecSqlDto {

    /**
     * sql语句
     */
    private String reportSql;

    /**
     *  sql类型 1标准sql 2存储过程
     */
    private Integer sqlType;

    /**
     * 输入参数
     */
    private String inParam;

    /**
     * 输出参数
     */
    private String outParam;

    /**
     * sql参数
     */
    private String sqlParams;

}
