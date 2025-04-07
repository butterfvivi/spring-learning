package org.vivi.framework.ureport.store.core.expression.function.string;

import org.vivi.framework.ureport.store.core.build.Context;
import org.vivi.framework.ureport.store.core.exception.ReportComputeException;
import org.vivi.framework.ureport.store.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.store.core.model.Cell;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: summer
 * @Date: 2022/10/23 10:48
 * @Description:
 **/
@Component
public class SplitFunction extends StringFunction{
    @Override
    public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
        if (dataList.size() < 1) {
            throw new ReportComputeException("[" + name() + "]函数需要两个参数值");
        }
        String str = buildString(dataList.get(0));
        String separatorChars = buildString(dataList.get(1));
        return StringUtils.split(str, separatorChars);
    }

    @Override
    public String name() {
        return "split";
    }
}
