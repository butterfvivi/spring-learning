package org.vivi.framework.report.service.web.dto.doctpl;

import lombok.Data;
import org.vivi.framework.report.service.model.doctpl.DocTpl;

import java.util.List;

@Data
public class DocTplDto extends DocTpl {
	
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
	 * @Fields datasourceId : 数据源id
	 */
	private String datasourceId;
	
	/**  
	 * @Fields margins : 页边距
	 */
	private String margins = "[]";
}
