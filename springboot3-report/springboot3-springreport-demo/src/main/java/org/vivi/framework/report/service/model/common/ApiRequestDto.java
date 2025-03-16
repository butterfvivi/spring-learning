package org.vivi.framework.report.service.model.common;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**  
 * @ClassName: ApiRequestDto
 * @Description: api接口请求用实体类
*/
@Data
public class ApiRequestDto {

	/**  
	 * @Fields url : url
	 */
	private String url;
	
	/**  
	 * @Fields requestType : 请求方式 post get 默认post
	 */
	private String requestType = "post";
	
	/**  
	 * @Fields params : 参数
	 */
	private List<Map<String, Object>> params;
	
	/**  
	 * @Fields resultType : 返回值类型 String Array Object  默认String
	 */
	private String resultType = "String";
	
	/**  
	 * @Fields resultTypeProp : 返回值属性，当resultType = Object时需要用到该参数
	 */
	private List<Map<String, String>> resultTypeProp;
	
	 /**  
	 * @Fields screenComponentId : 大屏组件id
	 */
	private Long screenComponentId;
	
	/**  
	 * @Fields urlParams : url中传过来的参数
	 */
	private Map<String, Object> urlParams;
	
	/**  
	 * @Fields resultTypePropPerfix : 返回值前缀
	 */
	private String resultTypePropPerfix;
	
	/**  
	 * @Fields isBindComponent : 是否是绑定组件
	 */
	private Integer isBindComponent;
	
	/**  
	 * @Fields drillProp : 下钻属性
	 */
	private List<Map<String, String>> drillProp;
	
}
