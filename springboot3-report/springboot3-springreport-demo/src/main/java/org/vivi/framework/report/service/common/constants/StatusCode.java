package org.vivi.framework.report.service.common.constants;

/**   
 * @ClassName:  StatusCode   
 * @Description:状态码
 * @author: caiyang 
 * @date:   2019年2月15日 上午9:20:45   
 *      
 */
public class StatusCode {
	
	/**
	 * :执行成功
	 */
	public static String SUCCESS = "200";
	
	/**
	 * 执行失败异常
	 */
	public static String FAILURE = "50001";
	
	/**
	 * 参数校验异常
	 */
	public static String CHECK_FAILURE = "50002";

	/**
	 * TOKEN失效
	 */
	public static String TOKEN_FAILURE = "50004";

	/**
	 * @Feilds:TOKEN_ERROR token错误
	 */
	public static String TOKEN_ERROR = "50005";
}
