package org.vivi.framework.report.service.core.calculate;

import com.googlecode.aviator.AviatorEvaluator;
import org.vivi.framework.report.service.common.utils.CheckUtil;
import org.vivi.framework.report.service.common.utils.ListUtil;
import org.vivi.framework.report.service.common.utils.StringUtil;
import org.vivi.framework.report.service.web.dto.reporttpl.GroupSummaryData;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**  
 * @ClassName: GroupAvgCalculate
 * @Description: 分组平均数计算
*/
public class GroupAvgCalculate extends Calculate<GroupSummaryData>{

	@Override
	public String calculate(GroupSummaryData bindData) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal sum = new BigDecimal(0);
		int size = 0;
		for (int i = 0; i < bindData.getDatas().size(); i++) {
			String property = bindData.getProperty();
			if(StringUtil.isNullOrEmpty(property)) {
				break;
			}
			Object object = null;
			Map<String, Object> datas = ListUtil.getProperties(bindData.getProperty(), bindData.getDatas().get(i));
			Set<String> set = datas.keySet();
			if(ListUtil.isNotEmpty(set))
			{
				for (String o : set) {
		        	property = property.replace(o, datas.get(o)==null?"":String.valueOf(datas.get(o)));
		        }
				try {
					object = AviatorEvaluator.execute(property);
				} catch (Exception e) {
					object = 0;
				}
			}else {
				object = bindData.getDatas().get(i).get(property);
			}
			if(CheckUtil.isNumber(String.valueOf(object)))
			{
				sum = sum.add(new BigDecimal(String.valueOf(object)));
			}
			size = size + 1;
		}
		result = sum.divide(new BigDecimal(size),bindData.getDigit(),BigDecimal.ROUND_HALF_UP);
		return String.valueOf(result);
	}

}
