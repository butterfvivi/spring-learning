package org.vivi.framework.report.service.web.dto.reporttpldatasource;

import lombok.Data;

/**
* <p>Title: MesExecSqlDto</p>
* <p>Description: 执行sql用参数实体类</p>
*/
@Data
public class MesExecSqlDto {

	/**
	* @Feilds:tplSql sql语句
	*/
	private String tplSql;
	
	/**
	* @Feilds:tplId 模板id
	*/
	private Long tplId;
	
	/**
	* @Feilds:datasourceId 数据源id
	*/
	private Long datasourceId;
	
	/**  
	 * @Fields sqlType : sql类型 1标准sql 2存储过程
	 */
	private Integer sqlType;
	
	/**  
	 * @Fields inParam : 输入参数
	 */
	private String inParam;
	
	/**  
	 * @Fields outParam : 输出参数
	 */
	private String outParam;
	
	/**  
	 * @Fields params : sql参数
	 */
	private String sqlParams;
	
}
