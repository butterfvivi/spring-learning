
package org.vivi.framework.report.service.common.enums;

import org.vivi.framework.report.service.common.enums.base.BaseIntEnum;
/**
* @ClassName: DataFromEnum 
* @Description: 数据来源枚举类
*
*/
public enum DataFromEnum implements BaseIntEnum{

	DEFAULT {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "默认";
		}
	},
	ORIGINAL {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "原始数据";
		}
	},
	CELL {
		public Integer getCode() {
			return 3;
		}

		public String getName() {
			return "单元格";
		}
	}
}
