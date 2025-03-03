package org.vivi.framework.report.service.web.dto.reporttpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.vivi.framework.report.service.model.reportsheetpdfprintsetting.ReportSheetPdfPrintSetting;

import java.util.List;
import java.util.Map;

/**  
 * @ClassName: ResLuckySheetTplSettingsDto
 * @Description: luckysheet模板返回结果用实体类
*/

/**  
 * @ClassName: ResLuckySheetTplSettingsDto
*/
@Data
public class ResLuckySheetTplSettingsDto {

	
	/**  
	 * @Fields hyperlinks : 超链接
	 */
	private Map<String, Map<String, Object>> hyperlinks;
	
	/**  
	 * @Fields cellDatas : 单元格数据
	 */
	private List<Map<String, Object>> cellDatas;
	
	/**  
	 * @Fields extraCustomCellConfigs : 自定义额外单元格配置
	 */
	private Map<String, JSONObject> extraCustomCellConfigs;
	
	/**  
	 * @Fields config :  列宽行高等额外配置
	 */
	private Map<String, Object> config;
	
	
	/**  
	 * @Fields blockData : 循环块
	 */
	private List<Map<String, Object>> blockData;
	
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
    private JSONArray calcChain = new JSONArray();
    
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
    private JSONArray datasourceConfig;
    
    /**  
     * @Fields cellFormats : 单元格格式
     */
    private Map<String, Object> cellFormats;
    
    /**  
     * @Fields sheetId : sheet id
     */
    private Long sheetId;
    
    /**  
     * @Fields luckysheetAlternateformatSave : 交替颜色配置
     */
    private JSONArray luckysheetAlternateformatSave;
    
    /**  
     * @Fields chart : 图表
     */
    private JSONArray chart;
    
    /**  
     * @Fields chartXaxisData : 图表x轴配置
     */
    private JSONArray chartXaxisData;
    
    /**  
     * @Fields dataVerification : 数据校验项
     */
    private JSONObject dataVerification;
    
    /**  
     * @Fields printSettings : 打印设置
     */
    private ReportSheetPdfPrintSetting reportSheetPdfPrintSetting;
    
    /**  
     * @Fields pageDivider : 分页线配置
     */
    private JSONArray pageDivider;
    
    /**  
     * @Fields luckysheetConditionformatSave : 条件格式配置
     */
    private JSONArray luckysheetConditionformatSave;
    
}
