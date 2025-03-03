package org.vivi.framework.report.service.web.dto.reporttpl;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.vivi.framework.report.service.common.enums.CellExtendEnum;
import org.vivi.framework.report.service.common.enums.YesNoEnum;

import java.util.List;
import java.util.Map;

@Data
public class LuckySheetBindData {
	
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
	 * @Fields datas : 数据
	 */
	private List<List<Map<String, Object>>> datas;
	
	/**  
	 * @Fields filterDatas : 过滤后的数据
	 */
	private List<List<Map<String, Object>>> filterDatas;
	
	/**
	* @Feilds:aggregateType 聚合类型 list:列表 group分组
	*/
	private String aggregateType;
	
	/** cell_extend - 单元格扩展方向 1不扩展 2纵向扩展 2横向扩展 */
    private Integer cellExtend;
    
    /**  
     * @Fields lastAggregateType : 上次操作的聚合方式 聚合类型 list:列表 group分组 summary汇总
     */
    private String lastAggregateType;
    
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
     * @Fields rowSpan : 合并行数
     */
    private Integer rowSpan;
    
    /**  
     * @Fields colSpan : 合并列数
     */
    private Integer colSpan;
    
    /**  
     * @Fields isLink : 是否是超链接 1是 2否
     */
    private Integer isLink = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields cellData : 单元格配置
     */
    private Map<String, Object> cellData;
    
    /**  
     * @Fields linkConfig : 超链接配置
     */
    private Map<String, Object> linkConfig;
    
    /**  
     * @Fields isMerge : 是否合并单元格
     */
    private Integer isMerge = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields isFunction : 是否是函数
     */
    private Integer isFunction = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields digit : 小数位数
     */
    private Integer digit;
    
    /** cell_function - 函数类型 0无函数 1求和 2求平均值 3最大值 4最小值 */
    private Integer cellFunction;
    
    /**  
     * @Fields groupSummaryDependencyR : 分组聚合计算依赖横坐标
     */
    private String groupSummaryDependencyR;
    
    /**  
     * @Fields groupSummaryDependencyC : 分组聚合计算依赖纵坐标
     */
    private String groupSummaryDependencyC;
    
    /**  
     * @Fields dataFrom : 数据来源 1默认 2原始数据 3单元格
     */
    private Integer dataFrom; 
    
    /**  
     * @Fields downProperty : 向下扩展属性
     */
    private String downProperty;
    
    /**  
     * @Fields rightProperty : 向右扩展属性
     */
    private String rightProperty;

    /**  
     * @Fields isGroupMerge : 合并单元格是否合一
     */
    private Boolean isGroupMerge = false;
    
    /**  
     * @Fields lastIsGroupMerge : 上组合并单元格是否合一
     */
    private Boolean lastIsGroupMerge = false;
    
    /**  
     * @Fields lastCellExtend : 上组数据扩展方向
     */
    private Integer lastCellExtend = CellExtendEnum.NOEXTEND.getCode();
    
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
     * @Fields multiDatas : 多数据集数据
     */
    private Map<String, List<List<Map<String, Object>>>> multiDatas;
    
    /**  
     * @Fields datasetName : 数据集名称
     */
    private String datasetName;
    
    /**  
     * @Fields isRelied : 数据是否被其他单元格依赖 1是 2否 
     */
    private Integer isRelied;
    
    /**  
     * @Fields relyCells : 依赖单元格
     */
    private String relyCells;
    
    /**  
     * @Fields recalculateCoords : 是否重新计算坐标 1是 2否 默认1
     */
    private Integer recalculateCoords = YesNoEnum.YES.getCode();
    
    /**  
     * @Fields lastCoordsx : 上次计算的横坐标
     */
    private Integer lastCoordsx;
    
    /**  
     * @Fields lastCoordsy : 上次计算的纵坐标
     */
    private Integer lastCoordsy;
    
    /**  
     * @Fields isRelyCell : 是否是依赖单元格处理 1是 2否 默认2
     */
    private Integer isRelyCell = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields relyCellExtend : 依赖单元格的扩展方向 单元格扩展方向 1不扩展 3纵向扩展 2横向扩展 4交叉扩展
     */
    private Integer relyCellExtend = CellExtendEnum.NOEXTEND.getCode();
    
    /**  
     * @Fields relyCoordsx : 依赖单元格的横坐标
     */
    private Integer relyCoordsx;
    
    /**  
     * @Fields relyCoordsy : 依赖单元格纵坐标
     */
    private Integer relyCoordsy;
    
    /**  
     * @Fields isDict : 是否是数据字典
     */
    private Boolean isDict = false;
    
    /** datasource_id - 数据字典数据源id */
    private Long datasourceId;

    /** dict_type - 数据字典类型 */
    private String dictType;
    
    /** alternate_format - 是否交替颜色1是 2否 */
    private Integer alternateFormat = YesNoEnum.NO.getCode();

    /** alternate_format_bc_odd - 奇数行背景颜色 */
    private String alternateFormatBcOdd;

    /** alternate_format_bc_even - 偶数行背景颜色 */
    private String alternateFormatBcEven;

    /** alternate_format_fc_odd - 奇数行字体颜色 */
    private String alternateFormatFcOdd;

    /** alternate_format_fc_even - 偶数行字体颜色 */
    private String alternateFormatFcEven;
    
    /**  
     * @Fields relyCrossIndex : 交叉索引
     */
    private Integer relyCrossIndex;
    
    /**  
     * @Fields reliedCellSize : 依赖单元格数据
     */
    private Integer reliedCellSize = 0;
    
    /**  
     * @Fields groupProperty : 分组属性
     */
    private String groupProperty;
    
    /** is_conditions - 是否有单元格过滤条件 1是 2否 */
    private Integer isConditions;

    /** cell_conditions - 单元格过滤条件 */
    private String cellConditions;
    
    /**  
     * @Fields isChartCell : 是否图表单元格 1是 2否
     */
    private Integer isChartCell = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields chartId : 图表id
     */
    private String chartId;
    
    /** is_data_verification - 是否是数据校验项 1是 2否 */
    private Integer isDataVerification = YesNoEnum.NO.getCode();

    /** data_verification - 数据校验项目 */
    private String dataVerification;
    
    private Boolean isDrill;

    /** drill_id - 下钻报表id */
    private Long drillId;

    /** drill_attrs - 下钻属性 */
    private String drillAttrs;
    
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
    
    /**  
     * @Fields blockDatas : 循环块数据
     */
    private Map<String, List<List<Map<String, Object>>>> blockDatas;
    
    /** is_subtotal - 是否需要小计 */
    private Boolean isSubtotal;

    /** subtotal_cells - 小计单元格 */
    private String subtotalCells;
    
    /**  
     * @Fields subTotalName : 小计名称
     */
    private String subTotalName = "小计";

    /** is_subtotal_calc - 是否有小计公式链 */
    private Boolean isSubtotalCalc;

    /** subtotal_calc - 小计公式链 */
    private String subtotalCalc;
    
    /**  
     * @Fields groupSubtotalCount : 分组小计数量
     */
    private Map<Integer, Integer> groupSubtotalCount;
    
    /**  
     * @Fields sheetId : sheet主键
     */
    private Long sheetId;
    
    /**  
     * @Fields subtotalAttrs : 小计属性
     */
    private JSONObject subtotalAttrs;
    
    /**  
     * @Fields sheetIndex : sheetIndex
     */
    private String sheetIndex;
    
    /** is_chart_attr - 是否是图表中的单元格属性 1是 2否 */
    private Integer isChartAttr;
    
    private Integer startx;
    
    private Integer starty;
    
    private Integer endx;
    
    private Integer endy;
    
    /** lastIsConditions - 上一个单元格数据是否有单元格过滤条件 1是 2否 */
    private Integer lastIsConditions = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields originalData : 原始数据
     */
    private List<Map<String, Object>> originalData;
    
    /**  
     * @Fields relyIndex : 依赖数据的index
     */
    private Integer relyIndex;
    
    /**  
     * @Fields cellContent : 单元格文本信息(去掉数据集信息)
     */
    private String cellText;
    
    /**  
     * @Fields cellFillType : 数据填充方式 1插入 2覆盖
     */
    private Integer cellFillType = 1;
    
    /**  
     * @Fields tplType : 是否是填报数据 1是 2否
     */
    private int tplType = 1;
}
