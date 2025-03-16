package org.vivi.framework.report.service.web.dto.reporttpl;

import lombok.Data;
import org.vivi.framework.report.service.model.reporttpl.ReportTpl;

import java.util.List;

/**  
* @Description: report_tpl扩展用实体类
 */
@Data
public class ReportTplDto extends ReportTpl {

	/**
	 * 	报表数据源
	 */
	private List<Long> dataSource;
	
	/**
	 * 	数据源编码
	 */
	private String dataSourceCode;
	
	/**
	 * 	数据源名称
	 */
	private String dataSourceName;
	
	/**
	 * 	报表类型名称
	 */
	private String reportTypeName;
	
	/**  
	 * @Fields roles : 角色
	 */
	private List<Long> roles;
	
	/**  
	 * @Fields datasourceId : 数据源id
	 */
	private String datasourceId;
	
}
