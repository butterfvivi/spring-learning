package org.vivi.framework.report.service.common.enums;

import org.vivi.framework.report.service.common.enums.base.BaseCharEnum;

/**
 * @ClassName: SqlOrderEnum
 * @Description: sql排序用枚举类
*/
public enum SqlOrderEnum implements BaseCharEnum {

	ASC {
		public String getCode() {
			return "ASC";
		}

		public String getName() {
			return "正序";
		}
	},
	DESC {
		public String getCode() {
			return "DESC";
		}

		public String getName() {
			return "倒序";
		}
	}
}
