package org.vivi.framework.report.service.common.enums;


import org.vivi.framework.report.service.common.enums.base.BaseIntEnum;

/**
* @ClassName: DelFlagEnum 
* @Description: 删除标记枚举类
*
*/
public enum DelFlagEnum implements BaseIntEnum {

	UNDEL {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "未删除";
		}
	},
	DEL {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "已删除";
		}
	}
}
