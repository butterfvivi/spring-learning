package org.vivi.framework.report.service.core.calculate;

import com.googlecode.aviator.AviatorEvaluator;
import org.vivi.framework.report.service.common.utils.CheckUtil;
import org.vivi.framework.report.service.common.utils.ListUtil;
import org.vivi.framework.report.service.common.utils.StringUtil;
import org.vivi.framework.report.service.web.dto.reporttpl.GroupSummaryData;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

/**  
 * @ClassName: GroupAddCalculate
 * @Description: 分组加法计算
*/
public class GroupAddCalculate extends Calculate<GroupSummaryData>{

	@Override
	public String calculate(GroupSummaryData bindData) {
		BigDecimal result = new BigDecimal(0);
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
				result = result.add(new BigDecimal(String.valueOf(object)));
			}
		}
		result = result.setScale(bindData.getDigit()==null?2:bindData.getDigit(), RoundingMode.HALF_UP);
		return String.valueOf(result);
	}

}
