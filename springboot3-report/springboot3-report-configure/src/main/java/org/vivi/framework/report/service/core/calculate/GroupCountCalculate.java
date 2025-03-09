package org.vivi.framework.report.service.core.calculate;

import org.vivi.framework.report.service.web.dto.reporttpl.GroupSummaryData;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**  
 * @ClassName: GroupAddCalculate
 * @Description: 分组加法计算
*/
public class GroupCountCalculate extends Calculate<GroupSummaryData>{

	@Override
	public String calculate(GroupSummaryData bindData) {
		BigDecimal result = new BigDecimal(bindData.getDatas().size());
		result = result.setScale(bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}

}
