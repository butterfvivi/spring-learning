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
import org.vivi.framework.ureport.store.core.build.DatasetUtils;
import org.vivi.framework.ureport.store.core.definition.value.ValueType;
import org.vivi.framework.ureport.store.core.expression.model.expr.dataset.DatasetExpression;
import org.vivi.framework.ureport.store.core.model.Cell;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2016年12月21日
 */
public class DatasetValueCompute implements ValueCompute {
	@Override
	public List<BindData> compute(Cell cell, Context context) {
		DatasetExpression expr=(DatasetExpression)cell.getValue();
		return DatasetUtils.computeDatasetExpression(expr, cell, context);
	}
	@Override
	public ValueType type() {
		return ValueType.dataset;
	}
}
