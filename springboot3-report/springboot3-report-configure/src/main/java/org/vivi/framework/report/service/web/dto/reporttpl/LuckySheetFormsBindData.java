package org.vivi.framework.report.service.web.dto.reporttpl;

import com.alibaba.fastjson.JSONObject;
import org.vivi.framework.report.service.common.enums.CellExtendEnum;
import org.vivi.framework.report.service.common.enums.YesNoEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LuckySheetFormsBindData {
	
	/**  
	 * @Fields reportCellId : 单元格id
	 */
	private Long reportCellId;

	/**
	* @Feilds:coordsx 横坐标
	*/
	private Integer coordsx;
	
	/**
	* @Feilds:coordsy 纵坐标
	*/
	private Integer coordsy;
	
	/**
	* @Feilds:aggregateType 聚合类型 list:列表 group分组
	*/
	private String aggregateType;
	
	/**  
	 * @Fields datas : 数据
	 */
	private List<List<Map<String, Object>>> datas;
	
	/**  
	 * @Fields filterDatas : 过滤后的数据
	 */
	private List<List<Map<String, Object>>> filterDatas;
	
	
	/** cell_extend - 单元格扩展方向 1不扩展 2纵向扩展 2横向扩展 */
    private Integer cellExtend;
    
    /**  
     * @Fields property : 属性值
     */
    private String property;
    
    /**  
     * @Fields isFirst : 是否是第一次聚合计算
     */
    private Integer isFirst;
    
    /**  
     * @Fields cellValueType : 单元格类型 1固定值 2变量
     */
    private Integer cellValueType;
    
    /**  
     * @Fields cellData : 单元格配置
     */
    private Map<String, Object> cellData;
    
    
    /**  
     * @Fields cellValue : 单元格值
     */
    private String cellValue;
    
    /**  
     * @Fields warning : 是否预警
     */
    private Boolean warning = false;
    
    /**  
     * @Fields warningRules : 预警规则
     */
    private String warningRules = ">=";
    
    /**  
     * @Fields warningColor : 预警颜色
     */
    private String warningColor;
    
    /**  
     * @Fields threshold : 预警阈值
     */
    private String threshold;
    
    /**  
     * @Fields warningContent : 预警内容
     */
    private String warningContent;
    
    /**  
     * @Fields cellAttrs : 单元格属性
     */
    private JSONObject cellAttrs;
    
    /**  
     * @Fields isMerge : 是否合并单元格
     */
    private Integer isMerge = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields rowSpan : 合并行数
     */
    private Integer rowSpan;
    
    /**  
     * @Fields colSpan : 合并列数
     */
    private Integer colSpan;
    
    /**  
     * @Fields digit : 小数位数
     */
    private Integer digit;
    
    /** cell_function - 函数类型 0无函数 1求和 2求平均值 3最大值 4最小值 */
    private Integer cellFunction;
    
    /**  
     * @Fields dataFrom : 数据来源 1默认 2原始数据 3单元格
     */
    private Integer dataFrom; 
    
    /**  
     * @Fields groupSummaryDependencyR : 分组聚合计算依赖横坐标
     */
    private String groupSummaryDependencyR;
    
    /**  
     * @Fields groupSummaryDependencyC : 分组聚合计算依赖纵坐标
     */
    private String groupSummaryDependencyC;
    
    /**  
     * @Fields isFunction : 是否是函数
     */
    private Integer isFunction = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields lastAggregateType : 上次操作的聚合方式 聚合类型 list:列表 group分组 summary汇总
     */
    private String lastAggregateType;
    
    /**  
     * @Fields lastCellExtend : 上组数据扩展方向
     */
    private Integer lastCellExtend = CellExtendEnum.NOEXTEND.getCode();
    
    /**  
     * @Fields datasetName : 数据集名称
     */
    private String datasetName;
    
    /**  
     * @Fields multiDatas : 多数据集数据
     */
    private Map<String, List<List<Map<String, Object>>>> multiDatas;
    
    /** is_conditions - 是否有单元格过滤条件 1是 2否 */
    private Integer isConditions;

    /** cell_conditions - 单元格过滤条件 */
    private String cellConditions;
    
    /**  
     * @Fields groupProperty : 分组属性
     */
    private String groupProperty;
    
    /** unit_transfer - 是否数值单位转换 */
    private Boolean unitTransfer;

    /** transfer_type - 转换方式 1乘法 2除法 */
    private Integer transferType;

    /** multiple - 倍数 */
    private String multiple;
    
    /**  
     * @Fields cellConditionType : 过滤条件类型
     */
    private String cellConditionType = "and";
    
    /** lastIsConditions - 上一个单元格数据是否有单元格过滤条件 1是 2否 */
    private Integer lastIsConditions = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields sheetIndex : sheetIndex
     */
    private String sheetIndex;
}
