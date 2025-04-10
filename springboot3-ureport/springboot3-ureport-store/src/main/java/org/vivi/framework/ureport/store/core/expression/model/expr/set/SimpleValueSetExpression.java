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
package org.vivi.framework.ureport.store.core.expression.model.expr.set;

import org.vivi.framework.ureport.store.core.build.Context;
import org.vivi.framework.ureport.store.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.store.core.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.store.core.expression.model.expr.BaseExpression;
import org.vivi.framework.ureport.store.core.model.Cell;

/**
 * @author Jacky.gao
 * @since 2017年1月1日
 */
public class SimpleValueSetExpression extends BaseExpression {
	private static final long serialVersionUID = -5433811018086391838L;
	private Object simpleValue;
	public SimpleValueSetExpression(Object simpleValue) {
		this.simpleValue=simpleValue;
	}
	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		return new ObjectExpressionData(simpleValue);
	}
}
