package org.vivi.framework.report.service.web.dto.reportdatasource;

import lombok.Data;
import org.vivi.framework.report.service.common.entity.BaseEntity;

/**  
 * @ClassName: ApiTestResultDto
 * @Description: api接口测试返回结果用实体类
*/
@Data
public class ApiTestResultDto extends BaseEntity {

	/**  
	 * @Fields apiResult : 接口返回结果
	 */
	private String apiResult;
}
