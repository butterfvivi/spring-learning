package org.vivi.framework.report.service.common.enums;


import org.vivi.framework.report.service.common.enums.base.BaseCharEnum;

/**
 * @ClassName: RequestTypeEnum
 * @Description: 请求类型用枚举类
*/
public enum RequestTypeEnum implements BaseCharEnum {

	POST {
		public String getCode() {
			return "post";
		}

		public String getName() {
			return "post请求";
		}
	},
	GET {
		public String getCode() {
			return "get";
		}

		public String getName() {
			return "get请求";
		}
	},
	
}
