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
package org.vivi.framework.ureport.simple.ureport.expression.model.expr.ifelse;

import java.util.List;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.BaseExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.ExpressionBlock;
import org.vivi.framework.ureport.simple.ureport.model.Cell;

/**
 * @author Jacky.gao
 * @since 2017年1月16日
 */
public class IfExpression extends BaseExpression {
	
	private static final long serialVersionUID = -514395376408127087L;
	
	private ExpressionConditionList conditionList;
	
	private ExpressionBlock expression;
	
	private List<ElseIfExpression> elseIfExpressions;
	
	private ElseExpression elseExpression;
	
	@Override
	protected ExpressionData<?> compute(Cell cell,Cell currentCell, Context context) {
		if(conditionList!=null){
			boolean result=conditionList.eval(context, cell,currentCell);
			if(result){
				return expression.execute(cell, currentCell,context);
			}
		}
		if(elseIfExpressions!=null){				
			for(ElseIfExpression elseIfExpr:elseIfExpressions){
				if(elseIfExpr.conditionsEval(cell, currentCell,context)){
					return elseIfExpr.execute(cell,currentCell, context);
				}
			}
		}
		if(elseExpression!=null){
			return elseExpression.execute(cell,currentCell, context);
		}
		return new ObjectExpressionData(null);
	}
	public void setConditionList(ExpressionConditionList conditionList) {
		this.conditionList = conditionList;
	}
	public ExpressionConditionList getConditionList() {
		return conditionList;
	}
	public void setElseExpression(ElseExpression elseExpression) {
		this.elseExpression = elseExpression;
	}
	public void setElseIfExpressions(List<ElseIfExpression> elseIfExpressions) {
		this.elseIfExpressions = elseIfExpressions;
	}
	public ExpressionBlock getExpression() {
		return expression;
	}
	public void setExpression(ExpressionBlock expression) {
		this.expression = expression;
	}
	public ElseExpression getElseExpression() {
		return elseExpression;
	}
	public List<ElseIfExpression> getElseIfExpressions() {
		return elseIfExpressions;
	}
	@Override
	public void fetchCellName(List<String> list) {
		if(conditionList != null) {
			conditionList.fetchCellName(list);
		}
		if(expression != null) {
			expression.fetchCellName(list);
		}
		if(elseIfExpressions != null && elseIfExpressions.size() > 0) {
			for (ElseIfExpression elseIfExpression : elseIfExpressions) {
				elseIfExpression.fetchCellName(list);
			}
		}
		if(elseExpression != null) {
			elseExpression.fetchCellName(list);
		}
	}
}
