package org.vivi.framework.report.simple.core.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**  
 * @ClassName: MesExportExcel
 * @Description: 导出excel用实体类
*/
@Data
public class MesExportExcel {

	/**  
	 * @Fields password : 密码
	 */
	private String password;
	
	/**  
	 * @Fields sheetConfigs : sheet页配置信息
	 */
	private List<MesSheetConfig> sheetConfigs;
	
	/**  
	 * @Fields chartsBase64 : chart的base64数据
	 */
	private JSONObject chartsBase64;
	
	/**  
	 * @Fields imageInfos : 计算位置后的图片信息
	 */
	private Map<String, Map<String, Object>> imageInfos;
	
	/**  
	 * @Fields backImages : 背景图片，通过插入图片添加的图片
	 */
	private Map<String, String> backImages;
	
}
