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
package org.vivi.framework.ureport.store.core.build.compute;

import org.vivi.framework.ureport.store.core.build.BindData;
import org.vivi.framework.ureport.store.core.build.Context;
import org.vivi.framework.ureport.store.core.definition.value.ExpressionValue;
import org.vivi.framework.ureport.store.core.definition.value.ValueType;
import org.vivi.framework.ureport.store.core.exception.ReportComputeException;
import org.vivi.framework.ureport.store.core.expression.ExpressionUtils;
import org.vivi.framework.ureport.store.core.expression.function.Function;
import org.vivi.framework.ureport.store.core.expression.function.page.PageFunction;
import org.vivi.framework.ureport.store.core.expression.model.Expression;
import org.vivi.framework.ureport.store.core.expression.model.data.BindDataListExpressionData;
import org.vivi.framework.ureport.store.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.store.core.expression.model.expr.*;
import org.vivi.framework.ureport.store.core.expression.model.expr.ifelse.*;
import org.vivi.framework.ureport.store.core.model.Cell;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jacky.gao
 * @since 2016年12月27日
 */
public class ExpressionValueCompute implements ValueCompute {
	@Override
	public List<BindData> compute(Cell cell, Context context) {
		ExpressionValue exprValue=(ExpressionValue)cell.getValue();
		Expression expr=exprValue.getExpression();
		List<BindData> list=new ArrayList<BindData>();
		if(!context.isDoPaging()){
			boolean hasPageFun=hasPageFunction(expr);
			if(hasPageFun){
				cell.setExistPageFunction(true);
				context.addExistPageFunctionCells(cell);
				return list;
			}			
		}
		ExpressionData<?> data=expr.execute(cell, cell,context);
		if(data instanceof BindDataListExpressionData){
			BindDataListExpressionData exprData=(BindDataListExpressionData)data;
			return exprData.getData();
		}
		Object obj=data.getData();
		if(obj instanceof List){
			List<?> listData=(List<?>)obj;
			for(Object o:listData){
				list.add(new BindData(o));
			}
		}else{
			if(obj!=null){
				list.add(new BindData(obj));							
			}
		}
		return list;
	}
	
	private boolean hasPageFunction(Expression expr){
		if(expr==null){
			return false;
		}
		if(expr instanceof IfExpression){
			boolean has=false;
			IfExpression ifExpr=(IfExpression)expr;
			ExpressionConditionList exprConditionList=ifExpr.getConditionList();
			if(exprConditionList!=null){
				List<ExpressionCondition> conditionList=exprConditionList.getConditions();
				if(conditionList!=null){
					for(ExpressionCondition exprCondition:conditionList){
						Expression leftExpression=exprCondition.getLeft();
						has=hasPageFunction(leftExpression);
						if(has){
							return has;
						}
						Expression rightExpression=exprCondition.getRight();
						has=hasPageFunction(rightExpression);
						if(has){
							return has;
						}
					}
				}
			}
			has=hasPageFunction(ifExpr.getExpression());
			if(has){
				return has;
			}
			ElseExpression elseExpr=ifExpr.getElseExpression();
			if(elseExpr!=null){
				has=hasPageFunction(elseExpr.getExpression());
				if(has){
					return has;
				}
			}
			List<ElseIfExpression> elseIfList=ifExpr.getElseIfExpressions();
			if(elseIfList==null || elseIfList.size()==0){
				return false;
			}
			for(ElseIfExpression elseIfExpr:elseIfList){
				has=hasPageFunction(elseIfExpr.getExpression());
				if(has){
					return has;
				}
			}
		}else if(expr instanceof JoinExpression){
			JoinExpression joinExpr=(JoinExpression)expr;
			List<BaseExpression> list=joinExpr.getExpressions();
			if(list==null || list.size()==0){
				return false;
			}
			for(BaseExpression baseExpr:list){
				boolean has=hasPageFunction(baseExpr);
				if(has){
					return has;
				}
			}
		}else if(expr instanceof FunctionExpression){
			FunctionExpression funExpr=(FunctionExpression)expr;
			String name=funExpr.getName();
			Function fun= ExpressionUtils.getFunctions().get(name);
			if(fun==null){
				throw new ReportComputeException("Function ["+name+"] not exist.");
			}
			if(fun instanceof PageFunction){
				return true;
			}
			List<BaseExpression> list=funExpr.getExpressions();
			if(list==null || list.size()==0){
				return false;
			}
			for(BaseExpression baseExpr:list){
				boolean has=hasPageFunction(baseExpr);
				if(has){
					return has;
				}
			}
		} else if (expr instanceof ExpressionBlock) {
			FunctionExpression funExpr = null;

			try {
				List<Expression> expressionList = ((ExpressionBlock) expr).getExpressionList();
				if (!CollectionUtils.isEmpty(expressionList)) {
					ParenExpression expression = (ParenExpression) expressionList.get(0);
					List<BaseExpression> expressions = expression.getExpressions();
					if (!CollectionUtils.isEmpty(expressions) && expressions.get(0) instanceof FunctionExpression) {
						funExpr = (FunctionExpression) expressions.get(0);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (funExpr == null) {
				return false;
			}
			String name = funExpr.getName();
			Function fun = ExpressionUtils.getFunctions().get(name);
			if (fun == null) {
				throw new RuntimeException(name + "函数不支持");
			}
			if (fun instanceof PageFunction) {
				return true;
			}
			List<BaseExpression> list = funExpr.getExpressions();
			if (list == null || list.size() == 0) {
				return false;
			}
			for (BaseExpression baseExpr : list) {
				boolean has = hasPageFunction(baseExpr);
				if (has) {
					return has;
				}
			}
		}
		return false;
	}
	
	@Override
	public ValueType type() {
		return ValueType.expression;
	}
}
