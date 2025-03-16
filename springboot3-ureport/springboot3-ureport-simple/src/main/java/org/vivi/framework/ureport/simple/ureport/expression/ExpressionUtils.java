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
package org.vivi.framework.ureport.simple.ureport.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import org.vivi.framework.ureport.simple.ureport.build.assertor.Assertor;
import org.vivi.framework.ureport.simple.ureport.build.assertor.EqualsAssertor;
import org.vivi.framework.ureport.simple.ureport.build.assertor.EqualsGreatThenAssertor;
import org.vivi.framework.ureport.simple.ureport.build.assertor.EqualsLessThenAssertor;
import org.vivi.framework.ureport.simple.ureport.build.assertor.GreatThenAssertor;
import org.vivi.framework.ureport.simple.ureport.build.assertor.InAssertor;
import org.vivi.framework.ureport.simple.ureport.build.assertor.LessThenAssertor;
import org.vivi.framework.ureport.simple.ureport.build.assertor.LikeAssertor;
import org.vivi.framework.ureport.simple.ureport.build.assertor.NotEqualsAssertor;
import org.vivi.framework.ureport.simple.ureport.build.assertor.NotInAssertor;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserLexer;
import org.vivi.framework.ureport.simple.ureport.dsl.ReportParserParser;
import org.vivi.framework.ureport.simple.ureport.exception.ReportParseException;
import org.vivi.framework.ureport.simple.ureport.expression.function.AvgFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.ColumnFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.CountFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.FormatFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.Function;
import org.vivi.framework.ureport.simple.ureport.expression.function.GetFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.JsonFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.ListFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.MaxFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.MinFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.OrderFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.ParameterFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.ParameterIsEmptyFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.RangeFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.RowFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.SumFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.date.DateAddFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.date.DayFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.date.MonthFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.date.ToDateFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.date.ToDayFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.date.WeekFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.date.YearFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.AbsFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.CeilFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.ChnFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.ChnMoneyFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.CosFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.ExpFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.FloorFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.Log10Function;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.LogFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.MedianFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.ModeFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.PowFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.RandomFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.RoundFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.SinFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.SqrtFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.StdevpFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.TanFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.math.VaraFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.page.PageAvgFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.page.PageCountFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.page.PageMaxFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.page.PageMinFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.page.PageNumberFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.page.PageRowsFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.page.PageSumFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.page.PageTotalFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.string.IndexOfFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.string.LengthFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.string.LowerFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.string.ReplaceFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.string.SubstringFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.string.TrimFunction;
import org.vivi.framework.ureport.simple.ureport.expression.function.string.UpperFunction;
import org.vivi.framework.ureport.simple.ureport.expression.model.Expression;
import org.vivi.framework.ureport.simple.ureport.expression.model.Op;
import org.vivi.framework.ureport.simple.ureport.expression.parse.ExpressionErrorListener;
import org.vivi.framework.ureport.simple.ureport.expression.parse.ExpressionVisitor;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.BooleanExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.CellObjectExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.CellPositionExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.CurrentCellDataExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.CurrentCellValueExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.DatasetExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.ExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.FunctionExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.IntegerExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.NullExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.NumberExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.RelativeCellExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.SetExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.StringExpressionBuilder;
import org.vivi.framework.ureport.simple.ureport.expression.parse.builder.VariableExpressionBuilder;

/**
 * @author Jacky.gao
 * @since 2016年12月24日
 */
public class ExpressionUtils {

	public static final String EXPR_PREFIX = "${";

	public static final String EXPR_SUFFIX = "}";

	private static ExpressionVisitor exprVisitor;

	private static Map<String, Function> functions = new HashMap<String, Function>();

	private static Map<Op, Assertor> assertorsMap = new HashMap<Op, Assertor>();

	private static List<ExpressionBuilder> expressionBuilders = new ArrayList<ExpressionBuilder>();

	private static List<String> cellNameList = new ArrayList<String>();

	private static String[] LETTERS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	static {
		expressionBuilders.add(new StringExpressionBuilder());
		expressionBuilders.add(new VariableExpressionBuilder());
		expressionBuilders.add(new BooleanExpressionBuilder());
		expressionBuilders.add(new IntegerExpressionBuilder());
		expressionBuilders.add(new DatasetExpressionBuilder());
		expressionBuilders.add(new FunctionExpressionBuilder());
		expressionBuilders.add(new NumberExpressionBuilder());
		expressionBuilders.add(new CellPositionExpressionBuilder());
		expressionBuilders.add(new RelativeCellExpressionBuilder());
		expressionBuilders.add(new SetExpressionBuilder());
		expressionBuilders.add(new CellObjectExpressionBuilder());
		expressionBuilders.add(new NullExpressionBuilder());
		expressionBuilders.add(new CurrentCellValueExpressionBuilder());
		expressionBuilders.add(new CurrentCellDataExpressionBuilder());

		assertorsMap.put(Op.Equals, new EqualsAssertor());
		assertorsMap.put(Op.EqualsGreatThen, new EqualsGreatThenAssertor());
		assertorsMap.put(Op.EqualsLessThen, new EqualsLessThenAssertor());
		assertorsMap.put(Op.GreatThen, new GreatThenAssertor());
		assertorsMap.put(Op.LessThen, new LessThenAssertor());
		assertorsMap.put(Op.NotEquals, new NotEqualsAssertor());
		assertorsMap.put(Op.In, new InAssertor());
		assertorsMap.put(Op.NotIn, new NotInAssertor());
		assertorsMap.put(Op.Like, new LikeAssertor());

		for (int i = 0; i < LETTERS.length; i++) {
			cellNameList.add(LETTERS[i]);
		}

		for (int i = 0; i < LETTERS.length; i++) {
			String name = LETTERS[i];
			for (int j = 0; j < LETTERS.length; j++) {
				cellNameList.add(name + LETTERS[j]);
			}
		}

		// 数值函数
		setFunction(new CountFunction());
		setFunction(new SumFunction());
		setFunction(new MaxFunction());
		setFunction(new MinFunction());
		setFunction(new ListFunction());
		setFunction(new AvgFunction());
		setFunction(new OrderFunction());
		
		// 时间函数
		setFunction(new WeekFunction());
		setFunction(new DayFunction());
		setFunction(new MonthFunction());
		setFunction(new YearFunction());
		setFunction(new FormatFunction());
		setFunction(new ToDayFunction());
		setFunction(new DateAddFunction());
		setFunction(new ToDateFunction());
		
		setFunction(new GetFunction());
		setFunction(new AbsFunction());
		setFunction(new CeilFunction());
		setFunction(new ChnFunction());
		setFunction(new ChnMoneyFunction());
		setFunction(new CosFunction());
		setFunction(new ExpFunction());
		setFunction(new FloorFunction());
		setFunction(new Log10Function());
		setFunction(new LogFunction());
		setFunction(new PowFunction());
		setFunction(new RandomFunction());
		setFunction(new RoundFunction());
		setFunction(new SinFunction());
		setFunction(new SqrtFunction());
		setFunction(new TanFunction());
		setFunction(new StdevpFunction());
		setFunction(new VaraFunction());
		setFunction(new ModeFunction());
		setFunction(new MedianFunction());

		// 字符串函数
		setFunction(new LengthFunction());
		setFunction(new LowerFunction());
		setFunction(new IndexOfFunction());
		setFunction(new ReplaceFunction());
		setFunction(new SubstringFunction());
		setFunction(new TrimFunction());
		setFunction(new UpperFunction());

		setFunction(new PageTotalFunction());
		setFunction(new PageNumberFunction());
		setFunction(new PageAvgFunction());
		setFunction(new PageCountFunction());
		setFunction(new PageMaxFunction());

		setFunction(new PageMinFunction());
		setFunction(new PageRowsFunction());
		setFunction(new PageSumFunction());

		setFunction(new ParameterFunction());
		setFunction(new ParameterIsEmptyFunction());
		setFunction(new JsonFunction());

		setFunction(new RowFunction());
		setFunction(new ColumnFunction());
		
		setFunction(new RangeFunction());
	}

	public static List<String> getCellNameList() {
		return cellNameList;
	}

	public static Function getFunction(String functionName) {
		return functions.get(functionName.toLowerCase());
	}

	public static Map<Op, Assertor> getAssertorsMap() {
		return assertorsMap;
	}

	public static boolean conditionEval(Op op, Object left, Object right) {
		Assertor assertor = assertorsMap.get(op);
		boolean result = assertor.eval(left, right);
		return result;
	}

	public static Expression parseExpression(String text) {
		ANTLRInputStream antlrInputStream = new ANTLRInputStream(text);
		ReportParserLexer lexer = new ReportParserLexer(antlrInputStream);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ReportParserParser parser = new ReportParserParser(tokenStream);
		ExpressionErrorListener errorListener = new ExpressionErrorListener();
		parser.addErrorListener(errorListener);
		exprVisitor = new ExpressionVisitor(expressionBuilders);
		Expression expression = exprVisitor.visitEntry(parser.entry());
		String error = errorListener.getErrorMessage();
		if (error != null) {
			throw new ReportParseException("Expression parse error:" + error);
		}
		return expression;
	}

	public static ExpressionVisitor getExprVisitor() {
		return exprVisitor;
	}

	private static void setFunction(Function function) {
		functions.put(function.name(), function);
	}
}
