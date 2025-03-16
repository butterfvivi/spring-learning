package org.vivi.framework.report.service.web.dto.reporttpl;

import lombok.Data;

/**  
 * @ClassName: AlternateformatDto
 * @Description: 交替颜色配置实体类
*/
@Data
public class AlternateformatDto {

	/**  
	 * @Fields fcOdd : 奇数行字体颜色
	 */
	private String fcOdd;
	
	/**  
	 * @Fields fcEven : 偶数行字体颜色
	 */
	private String fcEven;
	
	/**  
	 * @Fields bcOdd : 奇数行背景颜色
	 */
	private String bcOdd;
	
	/**  
	 * @Fields bcEven : 偶数行背景色
	 */
	private String bcEven;
}
