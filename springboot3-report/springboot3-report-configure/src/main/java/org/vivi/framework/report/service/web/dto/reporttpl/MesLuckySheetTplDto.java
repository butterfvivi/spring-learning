package org.vivi.framework.report.service.web.dto.reporttpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.vivi.framework.report.service.model.reportsheetpdfprintsetting.ReportSheetPdfPrintSetting;

import java.util.List;
import java.util.Map;

/**  
 * @ClassName: MesLuckySheetTplDto
 * @Description: luckysheet模板实体类
 * @author caiyang
 * @date 2022-02-01 10:46:48 
*/  
@Data
public class MesLuckySheetTplDto {
	
	/**  
	 * @Fields id : 模板id
	 */
//	private Long tplId;

	/**  
	 * @Fields title : 模板标题
	 */
	private String title;
	
	/**  
	 * @Fields hyperlinks : 超链接
	 */
	private Map<String, JSONObject> hyperlinks;
	
	/**  
	 * @Fields cellDatas : 单元格数据
	 */
	private List<Map<String, Object>> cellDatas;
	
	/**  
	 * @Fields blockCellDatas : 循环块数据
	 */
	private List<Map<String, Object>> blockCellDatas;
	
	/**  
	 * @Fields extraCustomCellConfigs : 自定义额外单元格配置
	 */
	private Map<String, JSONObject> extraCustomCellConfigs;
	
	/**  
	 * @Fields config : 列宽行高等额外配置
	 */
	private Map<String, Object> config;
	
	/**  
	 * @Fields isParamMerge : 是否合并参数
	 */
//	private Integer isParamMerge;
	
	/**  
	 * @Fields blockCells : 数据块单元格
	 */
	private Map<String, List<Map<String, Object>>> blockCells;
	
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
    private JSONArray calcChain;
    
    /**  
     * @Fields sheetOrder : TODO
     */
    private Integer sheetOrder;
    
    /**  
     * @Fields sheetName : sheet名称
     */
    private String sheetName;
    
    /**  
     * @Fields datasourceConfig : 绑定数据配置
     */
    private JSONArray datasourceConfig;
    
    /**  
     * @Fields luckysheetAlternateformatSave : 交替颜色配置
     */
    private JSONArray luckysheetAlternateformatSave;
    
    /**  
     * @Fields chart : 图表配置
     */
    private JSONArray chart;
    
    /**  
     * @Fields chartXaxisData : 图表x轴配置
     */
    private JSONArray chartXaxisData;
    
    /**  
     * @Fields chartCells : 图表对应的单元格
     */
    private JSONArray chartCells;
    
    /**  
     * @Fields dataVerification : 数据校验项
     */
    private JSONObject dataVerification;
    
    /**  
     * @Fields xxbtScreenShot : 斜线表头截图
     */
    private JSONObject xxbtScreenShot;
    
    /**  
     * @Fields printSettings : pdf/打印设置
     */
    private ReportSheetPdfPrintSetting printSettings;
    
    /**  
     * @Fields pageDivider : 分页线配置信息
     */
    private List<Integer> pageDivider;
    
    /**  
     * @Fields sheetAuth : sheet页选区权限
     */
    private JSONObject sheetRangeAuth;
    
    /**  
     * @Fields luckysheetConditionformatSave : 条件格式配置
     */
    private JSONArray luckysheetConditionformatSave;
}
