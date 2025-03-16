package org.vivi.framework.report.service.common.utils;

import lombok.Data;

@Data
public class PropertyRatio {

	/**  
	 * @Fields property : 属性
	 */
	private String property;
	
	/**  
	 * @Fields ratio : 相似度
	 */
	private float ratio;
	
}
