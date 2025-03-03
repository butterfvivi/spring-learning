package org.vivi.framework.report.service.web.dto.reporttpldataset;

import lombok.Data;

import java.util.Map;

@Data
public class MesGetRelyOnSelectData {

	/**  
	 * @Fields selectContent : 查询sql
	 */
	private String selectContent;
	
	/**  
	 * @Fields datasourceId : 数据源id
	 */
	private Long datasourceId;
	
	/**  
	 * @Fields params : 参数
	 */
	private Map<String, Object> params;
}
