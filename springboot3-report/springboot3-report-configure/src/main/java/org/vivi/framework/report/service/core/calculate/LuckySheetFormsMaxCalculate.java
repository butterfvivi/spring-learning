package org.vivi.framework.report.service.core.calculate;


import org.vivi.framework.report.service.common.utils.CheckUtil;
import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetFormsBindData;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
* <p>Title: MaxCalculate</p>
* <p>Description: 求最大值</p>
* @author caiyang
* @date 2020年5月20日
*/
public class LuckySheetFormsMaxCalculate extends Calculate<LuckySheetFormsBindData>{

	@Override
	public String calculate(LuckySheetFormsBindData bindData) {
		BigDecimal result = new BigDecimal(0);
		String property = bindData.getProperty();
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			for (int j = 0; j < bindData.getDatas().get(i).size(); j++) {
				Object object = bindData.getDatas().get(i).get(j).get(property);
				if(CheckUtil.isNumber(String.valueOf(object)))
				{
					BigDecimal number = new BigDecimal(String.valueOf(object));
					if (number.compareTo(result)>0) {
						result = number;
					}
				}
			}
		}
		result = result.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}

}
