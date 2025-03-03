package org.vivi.framework.report.service.common.enums;

import org.vivi.framework.report.service.common.enums.base.BaseCharEnum;

/**  
 * @ClassName: OperatorEnum
 * @Description: 操作符枚举类
*/
public enum OperatorEnum implements BaseCharEnum{

	EQ {
		public String getCode() {
			return "=";
		}

		public String getName() {
			return "等于";
		}
	},
	NE {
		public String getCode() {
			return "!=";
		}

		public String getName() {
			return "不等于";
		}
	},
	GT {
		public String getCode() {
			return ">";
		}

		public String getName() {
			return "大于";
		}
	},
	GE {
		public String getCode() {
			return ">=";
		}

		public String getName() {
			return "大于等于";
		}
	},
	LT {
		public String getCode() {
			return "<";
		}

		public String getName() {
			return "小于";
		}
	},
	LE {
		public String getCode() {
			return "<=";
		}

		public String getName() {
			return "小于等于";
		}
	},
	IN {
		public String getCode() {
			return "in";
		}

		public String getName() {
			return "包含";
		}
	},
	NOTIN {
		public String getCode() {
			return "not in";
		}

		public String getName() {
			return "不包含";
		}
	},
}
