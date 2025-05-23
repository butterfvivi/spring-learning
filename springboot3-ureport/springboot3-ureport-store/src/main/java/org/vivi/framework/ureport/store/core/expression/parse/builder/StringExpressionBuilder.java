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
package org.vivi.framework.ureport.store.core.expression.parse.builder;


import org.vivi.framework.ureport.store.core.dsl.ReportParserParser;
import org.vivi.framework.ureport.store.core.expression.model.expr.BaseExpression;
import org.vivi.framework.ureport.store.core.expression.model.expr.StringExpression;

/**
 * @author Jacky.gao
 * @since 2016年12月23日
 */
public class StringExpressionBuilder implements ExpressionBuilder {
	@Override
	public BaseExpression build(ReportParserParser.UnitContext unitContext) {
		String text=unitContext.STRING().getText();
		text=text.substring(1,text.length()-1);
		StringExpression stringExpr=new StringExpression(text);
		return stringExpr;
	}
	@Override
	public boolean support(ReportParserParser.UnitContext unitContext) {
		return unitContext.STRING()!=null;
	}
}
