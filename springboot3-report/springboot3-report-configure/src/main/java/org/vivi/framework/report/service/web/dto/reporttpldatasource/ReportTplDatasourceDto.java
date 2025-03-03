package org.vivi.framework.report.service.web.dto.reporttpldatasource;

import lombok.Data;
import org.vivi.framework.report.service.model.reportdatasource.ReportDatasource;

/**
* <p>Title: ReportTplDatasourceDto</p>
* <p>Description: 报表模板数据源扩展用实体类</p>
*/
@Data
public class ReportTplDatasourceDto extends ReportDatasource {

	/**
	* @Feilds:dataSourceName 数据源名称
	*/
	private String dataSourceName;
	
	/**
	* @Feilds:dataSourceCode 数据源代码
	*/
	private String dataSourceCode;
	
	/**  
	 * @Fields type : 数据源类型
	 */
	private Integer type;
			
    /**  
     * @Fields apiColumns : api 数据列
     */
    private String apiColumns;
}
