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
import org.vivi.framework.ureport.store.core.chart.Chart;
import org.vivi.framework.ureport.store.core.chart.ChartData;
import org.vivi.framework.ureport.store.core.definition.value.ChartValue;
import org.vivi.framework.ureport.store.core.definition.value.ValueType;
import org.vivi.framework.ureport.store.core.model.Cell;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Jacky.gao
 * @since 2017年6月9日
 */
public class ChartValueCompute implements ValueCompute {

	@Override
	public List<BindData> compute(Cell cell, Context context) {
		ChartValue chartValue=(ChartValue)cell.getValue();
		Chart chart=chartValue.getChart();
		ChartData data=chart.doCompute(cell, context);
		List<BindData> list=new ArrayList<BindData>();
		list.add(new BindData(data));
		return list;
	}

	@Override
	public ValueType type() {
		return ValueType.chart;
	}
}
