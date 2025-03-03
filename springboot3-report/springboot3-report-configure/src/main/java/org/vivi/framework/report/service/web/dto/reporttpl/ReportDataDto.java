package org.vivi.framework.report.service.web.dto.reporttpl;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Map;

@Data
public class ReportDataDto {

	/**  
	 * @Fields reportDatas : 上报的数据
	 */
	private Map<String, Map<String, Object>> reportDatas;
	
	/**  
	 * @Fields datasKey : 主键信息
	 */
	private Map<String, Map<String, Object>> datasKey;
	
	/**  
	 * @Fields basicDatas : 原始数据
	 */
	private Map<String, Map<String, Object>> basicDatas;
	
	/**  
	 * @Fields tplId : 模板id
	 */
	private Long tplId;
	
	/**  
	 * @Fields version : 当前更新版本号，防止重复提交覆盖数据
	 */
	private Long version;
	
	/**  
	 * @Fields reCalculate : 需要重新计算的列数据
	 */
	private JSONObject reCalculate;
	
	/**  
	 * @Fields autoFillAttrs : 自动填充属性
	 */
	private JSONObject autoFillAttrs;
}
