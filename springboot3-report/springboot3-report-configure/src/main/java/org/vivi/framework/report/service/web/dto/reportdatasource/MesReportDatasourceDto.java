package org.vivi.framework.report.service.web.dto.reportdatasource;

import lombok.Data;

import java.util.List;

@Data
public class MesReportDatasourceDto {

	/**
	 * 数据源类型
	 */
	List<Integer> datasourceType;
	
	/**  
	 * @Fields merchantNo : 商户号
	 */
	private String merchantNo;
}
