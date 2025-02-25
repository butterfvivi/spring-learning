package org.vivi.framework.dynamic.sqlbatis.serialization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 动态映射语句
 * </p>
 */
@TableName("mapped_statement")
public class MappedStatementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 租户
     */
    private String tenantCode;

    /**
     * 父亲id
     */
    private Integer parentId;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 数据库编号
     */
    private String dataSourceCodes;

    /**
     * UNKNOWN, INSERT, UPDATE, DELETE, SELECT, FLUSH
     */
    private String commandType;

    /**
     * 动态mybatissql
     */
    private String sqlStatement;

    /**
     * mybatis resultType
     */
    private String resultType;

    /**
     * mybatis resultMap
     */
    private String resultMap;

    /**
     * 返回值类型 COLLECT, PAGE, MAP,STRING,INTEGER,LONG,BOOLEAN
     */
    private String returnType;

    /**
     * 输入参数
     */
    private String inputParameter;

    /**
     * 输出参数
     */
    private String outputParameter;

    /**
     * 关联表
     */
    private String tables;

    /**
     * 状态：1=启用；2=禁用
     */
    private Byte status;

    /**
     * 版本每次修改版本加1
     */
    private String version;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 属性
     */
    private String attribute;

    /**
     * 0=未删除；1=已删除
     */
    private Byte isDeleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人账号
     */
    private String createBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 修改人账号
     */
    private String updateBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getSqlStatement() {
        return sqlStatement;
    }

    public void setSqlStatement(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultMap() {
        return resultMap;
    }

    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getInputParameter() {
        return inputParameter;
    }

    public void setInputParameter(String inputParameter) {
        this.inputParameter = inputParameter;
    }

    public String getOutputParameter() {
        return outputParameter;
    }

    public void setOutputParameter(String outputParameter) {
        this.outputParameter = outputParameter;
    }

    public String getTables() {
        return tables;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getDataSourceCodes() {
        return dataSourceCodes;
    }

    public void setDataSourceCodes(String dataSourceCodes) {
        this.dataSourceCodes = dataSourceCodes;
    }
}
