package org.vivi.framework.report.service.common.enums;


import org.vivi.framework.report.service.common.enums.base.BaseIntEnum;

/**
 * @ClassName: SqlTypeEnum
 * @Description: sql类型枚举类
*/
public enum SqlTypeEnum implements BaseIntEnum {

	SQL {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "sql语句";
		}
	},
	FUNCTION {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "函数或者存储过程";
		}
	}
}
