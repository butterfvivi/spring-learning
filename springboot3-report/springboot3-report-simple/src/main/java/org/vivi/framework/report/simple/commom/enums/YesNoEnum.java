/*   
 * Copyright (c) 2016-2020 canaanQd. All Rights Reserved.   
 *   
 * This software is the confidential and proprietary information of   
 * canaanQd. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with canaanQd.   
 *   
 */ 

package org.vivi.framework.report.simple.commom.enums;


import org.vivi.framework.report.simple.commom.enums.base.BaseIntEnum;

/**
* @ClassName: YesNoEnum 
* @Description: 是否枚举类
*
*/
public enum YesNoEnum implements BaseIntEnum {

	YES {
		public Integer getCode() {
			return 1;
		}

		public String getName() {
			return "是";
		}
	},
	NO {
		public Integer getCode() {
			return 2;
		}

		public String getName() {
			return "否";
		}
	}
}
