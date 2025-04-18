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
package org.vivi.framework.ureport.demo.core.expression.model.expr.cell;


import org.vivi.framework.ureport.demo.core.Utils;
import org.vivi.framework.ureport.demo.core.build.Context;
import org.vivi.framework.ureport.demo.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.demo.core.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.demo.core.expression.model.expr.BaseExpression;
import org.vivi.framework.ureport.demo.core.model.Cell;
import org.apache.commons.lang.StringUtils;

/**
 * @author Jacky.gao
 * @since 2017年1月20日
 */
public class CellObjectExpression extends BaseExpression {
    private static final long serialVersionUID = 1558531964770533126L;
    private String property;

    public CellObjectExpression(String property) {
        this.property = property;
    }

    @Override
    protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
        while (!context.isCellPocessed(cell.getName())) {
            context.getReportBuilder().buildCell(context, null);
        }
        if (StringUtils.isNotBlank(property)) {
            Object obj = Utils.getProperty(cell, property);
            return new ObjectExpressionData(obj);
        }
        return new ObjectExpressionData(cell);
    }
}
