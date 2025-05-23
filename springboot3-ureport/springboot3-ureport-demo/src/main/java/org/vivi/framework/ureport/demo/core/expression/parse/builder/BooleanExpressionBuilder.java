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
package org.vivi.framework.ureport.demo.core.expression.parse.builder;


import org.vivi.framework.ureport.demo.core.dsl.ReportParserParser;
import org.vivi.framework.ureport.demo.core.expression.model.expr.BaseExpression;
import org.vivi.framework.ureport.demo.core.expression.model.expr.BooleanExpression;

/**
 * @author Jacky.gao
 * @since 2016年12月25日
 */
public class BooleanExpressionBuilder implements ExpressionBuilder {
    @Override
    public BaseExpression build(ReportParserParser.UnitContext unitContext) {
        String text = unitContext.BOOLEAN().getText();
        return new BooleanExpression(Boolean.valueOf(text));
    }

    @Override
    public boolean support(ReportParserParser.UnitContext unitContext) {
        return unitContext.BOOLEAN() != null;
    }
}
