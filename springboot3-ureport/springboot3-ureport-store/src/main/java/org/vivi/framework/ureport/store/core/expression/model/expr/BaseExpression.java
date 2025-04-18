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
package org.vivi.framework.ureport.store.core.expression.model.expr;

import org.vivi.framework.ureport.store.core.build.Context;
import org.vivi.framework.ureport.store.core.expression.model.Condition;
import org.vivi.framework.ureport.store.core.expression.model.Expression;
import org.vivi.framework.ureport.store.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.store.core.model.Cell;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseExpression implements Expression {
	private static final long serialVersionUID = 3853234856946931008L;
	protected String expr;
	
	@Override
	public final ExpressionData<?> execute(Cell cell, Cell currentCell, Context context) {
		ExpressionData<?>  data=compute(cell,currentCell,context);
		return data;
	}
	
	protected abstract ExpressionData<?> compute(Cell cell,Cell currentCell, Context context);
	
	protected List<Cell> filterCells(Cell cell, Context context, Condition condition, List<Cell> targetCells) {
		if(condition==null){
			return targetCells;
		}
		List<Cell> list=new ArrayList<Cell>();
		for(Cell targetCell:targetCells){
			boolean conditionResult=true;
			List<Object> dataList=targetCell.getBindData();
			if(dataList==null){
				conditionResult=false;				
			}else{
				for(Object obj:dataList){
					boolean result=condition.filter(cell,targetCell, obj, context);
					if(!result){
						conditionResult=false;
						break;
					}
				}
			}
			if(!conditionResult){
				continue;
			}
			list.add(targetCell);
		}
		return list;
	}
	
	public void setExpr(String expr) {
		this.expr = expr;
	}
	public String getExpr() {
		return expr;
	}
}
