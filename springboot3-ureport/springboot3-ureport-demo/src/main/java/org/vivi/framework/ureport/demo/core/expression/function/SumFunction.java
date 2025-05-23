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
package org.vivi.framework.ureport.demo.core.expression.function;

import org.vivi.framework.ureport.demo.core.Utils;
import org.vivi.framework.ureport.demo.core.build.BindData;
import org.vivi.framework.ureport.demo.core.build.Context;
import org.vivi.framework.ureport.demo.core.expression.model.data.BindDataListExpressionData;
import org.vivi.framework.ureport.demo.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.demo.core.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.demo.core.expression.model.data.ObjectListExpressionData;
import org.vivi.framework.ureport.demo.core.model.Cell;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author Jacky.gao
 * @since 2016年12月27日
 */
@Component
public class SumFunction implements Function {

    @Override
    public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
        if (dataList == null || dataList.size() == 0) {
            return null;
        }
        Object singleData = null;
        BigDecimal total = new BigDecimal(0);
        for (ExpressionData<?> exprData : dataList) {
            if (exprData instanceof ObjectListExpressionData) {
                ObjectListExpressionData listExpr = (ObjectListExpressionData) exprData;
                List<?> list = listExpr.getData();
                for (Object obj : list) {
                    if (obj == null || StringUtils.isBlank(obj.toString())) {
                        continue;
                    }
                    singleData = obj;
                    BigDecimal bigData = Utils.toBigDecimal(obj);
                    total = total.add(bigData);
                }
            } else if (exprData instanceof ObjectExpressionData) {
                Object obj = exprData.getData();
                singleData = obj;
                if (obj != null && StringUtils.isNotBlank(obj.toString())) {
                    BigDecimal bigData = Utils.toBigDecimal(obj);
                    total = total.add(bigData);
                }
            } else if (exprData instanceof BindDataListExpressionData) {
                BindDataListExpressionData data = (BindDataListExpressionData) exprData;
                List<BindData> bindDataList = data.getData();
                for (BindData bindData : bindDataList) {
                    Object obj = bindData.getValue();
                    if (obj == null || StringUtils.isBlank(obj.toString())) {
                        continue;
                    }
                    singleData = obj;
                    BigDecimal bigData = Utils.toBigDecimal(obj);
                    total = total.add(bigData);
                }
            }
        }
        if (dataList.size() == 1) {
            if (singleData == null || singleData.equals("")) {
                return "";
            }
        }
        return total;
    }

    @Override
    public String name() {
        return "sum";
    }
}
