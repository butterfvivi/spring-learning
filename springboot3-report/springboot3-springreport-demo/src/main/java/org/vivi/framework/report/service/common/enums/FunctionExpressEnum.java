package org.vivi.framework.report.service.common.enums;


import org.vivi.framework.report.service.common.enums.base.BaseCharEnum;

public enum FunctionExpressEnum implements BaseCharEnum {
	NOW {
		public String getCode() {
			return "=NOW()";
		}

		public String getName() {
			return "当前时间函数";
		}
	},
}
