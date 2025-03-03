package org.vivi.framework.report.service.web.dto.reporttpl;

import lombok.Data;

import java.util.Map;

/**  
 * @ClassName: ReportDictsDto
 * @Description: 报表单元格对应的数据字典
*/
@Data
public class ReportCellDictsDto {

	/**  
	 * @Fields cellDictsValueLabel : 单元格坐标对应的字典 外层map的key是sheetindex_r_c，内层map的key是value，根据value获取label，后台显示数据用
	 */
	private Map<String, Map<String, String>> cellDictsValueLabel;
	
	/**  
	 * @Fields cellDictsLabelValue : 单元格坐标对应的字典 外层map的key是sheetindex_r_c，内层map的key是label，根据label获取value，返回给前端上报数据时用
	 */
	private Map<String, Map<String, String>> cellDictsLabelValue;
}
