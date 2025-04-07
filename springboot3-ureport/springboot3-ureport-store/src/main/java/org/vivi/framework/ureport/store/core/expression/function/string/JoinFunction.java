package org.vivi.framework.ureport.store.core.expression.function.string;

import org.vivi.framework.ureport.store.core.build.BindData;
import org.vivi.framework.ureport.store.core.build.Context;
import org.vivi.framework.ureport.store.core.exception.ReportComputeException;
import org.vivi.framework.ureport.store.core.expression.function.Function;
import org.vivi.framework.ureport.store.core.expression.model.data.BindDataListExpressionData;
import org.vivi.framework.ureport.store.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.store.core.expression.model.data.ObjectExpressionData;
import org.vivi.framework.ureport.store.core.expression.model.data.ObjectListExpressionData;
import org.vivi.framework.ureport.store.core.model.Cell;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: summer
 * @Date: 2022/3/26 10:24
 * @Description:
 **/
@Component
public class JoinFunction implements Function {
    @Override
    public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
        if (dataList.size() < 2) {
            throw new ReportComputeException("Function ["+name()+"] requires an array of strings and a string parameter.");
        }

        ExpressionData<?> strArrayExpressionData = dataList.get(0);
        ExpressionData<?> joinStrExpressionData = dataList.get(1);

        String joinStr = "";
        if (joinStrExpressionData instanceof ObjectExpressionData) {
            ObjectExpressionData joinData = (ObjectExpressionData) joinStrExpressionData;
            Object data = joinData.getData();
            if (data != null) {
                joinStr = data.toString();
            }
        }

        if (strArrayExpressionData instanceof ObjectListExpressionData) {
            ObjectListExpressionData objData = (ObjectListExpressionData) strArrayExpressionData;
            List<?> datas = objData.getData();
            if (CollectionUtils.isEmpty(datas)) {
                return null;
            }

            return StringUtils.join(datas, joinStr);
        } else if (strArrayExpressionData instanceof BindDataListExpressionData) {
            BindDataListExpressionData objData = (BindDataListExpressionData) strArrayExpressionData;
            List<BindData> datas = objData.getData();
            if (CollectionUtils.isEmpty(datas)) {
                return null;
            }

            return StringUtils.join(datas.stream().map(d -> d.getValue()).collect(Collectors.toList()), joinStr);
        } else {
            return strArrayExpressionData.getData();
        }
    }

    @Override
    public String name() {
        return "join";
    }
}
