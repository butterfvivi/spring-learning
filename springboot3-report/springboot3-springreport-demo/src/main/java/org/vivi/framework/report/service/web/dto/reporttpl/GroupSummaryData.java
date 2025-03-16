package org.vivi.framework.report.service.web.dto.reporttpl;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**  
 * @ClassName: GroupSummaryData
 * @Description: 分组汇总计算用实体类
*/
@Data
public class GroupSummaryData {

	/**  
	 * @Fields datas : 数据
	 */
	private List<Map<String, Object>> datas;
	
	/**  
	 * @Fields digit : 小数位数
	 */
	private Integer digit;
	
	/**  
	 * @Fields property : 属性
	 */
	private String property;
}
