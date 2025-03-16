package org.vivi.framework.report.service.web.dto.reporttpl;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.vivi.framework.report.service.common.enums.YesNoEnum;

import java.util.List;
import java.util.Map;

@Data
public class ResSheetsSettingsDto {

	/**  
	 * @Fields isParamMerge : 是否合并参数
	 */
	private Integer isParamMerge;
	
	/**  
	 * @Fields settings : sheet配置
	 */
	private List<ResLuckySheetTplSettingsDto> settings;
	
	/**  
     * @Fields sheetIndexIdMap : sheet的index 和 sheet的id map，可以通过sheet的index获取sheet的id
     */
    private Map<String, Long> sheetIndexIdMap;
    
    /**  
     * @Fields tplName : 模板名称
     */
    private String tplName;
    
    /**  
     * @Fields isCreator : 是否是创建人
     */
    private boolean isCreator;
    
    /**  
     * @Fields rangeAuth : 权限
     */
    private JSONObject sheetRangeAuth;
    
    /**  
     * @Fields creatorName : 创建者名称
     */
    private String creatorName;
    
    /**  
     * @Fields isThirdParty : 是否是第三方调用 1是 2否
     */
    private Integer isThirdParty = YesNoEnum.NO.getCode();
    
    /**  
     * @Fields tplType : 报表类型 1展示报表 2填报报表
     */
    private Integer tplType = 1;
    
}
