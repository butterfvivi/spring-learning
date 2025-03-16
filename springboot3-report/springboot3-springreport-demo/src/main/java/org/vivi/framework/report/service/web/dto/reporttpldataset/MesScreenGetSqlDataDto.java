package org.vivi.framework.report.service.web.dto.reporttpldataset;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**  
 * @ClassName: MesScreenGetSqlDataDto
 * @Description: 大屏根据sql获取数据用参数实体类
*/
@Data
public class MesScreenGetSqlDataDto {

	/**  
	 * @Fields dataSetId : 数据集id
	 */
	private Long dataSetId;
	
	/**  
	 * @Fields props : sql属性
	 */
	private List<String> props;
	
	/**  
	 * @Fields params : 参数
	 */
	private Map<String, Object> params;
	
	/**  
	 * @Fields screenComponentId : 大屏组件id
	 */
	private Long screenComponentId;
	
	/**  
	 * @Fields urlParams : url中传过来的参数
	 */
	private Map<String, Object> urlParams;
	
	/**  
	 * @Fields isCategory : 是否目录
	 */
	private boolean categoryType = false;
	
	/**  
	 * @Fields drillProp : 下钻属性
	 */
	private List<String> drillProp;
	
	/**  
	 * @Fields isBindComponent : 是否是绑定组件
	 */
	private Integer isBindComponent;
}
