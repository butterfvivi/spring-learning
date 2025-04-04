package org.vivi.framework.ureport.simple.ureport.expression.function;

import java.util.ArrayList;
import java.util.List;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * Range(from,to,step)
 * 
 * @author
 *
 */
public class RangeFunction implements Function {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		List<Object> list = new ArrayList<Object>();
		int size = dataList.size();
		if (size == 1) {
			Integer to = ToolUtils.toInteger(dataList.get(0).getData());
			range(list, to);
		} else if (size == 2) {
			Integer from = ToolUtils.toInteger(dataList.get(0).getData());
			Integer to = ToolUtils.toInteger(dataList.get(1).getData());
			range(list, from, to);
		} else if (size == 3) {
			Integer from = ToolUtils.toInteger(dataList.get(0).getData());
			Integer to = ToolUtils.toInteger(dataList.get(1).getData());
			Integer step = ToolUtils.toInteger(dataList.get(2).getData());
			range(list, from, to, step);
		}

		return list;
	}

	@Override
	public String name() {

		return "range";
	}

	private void range(List<Object> list, Integer to) {
		if (to != null) {
			for (int i = 1; i <= to; i++) {
				list.add(i);
			}
		}
	}

	private void range(List<Object> list, Integer from, Integer to) {
		if (to != null && from != null) {
			for (int i = from; i <= to; i++) {
				list.add(i);
			}
		}
	}

	private void range(List<Object> list, Integer from, Integer to, Integer step) {
		if (to != null && from != null && step != null) {
			for (int i = from; i <= to; i += step) {
				list.add(i);
			}
		}
	}

}
