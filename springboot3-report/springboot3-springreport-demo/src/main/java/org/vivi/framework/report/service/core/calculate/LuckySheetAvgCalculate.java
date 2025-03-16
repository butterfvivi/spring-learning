package org.vivi.framework.report.service.core.calculate;


import org.vivi.framework.report.service.common.utils.CheckUtil;
import org.vivi.framework.report.service.common.utils.ListUtil;
import org.vivi.framework.report.service.web.dto.reporttpl.LuckySheetBindData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* <p>Title: AvgCalculate</p>
* <p>Description: 平均值计算</p>
*/
public class LuckySheetAvgCalculate extends Calculate<LuckySheetBindData>{

	@Override
	public String calculate(LuckySheetBindData bindData) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal sum = new BigDecimal(0);
		int size = 0;
		String[] datasetNames = bindData.getDatasetName().split(",");
		if(datasetNames.length > 1)
		{
			for (int i = 0; i < datasetNames.length; i++) {
				List<List<Map<String, Object>>> datas = bindData.getMultiDatas().get(datasetNames[i]);
				List<String> properties = ListUtil.getPropertyList(bindData.getProperty(), datas.get(0).get(0), datasetNames[i]);
				for (int j = 0; j < properties.size(); j++) {
					int temp = 0;
					for (int j2 = 0; j2 < bindData.getDatas().size(); j2++) {
						for (int k = 0; k < bindData.getDatas().get(j2).size(); k++) {
							Object object = bindData.getDatas().get(j2).get(k).get(properties.get(j));
							if(CheckUtil.isNumber(String.valueOf(object)))
							{
								sum = sum.add(new BigDecimal(String.valueOf(object)));
							}
							temp = temp + 1;
						}
					}
					if(temp > size)
					{
						size = temp;
					}
				}
			}
		}else {
			String property = bindData.getProperty();
			for (int i = 0; i < bindData.getDatas().size(); i++) {
				for (int j = 0; j < bindData.getDatas().get(i).size(); j++) {
					Object object = bindData.getDatas().get(i).get(j).get(property);
					if(CheckUtil.isNumber(String.valueOf(object)))
					{
						sum = sum.add(new BigDecimal(String.valueOf(object)));
					}
					size = size + 1;
				}
			}
		}
		result = sum.divide(new BigDecimal(size),bindData.getDigit(),BigDecimal.ROUND_HALF_UP);
		return String.valueOf(result);
	}

}
