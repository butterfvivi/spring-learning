package org.vivi.framework.report.service.web.dto.reporttpl;

import lombok.Data;

@Data
public class ReportTplTreeDto extends ReportTplDto{

	/**  
	 * @Fields hasChildren : 是否有子节点
	 */
	private boolean hasChildren = true;
	
	/**  
	 * @Fields icon : 图标
	 */
	private String icon;
	
	/**  
	 * @Fields type : 类型 1文件夹 2文件
	 */
	private String type = "1";
}
