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
 * @since 2016年12月27日
 */
public class MaxFunction implements Function {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context,Cell currentCell) {
		if(dataList == null || dataList.size() == 0) {
			return null;
		}
		BigDecimal value = null;
		for(ExpressionData<?> exprData:dataList) {
			if(exprData instanceof ObjectListExpressionData) {
				ObjectListExpressionData listExpr = (ObjectListExpressionData)exprData;
				List<?> list = listExpr.getData();
				for(Object obj:list) {
					if(obj == null || StringUtils.isBlank(obj.toString())) {
						continue;
					}
					BigDecimal bigData = ToolUtils.toBigDecimal(obj);
					if(value == null) {
						value = bigData;
					} else {
						int result = value.compareTo(bigData);
						if(result == -1) {
							value = bigData;
						}						
					}
				}
			} else if(exprData instanceof ObjectExpressionData) {
				Object obj = exprData.getData();
				if(obj != null && StringUtils.isNotBlank(obj.toString())) {
					value = ToolUtils.toBigDecimal(obj);
				}
			} else if(exprData instanceof BindDataListExpressionData) {
				BindDataListExpressionData bindDataList = (BindDataListExpressionData)exprData;
				List<BindData> list = bindDataList.getData();
				for(BindData bindData:list) {
					Object obj = bindData.getValue();
					if(obj == null || StringUtils.isBlank(obj.toString())) {
						continue;
					}
					BigDecimal bigData = ToolUtils.toBigDecimal(obj);
					if(value == null) {
						value=bigData;
					} else {
						int result = value.compareTo(bigData);
						if(result == -1) {
							value = bigData;
						}						
					}
				}
			}
		}
		return value;
	}

	@Override
	public String name() {
		return "max";
	}
}
