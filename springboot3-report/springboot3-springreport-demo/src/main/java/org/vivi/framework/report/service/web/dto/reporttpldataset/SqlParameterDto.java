package org.vivi.framework.report.service.web.dto.reporttpldataset;

import lombok.Data;
import org.vivi.framework.report.service.common.entity.BaseEntity;

/**
* <p>Title: SqlParameterDto</p>
* <p>Description: sql参数用实体类</p>
*/
@Data
public class SqlParameterDto extends BaseEntity {

	/**
	* @Feilds:paramName 参数名称
	*/
	private String paramName;
	
	/**
	* @Feilds:paramCode 参数代码
	*/
	private String paramCode;
	
	/**
	* @Feilds:paramType 参数类型
	*/
	private String paramType;
	
	/**
	* @Feilds:paramDefault 默认值
	*/
	private String paramDefault;
	
	/**
	* @Feilds:paramRequired 是否必须
	*/
	private String paramRequired;
}
