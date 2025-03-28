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

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.AbsoluteContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.CellCoordinateConditionContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.CellCoordinateContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.CellIndicatorContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.CellPairContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.ConditionsContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.CoordinateContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.RangeContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.RelativeContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.SetContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.SimpleDataContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.SimpleValueContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.SingleCellConditionContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.SingleCellContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.SingleCellCoordinateContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.UnitContext;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser.WholeCellContext;
import org.vivi.framework.ureport.simple.ureport.exception.ReportParseException;
import org.vivi.framework.ureport.simple.ureport.expression.model.condition.BaseCondition;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.BaseExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.set.CellConditionExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.set.CellCoordinate;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.set.CellCoordinateExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.set.CellCoordinateSet;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.set.CellExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.set.CellPairExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.set.CoordinateType;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.set.FromToExpression;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.set.WholeCellExpression;

/**
 * @author Jacky.gao
 * @since 2016年12月25日
 */
public class SetExpressionBuilder extends BaseExpressionBuilder{
	@Override
	public BaseExpression build(UnitContext unitContext) {
		SetContext context=unitContext.set();
		BaseExpression setExpr=buildSetExpression(context);
		setExpr.setExpr(context.getText());
		return setExpr;
	}
	public BaseExpression buildSetExpression(SetContext context) {
		if(context instanceof SingleCellContext){
			TerminalNode cellNode=((SingleCellContext)context).Cell();
			return new CellExpression(cellNode.getText());
		}else if(context instanceof WholeCellContext){
			WholeCellContext ctx=(WholeCellContext)context;
			WholeCellExpression wholeCellExpression=new WholeCellExpression(ctx.Cell().getText());
			ConditionsContext conditionsContext=ctx.conditions();
			if(conditionsContext!=null){
				BaseCondition condition = buildConditions(conditionsContext);
				wholeCellExpression.setCondition(condition);
			}
			return wholeCellExpression;
		}else if(context instanceof SingleCellConditionContext){
			SingleCellConditionContext ctx=(SingleCellConditionContext)context;
			BaseCondition condition = buildConditions(ctx.conditions());
			return new CellConditionExpression(ctx.Cell().getText(),condition);
		}else if(context instanceof CellPairContext){
			CellPairContext pairContext=(CellPairContext)context;
			String startCellName=pairContext.Cell(0).getText();
			String endCellName=pairContext.Cell(1).getText();
			return new CellPairExpression(startCellName, endCellName);
		}else if(context instanceof SingleCellCoordinateContext){
			SingleCellCoordinateContext ctx=(SingleCellCoordinateContext)context;
			String cellName=null;
			if(ctx.Cell()!=null){
				cellName=ctx.Cell().getText();
			}
			CellCoordinateContext cellCoordinateContext=ctx.cellCoordinate();
			List<CoordinateContext> coordinateContexts=cellCoordinateContext.coordinate();
			CellCoordinateSet leftCoordinate=parseCellCoordinateSet(coordinateContexts.get(0));
			CellCoordinateSet rightCoordinate=null;
			if(coordinateContexts.size()>1){
				rightCoordinate=parseCellCoordinateSet(coordinateContexts.get(1));
			}
			return new CellCoordinateExpression(cellName,leftCoordinate,rightCoordinate);
		}else if(context instanceof CellCoordinateConditionContext){
			CellCoordinateConditionContext ctx=(CellCoordinateConditionContext)context;
			String cellName=null;
			if(ctx.Cell()!=null){
				cellName=ctx.Cell().getText();
			}
			CellCoordinateContext cellCoordinateContext=ctx.cellCoordinate();
			List<CoordinateContext> coordinateContexts=cellCoordinateContext.coordinate();
			CellCoordinateSet leftCoordinate=parseCellCoordinateSet(coordinateContexts.get(0));
			CellCoordinateSet rightCoordinate=null;
			if(coordinateContexts.size()>1){
				rightCoordinate=parseCellCoordinateSet(coordinateContexts.get(1));
			}
			BaseCondition condition = buildConditions(ctx.conditions());
			return new CellCoordinateExpression(cellName,leftCoordinate,rightCoordinate,condition);
		}else if(context instanceof RangeContext){
			RangeContext ctx=(RangeContext)context;
			List<SetContext> sets=ctx.set();
			if(sets.size()!=2){
				throw new ReportParseException("Range expression must have from and to expressions.");
			}
			BaseExpression fromExpr=buildSetExpression(sets.get(0));
			BaseExpression toExpr=buildSetExpression(sets.get(1));
			FromToExpression expr=new FromToExpression(fromExpr,toExpr);
			return expr;
		}else if(context instanceof SimpleDataContext){
			SimpleDataContext ctx=(SimpleDataContext)context;
			SimpleValueContext valueContext=ctx.simpleValue();
			return parseSimpleValueContext(valueContext);
		}
		throw new ReportParseException("Unknow context : "+context);
	}
	
	private CellCoordinateSet parseCellCoordinateSet(CoordinateContext ctx){
		List<CellCoordinate> coordinates=new ArrayList<CellCoordinate>();
		for(CellIndicatorContext indicatorContext:ctx.cellIndicator()){
			if(indicatorContext instanceof RelativeContext){
				RelativeContext context=(RelativeContext)indicatorContext;
				String cellName=context.Cell().getText();
				CellCoordinate coordinate=new CellCoordinate(cellName,CoordinateType.relative);
				coordinates.add(coordinate);
			}else{
				AbsoluteContext context=(AbsoluteContext)indicatorContext;
				String cellName=context.Cell().getText();
				String pos=context.INTEGER().getText();
				int position=Integer.valueOf(pos);
				CellCoordinate coordinate=new CellCoordinate(cellName,CoordinateType.absolute);
				coordinate.setPosition(position);
				if(context.EXCLAMATION()!=null){
					coordinate.setReverse(true);
				}
				coordinates.add(coordinate);
			}
		}
		return new CellCoordinateSet(coordinates);
	}

	@Override
	public boolean support(UnitContext unitContext) {
		return unitContext.set()!=null;
	}
}
