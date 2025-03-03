package org.vivi.framework.report.service.web.dto.reporttpl;

import lombok.Data;
import org.vivi.framework.report.service.common.entity.BaseEntity;

import java.util.Map;

@Data
public class SaveLuckySheetTplDto extends BaseEntity {
	
	/**  
	 * @Fields printSettings : 打印设置id
	 */
	private Map<String, Long> printSettings;
}
