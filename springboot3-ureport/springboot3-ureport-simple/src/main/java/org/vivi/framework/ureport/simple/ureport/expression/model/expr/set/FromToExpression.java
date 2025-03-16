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
package org.vivi.framework.ureport.simple.ureport.expression.model.expr.set;

import java.util.ArrayList;
import java.util.List;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.exception.ReportComputeException;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectListExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.BaseExpression;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * @author Jacky.gao
 * @since 2017年1月1日
 */
public class FromToExpression extends BaseExpression {
	
	private static final long serialVersionUID = -3250140935488901894L;
	
	private BaseExpression fromExpression;
	
	private BaseExpression toExpression;
	
	public FromToExpression(BaseExpression fromExpression,BaseExpression toExpression) {
		this.fromExpression = fromExpression;
		this.toExpression = toExpression;
	}

	@Override
	protected ExpressionData<?> compute(Cell cell,Cell currentCell, Context context) {
		Object fromData=fromExpression.execute(cell,currentCell, context);
		Object toData=toExpression.execute(cell,currentCell, context);
		int from=convertFloatData(fromData),to=convertFloatData(toData);
		List<Integer> list=new ArrayList<Integer>();
		for(int i=from;i<=to;i++){
			list.add(i);
		}
		return new ObjectListExpressionData(list);
	}
	
	private int convertFloatData(Object data){
		if(data instanceof ObjectExpressionData){
			Object obj=((ObjectExpressionData)data).getData();
			return ToolUtils.toBigDecimal(obj).intValue();
		}else{
			throw new ReportComputeException("Can not convert ["+data+"] to integer.");
		}
	}
	
	@Override
	public void fetchCellName(List<String> list) {
		if(fromExpression != null) {
			fromExpression.fetchCellName(list);
		}
		if(toExpression != null) {
			toExpression.fetchCellName(list);
		}
	}
}
