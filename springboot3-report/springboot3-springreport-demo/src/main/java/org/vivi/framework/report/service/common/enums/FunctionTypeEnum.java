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


import org.vivi.framework.report.service.common.enums.base.BaseIntEnum;

public enum FunctionTypeEnum implements BaseIntEnum {

	SUM {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "求和";
		}
	},
	AVG {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "求平均值";
		}
	},
	MAX {
		public Integer getCode() {
			return 3;
		}

		public String getName() {
			return "求最大值";
		}
	},
	MIN {
		public Integer getCode() {
			return 4;
		}

		public String getName() {
			return "求最小值";
		}
	},
	COUNT {
		public Integer getCode() {
			return 5;
		}

		public String getName() {
			return "计数";
		}
	}
}
