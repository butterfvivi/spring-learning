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
package org.vivi.framework.ureport.simple.ureport.expression.function;

import java.util.List;

import org.vivi.framework.ureport.simple.ureport.build.Context;
import org.vivi.framework.ureport.simple.ureport.exception.IndependenceException;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.model.Cell;
import org.vivi.framework.ureport.simple.ureport.utils.DataUtils;
import org.vivi.framework.ureport.simple.ureport.utils.StringUtils;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * @author Jacky.gao
 * @since 2017年5月23日
 */
public class FormatFunction implements Function {

	@Override
	public Object execute(List<ExpressionData<?>> params, Context context, Cell currentCell) {
		if (params == null || params.size() != 2) {
			throw new IndependenceException("format函数运行时需要两个参数");
		}
		Object value = DataUtils.getSingleExpressionData(params.get(0));
		String format = StringUtils.toTrimString(DataUtils.getSingleExpressionData(params.get(1)));
		return ToolUtils.format(value, format);
	}

	@Override
	public String name() {
		return "format";
	}
}
