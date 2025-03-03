package org.vivi.framework.report.service.web.dto.reporttpldataset;

import lombok.Data;
import org.vivi.framework.report.service.common.entity.BaseEntity;
import org.vivi.framework.report.service.common.enums.YesNoEnum;

import java.util.Map;

/**
* <p>Title: ReportParamDto</p>
* <p>Description: 报表参数用实体类</p>
*/
@Data
public class ReportParamDto extends BaseEntity {

	/**
	* @Feilds:paramName 参数名称
	*/
	private String paramName;
	
	/**
	* @Feilds:paramCode 参数编码
	*/
	private String paramCode;
	
	/**
	* @Feilds:paramType 参数类型
	*/
	private String paramType;
	
	/**
	* @Feilds:paramDefault 默认值
	*/
	private String paramDefault;
	
	/**
	* @Feilds:paramRequired 是否必填
	*/
	private Integer paramRequired;
	
	/**
	* @Feilds:isPagination 是否需要分页
	*/
	private Integer isPagination;
	
	/**
	* @Feilds:rules 校验规则
	*/
	private Map<String, Object> rules;
	
	/**
	* @Feilds:selectType 下拉框选择内容来源 1自定义 2sql
	*/
	private String selectType;
	
	/**
	* @Feilds:selectContent 下拉选择内容
	*/
	private String selectContent;
	
	/**
	* @Feilds:selectData 下拉框数据
	*/
	private Object selectData;
	
	/**  
	 * @Fields disabled : 是否禁用 默认false 不禁用
	 */
	private boolean disabled = false;
	
	/**  
	 * @Fields isRelyOnParams : 是否依赖其他参数
	 */
	private Integer isRelyOnParams;
	
	/**  
	 * @Fields relyOnParams : 依赖参数代码
	 */
	private String relyOnParams;
	
	/**  
	 * @Fields datasourceId : 数据源id
	 */
	private Long datasourceId;
	
	/**  
	 * @Fields dateFormt : 日期格式
	 */
	private String dateFormat;
	
	/**  
	 * @Fields paramHidden : 参数是否隐藏 1是 2否
	 */
	private Integer paramHidden = YesNoEnum.NO.getCode();
	
	/**  
	 * @Fields dateDefaultValue : 日期格式默认值
	 */
	private String dateDefaultValue;
	
	/**  
	 * @Fields checkStrictly : 是否父子联动
	 */
	private Integer checkStrictly;
	
	/**  
	 * @Fields init : 是否已经初始化数据
	 */
	private boolean init = false;
	
	/**  
	 * @Fields paramPrefix : api参数前缀
	 */
	private String paramPrefix;
	
	/**  
	 * @Fields componentType : 组件类型
	 */
	private String componentType;
}
