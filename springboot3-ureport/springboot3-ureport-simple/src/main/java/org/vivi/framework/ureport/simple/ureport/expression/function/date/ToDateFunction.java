package org.vivi.framework.ureport.simple.ureport.expression.function.date;

import java.util.List;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.exception.ReportComputeException;
import org.vivi.framework.ureport.simple.ureport.expression.function.Function;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.utils.DataUtils;
import org.vivi.framework.ureport.simple.ureport.utils.DateUtils;
import org.vivi.framework.ureport.simple.ureport.utils.StringUtils;

public class ToDateFunction implements Function {

	@Override
	public Object execute(List<ExpressionData<?>> params, Context context, Cell currentCell) {
		if (params == null || params.size() != 2) {
			throw new ReportComputeException("todate函数需要两个参数");
		}
		Object value = DataUtils.getSingleExpressionData(params.get(0));
		String format = StringUtils.toTrimString(DataUtils.getSingleExpressionData(params.get(1)));
		return DateUtils.parseDate(value, format);
	}

	@Override
	public String name() {
		return "todate";
	}
}
