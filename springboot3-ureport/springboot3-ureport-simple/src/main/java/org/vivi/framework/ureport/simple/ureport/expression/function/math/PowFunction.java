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
package org.vivi.framework.ureport.simple.ureport.expression.function.math;

import java.math.BigDecimal;
import java.util.List;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.exception.ReportComputeException;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * @author Jacky.gao
 * @since 2017年1月23日
 */
public class PowFunction extends MathFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		BigDecimal data = buildBigDecimal(dataList);
		int pos = 0;
		if (dataList.size() == 2) {
			ExpressionData<?> exprData = dataList.get(1);
			if (exprData instanceof ObjectExpressionData) {
				ObjectExpressionData objData = (ObjectExpressionData) exprData;
				Object obj = objData.getData();
				if (obj == null) {
					throw new ReportComputeException("Pow Function second parameter can not be null.");
				}
				pos = ToolUtils.toBigDecimal(obj).intValue();
			}
		}
		return Math.pow(data.doubleValue(), pos);
	}

	@Override
	public String name() {
		return "pow";
	}
}
