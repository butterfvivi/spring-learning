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
package org.vivi.framework.ureport.demo.core.expression.function.math;

import org.vivi.framework.ureport.demo.core.build.Context;
import org.vivi.framework.ureport.demo.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.demo.core.model.Cell;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 求方差
 *
 * @author Jacky.gao
 * @since 2017年1月23日
 */
@Component
public class VaraFunction extends MathFunction {

    @Override
    public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
        List<BigDecimal> list = buildDataList(dataList);
        BigDecimal total = new BigDecimal(0);
        for (BigDecimal bigData : list) {
            total = total.add(bigData);
        }
        int size = list.size();
        BigDecimal avg = total.divide(new BigDecimal(size), 8, BigDecimal.ROUND_HALF_UP);
        double sum = 0;
        for (BigDecimal bigData : list) {
            BigDecimal data = bigData.subtract(avg);
            sum += Math.pow(data.doubleValue(), 2);
        }
        BigDecimal result = new BigDecimal(sum);
        return result.divide(new BigDecimal(size), 8, BigDecimal.ROUND_HALF_UP);
    }


    @Override
    public String name() {
        return "vara";
    }
}
