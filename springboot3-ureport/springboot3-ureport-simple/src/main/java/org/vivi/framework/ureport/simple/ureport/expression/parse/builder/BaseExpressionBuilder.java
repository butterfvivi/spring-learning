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
package org.vivi.framework.ureport.simple.ureport.expression.parse.builder;

import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.CellNameExprConditionContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.ConditionContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.ConditionsContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.CurrentValueConditionContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.ExprConditionContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.ExprContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.JoinContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.PropertyConditionContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.SimpleValueContext;
import org.vivi.framework.ureport.simple.ureport.exception.ReportParseException;
import org.vivi.framework.ureport.simple.ureport.expression.ExpressionUtils;
import org.vivi.framework.ureport.simple.ureport.expression.model.Expression;
import org.vivi.framework.ureport.simple.ureport.expression.model.Op;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.BaseCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.BothExpressionCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.CellExpressionCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.CurrentValueExpressionCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.Join;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.PropertyExpressionCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.BaseExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.BooleanExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.IntegerExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.NullExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.NumberExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.StringExpression;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * @author Jacky.gao
 * @since 2016年12月26日
 */
public abstract class BaseExpressionBuilder implements ExpressionBuilder{
	protected BaseExpression parseSimpleValueContext(SimpleValueContext valueContext) {
		if(valueContext.BOOLEAN()!=null){
			return new BooleanExpression(Boolean.valueOf(valueContext.getText()));
		}else if(valueContext.INTEGER()!=null){
			return new IntegerExpression(Integer.valueOf(valueContext.INTEGER().getText()));
		}else if(valueContext.STRING()!=null){
			String text=valueContext.STRING().getText();
			text=text.substring(1,text.length()-1);
			return new StringExpression(text);
		}else if(valueContext.NUMBER()!=null){
			return new NumberExpression(ToolUtils.toBigDecimal(valueContext.NUMBER().getText()));
		}else if(valueContext.NULL()!=null){
			return new NullExpression();
		}
		throw new ReportParseException("Unknow simple value context "+valueContext);
	}

	
	protected BaseCondition buildConditions(ConditionsContext conditionsContext) {
		List<ConditionContext> conditionContextList=conditionsContext.condition();
		List<JoinContext> joins=conditionsContext.join();
		BaseCondition condition=null;
		BaseCondition topCondition=null;
		int opIndex=0;
		for(ConditionContext conditionCtx:conditionContextList){
			if(condition==null){
				condition=parseCondition(conditionCtx);
				topCondition=condition;
			}else{
				BaseCondition nextCondition=parseCondition(conditionCtx);
				condition.setNextCondition(nextCondition);
				condition.setJoin(Join.parse(joins.get(opIndex).getText()));
				opIndex++;
				condition=nextCondition;
			}
		}
		return topCondition;
	}
	private BaseCondition parseCondition(ConditionContext context){
		if(context instanceof ExprConditionContext){
			ExprConditionContext ctx=(ExprConditionContext)context;
			BothExpressionCondition condition=new BothExpressionCondition();
			List<ExprContext> exprContexts=ctx.expr();
			String left=exprContexts.get(0).getText();
			condition.setLeft(left);
			Expression leftExpr=ExpressionUtils.parseExpression(left);
			condition.setLeftExpression(leftExpr);
			String rightExpr=exprContexts.get(1).getText();
			condition.setRight(rightExpr);
			condition.setRightExpression(ExpressionUtils.parseExpression(rightExpr));
			condition.setOp(parseOp(ctx.OP()));
			condition.setOperation(ctx.OP().getText());
			return condition;
		}else if(context instanceof CurrentValueConditionContext){
			CurrentValueConditionContext ctx=(CurrentValueConditionContext)context;
			CurrentValueExpressionCondition condition=new CurrentValueExpressionCondition();
			String rightExpr=ctx.expr().getText();
			condition.setRight(rightExpr);
			condition.setRightExpression(ExpressionUtils.parseExpression(rightExpr));
			condition.setOp(parseOp(ctx.OP()));
			return condition;
		}else if(context instanceof PropertyConditionContext){
			PropertyConditionContext ctx=(PropertyConditionContext)context;
			PropertyExpressionCondition condition=new PropertyExpressionCondition();
			String left=ctx.property().getText();
			condition.setLeft(left);
			condition.setLeftProperty(left);
			String rightExpr=ctx.expr().getText();
			condition.setRight(rightExpr);
			condition.setRightExpression(ExpressionUtils.parseExpression(rightExpr));
			condition.setOp(parseOp(ctx.OP()));
			return condition;
		}else if(context instanceof CellNameExprConditionContext){
			CellNameExprConditionContext ctx=(CellNameExprConditionContext)context;
			CellExpressionCondition condition=new CellExpressionCondition();
			String left=ctx.Cell().getText();
			condition.setLeft(left);
			condition.setCellName(left);
			String rightExpr=ctx.expr().getText();
			condition.setRight(rightExpr);
			condition.setRightExpression(ExpressionUtils.parseExpression(rightExpr));
			condition.setOp(parseOp(ctx.OP()));
			return condition;
		}
		throw new ReportParseException("Unknow condition context : "+context);
	}
	
	private Op parseOp(TerminalNode opNode){
		if(opNode.getText().equals(">")){
			return Op.GreatThen;
		}
		if(opNode.getText().equals("<")){
			return Op.LessThen;
		}
		if(opNode.getText().equals(">=")){
			return Op.EqualsGreatThen;
		}
		if(opNode.getText().equals("<=")){
			return Op.EqualsLessThen;
		}
		if(opNode.getText().equals("==")){
			return Op.Equals;
		}
		if(opNode.getText().equals("!=")){
			return Op.NotEquals;
		}
		throw new ReportParseException("Unknow operator :" +opNode);
	}
}
