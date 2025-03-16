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

import org.vivi.framework.ureport.simple.ureport.build.aggregate.Aggregate;
import org.vivi.framework.ureport.simple.ureport.build.aggregate.AvgAggregate;
import org.vivi.framework.ureport.simple.ureport.build.aggregate.CountAggregate;
import org.vivi.framework.ureport.simple.ureport.build.aggregate.CustomGroupAggregate;
import org.vivi.framework.ureport.simple.ureport.build.aggregate.GroupAggregate;
import org.vivi.framework.ureport.simple.ureport.build.aggregate.MaxAggregate;
import org.vivi.framework.ureport.simple.ureport.build.aggregate.MinAggregate;
import org.vivi.framework.ureport.simple.ureport.build.aggregate.RegroupAggregate;
import org.vivi.framework.ureport.simple.ureport.build.aggregate.ReselectAggregate;
import org.vivi.framework.ureport.simple.ureport.build.aggregate.SelectAggregate;
import org.vivi.framework.ureport.simple.ureport.build.aggregate.SumAggregate;
import org.vivi.framework.ureport.simple.ureport.definition.value.AggregateType;
import org.vivi.framework.ureport.simple.ureport.exception.CellComputeException;
import org.vivi.framework.ureport.simple.ureport.expression.model.expr.dataset.DatasetExpression;
import org.vivi.framework.ureport.simple.ureport.model.Cell;

/**
 * @author Jacky.gao
 * @since 2016年12月26日
 */
public class DatasetUtils {

	private static final Map<AggregateType, Aggregate> aggregates = new HashMap<AggregateType, Aggregate>();

	static {
		// 分组
		aggregates.put(AggregateType.group, new GroupAggregate());
		// 列表
		aggregates.put(AggregateType.select, new SelectAggregate());
		//
		aggregates.put(AggregateType.reselect, new ReselectAggregate());
		//
		aggregates.put(AggregateType.regroup, new RegroupAggregate());
		// 平均值
		aggregates.put(AggregateType.avg, new AvgAggregate());
		// 统计数量
		aggregates.put(AggregateType.count, new CountAggregate());
		// 求和
		aggregates.put(AggregateType.sum, new SumAggregate());
		// 最小值
		aggregates.put(AggregateType.min, new MinAggregate());
		// 最大值
		aggregates.put(AggregateType.max, new MaxAggregate());
		// 自定义分组
		aggregates.put(AggregateType.customgroup, new CustomGroupAggregate());
	}

	public static List<BindData> computeDatasetExpression(DatasetExpression expr, Cell cell, Context context) {
		AggregateType aggregateType = expr.getAggregate();
		Aggregate aggregate = aggregates.get(aggregateType);
		if (aggregate != null) {
			return aggregate.aggregate(expr, cell, context);
		} else {
			throw new CellComputeException("Unknow aggregate : " + aggregateType);
		}
	}
}
