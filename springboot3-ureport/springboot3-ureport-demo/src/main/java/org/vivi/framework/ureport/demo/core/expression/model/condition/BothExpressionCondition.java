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
package org.vivi.framework.ureport.demo.core.expression.model.condition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.vivi.framework.ureport.demo.core.build.Context;
import org.vivi.framework.ureport.demo.core.expression.model.Expression;
import org.vivi.framework.ureport.demo.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.demo.core.model.Cell;

/**
 * @author Jacky.gao
 * @since 2016年11月22日
 */
public class BothExpressionCondition extends BaseCondition {
    private ConditionType type = ConditionType.expression;
    @JsonIgnore
    private Expression leftExpression;
    @JsonIgnore
    private Expression rightExpression;

    @Override
    Object computeLeft(Cell cell, Cell currentCell, Object obj, Context context) {
        ExpressionData<?> exprData = leftExpression.execute(cell, currentCell, context);
        return extractExpressionData(exprData);
    }

    @Override
    Object computeRight(Cell cell, Cell currentCell, Object obj, Context context) {
        ExpressionData<?> exprData = rightExpression.execute(cell, currentCell, context);
        return extractExpressionData(exprData);
    }


    @Override
    public ConditionType getType() {
        return type;
    }

    public void setLeftExpression(Expression leftExpression) {
        this.leftExpression = leftExpression;
    }

    public void setRightExpression(Expression rightExpression) {
        this.rightExpression = rightExpression;
    }
}
