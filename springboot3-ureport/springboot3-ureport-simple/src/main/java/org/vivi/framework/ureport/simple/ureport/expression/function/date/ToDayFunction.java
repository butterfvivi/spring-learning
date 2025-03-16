package org.vivi.framework.ureport.simple.ureport.expression.function.date;

import java.util.Date;
import java.util.List;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.expression.function.Function;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;

public class ToDayFunction implements Function {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		return new Date();
	}

	@Override
	public String name() {
		return "today";
	}

}
