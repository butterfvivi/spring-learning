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
package org.vivi.framework.ureport.demo.core.expression;

import org.vivi.framework.ureport.demo.core.build.assertor.*;
import org.vivi.framework.ureport.demo.core.dsl.ReportParserLexer;
import org.vivi.framework.ureport.demo.core.dsl.ReportParserParser;
import org.vivi.framework.ureport.demo.core.exception.ReportParseException;
import org.vivi.framework.ureport.demo.core.expression.function.Function;
import org.vivi.framework.ureport.demo.core.expression.model.Expression;
import org.vivi.framework.ureport.demo.core.expression.model.Op;
import org.vivi.framework.ureport.demo.core.expression.parse.ExpressionErrorListener;
import org.vivi.framework.ureport.demo.core.expression.parse.ExpressionVisitor;
import org.vivi.framework.ureport.demo.core.expression.parse.builder.*;
import org.vivi.framework.ureport.demo.core.utils.SpringContextUtils;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.*;


/**
 * 表达式工具
 */
public class ExpressionUtils {
    public static final String EXPR_PREFIX = "${";
    public static final String EXPR_SUFFIX = "}";
    private static ExpressionVisitor exprVisitor;
    private static Map<String, Function> functions = new HashMap<String, Function>();
    private static Map<Op, Assertor> assertorsMap = new HashMap<Op, Assertor>();
    private static List<ExpressionBuilder> expressionBuilders = new ArrayList<ExpressionBuilder>();
    private static List<String> cellNameList = new ArrayList<String>();
    private static String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    static {
        Collection<Function> coll = SpringContextUtils.getAllBeansOfType(Function.class);
        for (Function fun : coll) {
            functions.put(fun.name(), fun);
        }

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
    }

    public static List<String> getCellNameList() {
        return cellNameList;
    }

    public static Map<String, Function> getFunctions() {
        return functions;
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
}
