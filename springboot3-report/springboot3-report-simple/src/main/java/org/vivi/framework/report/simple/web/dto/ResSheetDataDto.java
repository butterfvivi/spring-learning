package org.vivi.framework.report.simple.web.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ResSheetDataDto {

	
	/**  
	 * @Fields hyperlinks : 超链接
	 */
	private Map<String, Map<String, Object>> hyperlinks;
	
	/**  
	 * @Fields cellDatas : 单元格数据
	 */
	private List<Map<String, Object>> cellDatas;
	
	/**  
	 * @Fields config : sheet页面的配置信息，例如行宽行高
	 */
	private Map<String, Object> config;
	
	/**  
	 * @Fields paginationMap : 分页信息
	 */
	private Map<String, Map<String, Integer>> pagination;
	
	/**  
	 * @Fields maxXAndY : 最大横坐标和纵坐标
	 */
	private Map<String, Integer> maxXAndY;
	
	
	/**  
	 * @Fields mergePagination : 合并参数分页内容
	 */
	private Map<String, Object> mergePagination;
	
	/**  
	 * @Fields frozen : 冻结信息
	 */
	private Map<String, Object> frozen;
	
	/**  
	 * @Fields images : 图片
	 */
	private Object images;
	
	/** sheet_index - sheet页唯一索引 */
    private String sheetIndex;

    /** calc_chain - 计算链，有公式的单元格信息 */
    private List<JSONObject> calcChain;
    
    /**  
     * @Fields sheetName : sheet名称
     */
    private String sheetName;
    
    /**  
     * @Fields sheetOrder : sheet顺序
     */
    private Integer sheetOrder;
    
    /**  
     * @Fields datasourceConfig : 绑定数据配置
     */
//    private JSONArray datasourceConfig;
    
    /**  
     * @Fields extendCellOrigin : 扩展单元格的坐标与原始单元格坐标的对应关系
     */
    private Map<String, JSONObject> extendCellOrigin;
    
    /**  
	 * @Fields extraCustomCellConfigs : 自定义额外单元格配置
	 */
	private Map<String, JSONObject> extraCustomCellConfigs;
	
	/**  
	 * @Fields columnStartCoords : 列对应的起始坐标
	 */
	private Map<String, JSONObject> columnStartCoords;
	
	/**  
	 * @Fields cellDatasourceConfig : 单元格数据源配置对应关系
	 */
	private List<Map<String, JSONObject>> cellDatasourceConfigs;
	
	/**  
	 * @Fields tableKeys : 表格主键
	 */
	private Map<String, JSONObject> tableKeys;
	
	/**  
	 * @Fields imageDatas : 图片数据
	 */
	private List<JSONObject> imageDatas;
	
	
	/**  
	 * @Fields base64Imgs : base64图片
	 */
	private JSONObject base64Imgs;
	
	/**  
	 * @Fields imgCells : 有图片的单元格
	 */
	private Map<String, JSONObject> imgCells;
	
	/**  
	 * @Fields allowEditConfigs : 是否允许单元格进行编辑配置信息
	 */
	Map<String, Boolean> allowEditConfigs;
	
	/**  
	 * @Fields nowFunction : now函数
	 */
	private Map<String, Object> nowFunction = new HashMap<>();
	
	/**  
	 * @Fields startXAndY : 起始横坐标和纵坐标
	 */
	private Map<String, Integer> startXAndY = new HashMap<>();
	
	/**  
	 * @Fields functionCellFormat : 函数格式
	 */
	private Map<String, Object> functionCellFormat = new HashMap<>();
	
	/**  
	 * @Fields chart : 图表
	 */
	private JSONArray chart;
	
	/**  
	 * @Fields chartCells : 图表坐标
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
	 * @Fields drillCells : 下钻单元格
	 */
	private JSONObject drillCells;
	
	/**  
	 * @Fields xxbtScreenshot : 斜线表头截图
	 */
	private JSONObject xxbtScreenshot;
	
	/**  
	 * @Fields imgInfo : 插入图片信息
	 */
	private JSONObject imgInfo;
	
	/**  
	 * @Fields replacedData : 被缓存替换的数据
	 */
	private Map<String, Object> replacedData; 
	
	private List<Object> newCellDatas;
	
	/**  
	 * @Fields cellBindData : 坐标对应的单元格数据
	 */
	private Map<String, SheetBindData> cellBindData;
	
	/**  
	 * @Fields pageDivider : 分页线配置
	 */
	private JSONArray pageDivider;
	
	/**  
     * @Fields luckysheetConditionformatSave : 条件格式配置
     */
    private JSONArray luckysheetConditionformatSave;
    
    /**  
     * @Fields wrapDatas : 自动换行的数据
     */
    private JSONArray wrapDatas;
    
    /**  
     * @Fields autoFillAttrs : 自动填充属性
     */
    private Map<String, JSONObject> autoFillAttrs;
    
    /**  
     * @Fields deleteTypes : 删除规则
     */
    private Map<String, JSONObject> deleteTypes;;

	public Map<String, Integer> getMaxXAndY() {
		if(maxXAndY == null)
		{
			maxXAndY = new HashMap<>();
			maxXAndY.put("maxX", 0);
			maxXAndY.put("maxY", 0);
		}
		return maxXAndY;
	}
}
