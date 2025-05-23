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
package org.vivi.framework.ureport.simple.ureport.expression.function.page;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectListExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * @author Jacky.gao
 * @since 2017年5月5日
 */
public class PageSumFunction extends PageFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		if (dataList == null) {
			return 0;
		}
		BigDecimal total = new BigDecimal(0);
		for (ExpressionData<?> exprData : dataList) {
			if (exprData instanceof ObjectListExpressionData) {
				ObjectListExpressionData listExpr = (ObjectListExpressionData) exprData;
				List<?> list = listExpr.getData();
				for (Object obj : list) {
					if (obj == null || StringUtils.isBlank(obj.toString())) {
						continue;
					}
					BigDecimal bigData = ToolUtils.toBigDecimal(obj);
					total = total.add(bigData);
				}
			} else if (exprData instanceof ObjectExpressionData) {
				Object obj = exprData.getData();
				if (obj != null && StringUtils.isNotBlank(obj.toString())) {
					BigDecimal bigData = ToolUtils.toBigDecimal(obj);
					total = total.add(bigData);
				}
			}
		}
		return total;
	}

	@Override
	public String name() {
		return "psum";
	}
}
