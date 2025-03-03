/*   
 * Copyright (c) 2016-2020 canaanQd. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * canaanQd. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with canaanQd.   
 *   
 */ 

package org.vivi.framework.report.service.common.enums;


import org.vivi.framework.report.service.common.enums.base.BaseCharEnum;

/**
 * @ClassName: InParamTypeEnum
 * @Description: 输出参数类型
*/
public enum OutParamTypeEnum implements BaseCharEnum {

	VARCHAR {
		public String getCode() {
			return "VARCHAR";
		}

		public String getName() {
			return "VARCHAR";
		}
	},
	INTEGER {
		public String getCode() {
			return "INTEGER";
		}

		public String getName() {
			return "INTEGER";
		}
	},
	BIGINT {
		public String getCode() {
			return "BIGINT";
		}

		public String getName() {
			return "BIGINT";
		}
	},
	FLOAT {
		public String getCode() {
			return "FLOAT";
		}

		public String getName() {
			return "FLOAT";
		}
	},
	DOUBLE {
		public String getCode() {
			return "DOUBLE";
		}

		public String getName() {
			return "DOUBLE";
		}
	},
	DECIMAL {
		public String getCode() {
			return "DECIMAL";
		}

		public String getName() {
			return "DECIMAL";
		}
	},
}
