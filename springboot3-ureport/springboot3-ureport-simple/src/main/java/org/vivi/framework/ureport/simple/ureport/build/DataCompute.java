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
package org.vivi.framework.ureport.simple.ureport.build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vivi.framework.ureport.simple.ureport.build.compute.ChartValueCompute;
import org.vivi.framework.ureport.simple.ureport.build.compute.DatasetValueCompute;
import org.vivi.framework.ureport.simple.ureport.build.compute.ExpressionValueCompute;
import org.vivi.framework.ureport.simple.ureport.build.compute.ImageValueCompute;
import org.vivi.framework.ureport.simple.ureport.build.compute.SimpleValueCompute;
import org.vivi.framework.ureport.simple.ureport.build.compute.SlashValueCompute;
import org.vivi.framework.ureport.simple.ureport.build.compute.ValueCompute;
import org.vivi.framework.ureport.simple.ureport.build.compute.ZxingValueCompute;
import org.vivi.framework.ureport.simple.ureport.definition.value.Value;
import org.vivi.framework.ureport.simple.ureport.definition.value.ValueType;
import org.vivi.framework.ureport.simple.ureport.exception.ReportException;
import org.vivi.framework.ureport.simple.ureport.model.Cell;

/**
 * @author Jacky.gao
 * @since 2016年12月21日
 */
public class DataCompute {

	private static final Map<ValueType, ValueCompute> valueComputesMap = new HashMap<ValueType, ValueCompute>();

	static {
		// 文本值
		valueComputesMap.put(ValueType.simple, new SimpleValueCompute());
		// 数据集值
		valueComputesMap.put(ValueType.dataset, new DatasetValueCompute());
		// 表达式值
		valueComputesMap.put(ValueType.expression, new ExpressionValueCompute());
		// 图片
		valueComputesMap.put(ValueType.image, new ImageValueCompute());
		// 斜表头
		valueComputesMap.put(ValueType.slash, new SlashValueCompute());
		// 二维码
		valueComputesMap.put(ValueType.zxing, new ZxingValueCompute());
		// 图表
		valueComputesMap.put(ValueType.chart, new ChartValueCompute());
	}

	public static List<BindData> buildCellData(Cell cell, Context context) {
		context.resetVariableMap();
		Value value = cell.getValue();
		ValueCompute valueCompute = valueComputesMap.get(value.getType());
		if (valueCompute != null) {
			List<BindData> list = valueCompute.compute(cell, context);
			return list;
		}
		throw new ReportException("Unsupport value: " + value);
	}
}
