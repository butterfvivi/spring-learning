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
package org.vivi.framework.ureport.demo.core.build;

import org.vivi.framework.ureport.demo.core.build.aggregate.*;
import org.vivi.framework.ureport.demo.core.definition.value.AggregateType;
import org.vivi.framework.ureport.demo.core.exception.CellComputeException;
import org.vivi.framework.ureport.demo.core.expression.model.expr.dataset.DatasetExpression;
import org.vivi.framework.ureport.demo.core.model.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatasetUtils {
    private static Map<AggregateType, Aggregate> aggregates = new HashMap<AggregateType, Aggregate>();

    static {
        aggregates.put(AggregateType.group, new GroupAggregate());
        aggregates.put(AggregateType.select, new SelectAggregate());
        aggregates.put(AggregateType.reselect, new ReselectAggregate());
        aggregates.put(AggregateType.regroup, new RegroupAggregate());
        aggregates.put(AggregateType.avg, new AvgAggregate());
        aggregates.put(AggregateType.count, new CountAggregate());
        aggregates.put(AggregateType.sum, new SumAggregate());
        aggregates.put(AggregateType.min, new MinAggregate());
        aggregates.put(AggregateType.max, new MaxAggregate());
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
