package org.vivi.framework.report.service.web.dto.reporttpl;

import lombok.Data;

import java.util.List;

@Data
public class MesLuckysheetsTplDto {

	/**  
	 * @Fields id : 模板id
	 */
	private Long tplId;
	
	/**  
	 * @Fields isParamMerge : 是否合并参数
	 */
	private Integer isParamMerge;
	
	/**  
	 * @Fields configs : sheet配置信息
	 */
	private List<MesLuckySheetTplDto> configs;
	
	/**  
	 * @Fields delSheetsIndex : 删除的sheet页
	 */
	private List<String> delSheetsIndex;
}
