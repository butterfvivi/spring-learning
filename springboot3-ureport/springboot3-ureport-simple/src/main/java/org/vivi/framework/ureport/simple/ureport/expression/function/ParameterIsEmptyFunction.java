package org.vivi.framework.ureport.simple.ureport.expression.function;

import java.util.List;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;

/**
 * @author Jacky.gao
 * @since 2017年12月7日
 */
public class ParameterIsEmptyFunction extends ParameterFunction {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		Object obj = super.execute(dataList, context, currentCell);
		if (obj == null || obj.toString().trim().equals("")) {
			return true;
		}
		return false;
	}

	@Override
	public String name() {
		return "emptyparam";
	}
}
