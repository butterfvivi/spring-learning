package org.vivi.framework.report.service.common.utils;

import java.util.UUID;

/**  
 * @ClassName: UUIDUtil
 * @Description: uuid工具类
*/
public class UUIDUtil {

	/**  
	 * @Title: getUUID
	 * @Description: 生成uuid
	 */
	public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
   }
	
	public static void main(String[] args) {
		System.out.println(UUIDUtil.getUUID());
	}

}
