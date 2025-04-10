/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vivi.framework.ureport.simple.ureport.expression.function;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.vivi.framework.ureport.simple.ureport.build.BindData;
import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.BindDataListExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectListExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * @author Jacky.gao
 * @since 2017年1月20日
 */
public class AvgFunction implements Function {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		if (dataList == null || dataList.size() == 0) {
			return null;
		}
		int size = 0;
		Object singleData = null;
		BigDecimal total = new BigDecimal(0);
		for (ExpressionData<?> exprData : dataList) {
			if (exprData instanceof ObjectListExpressionData) {
				ObjectListExpressionData listExpr = (ObjectListExpressionData) exprData;
				List<?> list = listExpr.getData();
				for (Object obj : list) {
					if (obj == null || StringUtils.isBlank(obj.toString())) {
						continue;
					}
					singleData = obj;
					BigDecimal bigData = ToolUtils.toBigDecimal(obj);
					total = total.add(bigData);
					size++;
				}
			} else if (exprData instanceof ObjectExpressionData) {
				ObjectExpressionData data = (ObjectExpressionData) exprData;
				Object obj = data.getData();
				if (obj == null || StringUtils.isBlank(obj.toString())) {
					continue;
				}
				BigDecimal bigData = ToolUtils.toBigDecimal(data.getData());
				singleData = data.getData();
				total = total.add(bigData);
				size++;
			} else if (exprData instanceof BindDataListExpressionData) {
				BindDataListExpressionData data = (BindDataListExpressionData) exprData;
				List<BindData> bindDataList = data.getData();
				for (BindData bindData : bindDataList) {
					Object obj = bindData.getValue();
					if (obj == null || StringUtils.isBlank(obj.toString())) {
						continue;
					}
					singleData = obj;
					BigDecimal bigData = ToolUtils.toBigDecimal(obj);
					total = total.add(bigData);
					size++;
				}
			}
		}
		if (size == 0) {
			return 0;
		} else if (size == 1) {
			if (singleData == null || singleData.equals("")) {
				return "";
			}
			return total;
		} else {
			return total.divide(new BigDecimal(size), 8, BigDecimal.ROUND_HALF_UP);
		}
	}

	@Override
	public String name() {
		return "avg";
	}
}
