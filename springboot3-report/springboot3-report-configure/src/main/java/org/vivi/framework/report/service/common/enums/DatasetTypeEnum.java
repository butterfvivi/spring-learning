package org.vivi.framework.report.service.common.enums;

import org.vivi.framework.report.service.common.enums.base.BaseIntEnum;

/**
 * @ClassName: DatasetTypeEnum
 * @Description: 数据集类型
*/
public enum DatasetTypeEnum implements BaseIntEnum {

	SQL {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "sql语句";
		}
	},
	API {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "接口";
		}
	}
}
