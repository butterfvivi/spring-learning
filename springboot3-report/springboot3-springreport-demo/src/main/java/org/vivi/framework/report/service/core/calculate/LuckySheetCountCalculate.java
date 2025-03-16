package org.vivi.framework.report.service.core.calculate;


import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetBindData;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
* <p>Title: LuckySheetCountCalculate</p>
* <p>Description: 计数</p>
*/
public class LuckySheetCountCalculate extends Calculate<LuckySheetBindData>{

	@Override
	public String calculate(LuckySheetBindData bindData) {
		int size = 0;
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			size = size + bindData.getDatas().get(i).size();
		}
		BigDecimal result = new BigDecimal(size);
		result = result.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}

}
