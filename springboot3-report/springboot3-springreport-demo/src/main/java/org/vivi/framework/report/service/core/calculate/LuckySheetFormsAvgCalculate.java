package org.vivi.framework.report.service.core.calculate;


import org.vivi.framework.report.service.common.utils.CheckUtil;
import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetFormsBindData;

import java.math.BigDecimal;

/**
* <p>Title: AvgCalculate</p>
* <p>Description: 平均值计算</p>
*/
public class LuckySheetFormsAvgCalculate extends Calculate<LuckySheetFormsBindData>{

	@Override
	public String calculate(LuckySheetFormsBindData bindData) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal sum = new BigDecimal(0);
		int size = 0;
		String property = bindData.getProperty();
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			for (int j = 0; j < bindData.getDatas().get(i).size(); j++) {
				Object object = bindData.getDatas().get(i).get(j).get(property);
				if(CheckUtil.isNumber(String.valueOf(object)))
				{
					sum = sum.add(new BigDecimal(String.valueOf(object)));
					size = size + 1;
				}
			}
		}
		result = sum.divide(new BigDecimal(size),bindData.getDigit(),BigDecimal.ROUND_HALF_UP);
		return String.valueOf(result);
	}

}
