package org.vivi.framework.report.service.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.service.model.common.ApiRequestDto;

import java.io.IOException;
import java.util.Map;

/**  
 * @ClassName: ICommonService
 * @Description: 共通服务
*/
public interface ICommonService {

	/**  
	 * @Title: upload
	 * @Description: 上传文件
	 */
	Object upload(MultipartFile file) throws IOException;
	
	/**  
	 * @MethodName: upload
	 * @Description: 字节流上传图片
	 */
	Map<String, String> upload(byte[] bytes,String fileName);
	
	/**  
	 * @Title: upload
	 * @Description: 上传文件
	 */
	Object uploadFile(MultipartFile file) throws IOException;
	
	/**  
	 * @Title: apiTest
	 * @Description: 接口测试
	 */
	Object apiTest(ApiRequestDto apiRequestDto);
	
	/**  
	 * @MethodName: parseXlsxByUrl
	 * @Description: 通过url解析xlsx文件
	 */
	JSONArray parseXlsxByUrl(JSONObject model) throws Exception;
	
}
