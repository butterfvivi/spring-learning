package org.vivi.framework.report.service.common.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.vivi.framework.report.service.common.enums.YesNoEnum;

import java.util.List;
import java.util.Map;

/**  
 * @ClassName: MesSheetConfig
 * @Description: 导出excel 单个sheet配置
*/
@Data
public class MesSheetConfig {

	/**  
	 * 单元格数据
	 */
	private List<Map<String, Object>> cellDatas;
	
	/**  
	 * 最大行最大列，创建sheet行列用
	 */
	private Map<String, Integer> maxXAndY;
	
	/**  
	 * 超链接配置
	 */
	private Map<String, Map<String, Object>> hyperlinks;
	
	/**  
	 * 边框信息
	 */
	private List<Object> borderInfos;
	
	/**  
	 * @Fields rowlen : 行高信息
	 */
	private Map<String, Object> rowlen;
	
	/**  
	 * @Fields columnlen : 列宽信息
	 */
	private Map<String, Object> columnlen;
	
	/**  
	 * @Fields sheetname : sheet名称
	 */
	private String sheetname;
	
	/**  
	 * @Fields frozen : 行列冻结信息
	 */
	private JSONObject frozen;
	
	/**  
	 * @Fields images : 图片信息
	 */
	private JSONObject images;
	
	/**  
	 * @Fields base64Images : base64格式图片
	 */
	private JSONObject base64Images;
	
	/**  
	 * @Fields imageDatas : 图片数据
	 */
	private List<JSONObject> imageDatas;
	
	/**  
	 * @Fields chart : 图表
	 */
	private JSONArray chart;
	
	/**  
	 * @Fields chartCells : 图表对应的单元格坐标信息
	 */
	private JSONObject chartCells;
	
	/**  
	 * @Fields colhidden : 隐藏列
	 */
	private JSONObject colhidden;
	
	/**  
	 * @Fields rowhidden : 隐藏行
	 */
	private JSONObject rowhidden;
	
	/**  
	 * @Fields dataVerification : 数据校验
	 */
	private JSONObject dataVerification;
	
	/**  
	 * @Fields authority : 保护信息
	 */
	private JSONObject authority;
	
	/**  
	 * @Fields isCoedit : 是否是协同编辑 默认否
	 */
	private Integer isCoedit = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields merge : 合并单元格信息
	 */
	private JSONObject merge;
	
	/**  
	 * @Fields filter : 过滤信息
	 */
	private JSONObject filter;
	
	/**  
     * @Fields luckysheetConditionformatSave : 条件格式配置
     */
    private JSONArray luckysheetConditionformatSave;
    
    /**  
     * @Fields wrapText : 自动换行的行标记
     */
    private Map<String, Integer> wrapText;
    
    /**  
     * @Fields sheetIndex : sheet唯一标识
     */
    private String sheetIndex;
    
    /**  
	 * @Fields noViewAuthCells : 没有查看权限的单元格
	 */
	private List<String> noViewAuthCells;
}
