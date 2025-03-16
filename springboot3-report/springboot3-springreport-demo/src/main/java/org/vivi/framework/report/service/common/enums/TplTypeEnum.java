package org.vivi.framework.report.service.common.enums;


import org.vivi.framework.report.service.common.enums.base.BaseIntEnum;

/**
 * @ClassName: TplTypeEnum
 * @Description: 报表类型用枚举类
*/
public enum TplTypeEnum implements BaseIntEnum {
	
	LIST {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "列表式报表";
		}
	},
	FORMS {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "填报式报表";
		}
	}
}
