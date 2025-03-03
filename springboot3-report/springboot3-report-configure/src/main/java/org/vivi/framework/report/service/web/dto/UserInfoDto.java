package org.vivi.framework.report.service.web.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/** 
* @ClassName: UserInfoDto 
* @Description: 用户登陆信息存储用实体类
*
*/
@Data
public class UserInfoDto implements Serializable{

	/** 
	* @Fields token : 登陆标识
	*/
	private String token;
	
	/**
	* @Fields userName : 用户名
	*/
	
	private String userName;
	/**
	* @Fields userId 用户表主键userId
	*/
	private Long userId;
	
	/**
	* @Fields userRealName 用户真实姓名
	*/
	private String userRealName;
	
}
