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
package org.vivi.framework.ureport.simple.ureport.expression.function.date;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.vivi.framework.ureport.simple.ureport.expression.function.Function;
import org.vivi.framework.ureport.simple.ureport.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.simple.ureport.utils.DataUtils;
import org.vivi.framework.ureport.simple.ureport.utils.DateUtils;

/**
 * @author Jacky.gao
 * @since 2017年1月22日
 */
public abstract class CalendarFunction implements Function {
	
	protected Calendar buildCalendar(List<ExpressionData<?>> params) {
		if (params == null || params.size() == 0) {
			return Calendar.getInstance();
		}
		ExpressionData<?> data = params.get(0);
		Object obj = DataUtils.getSingleExpressionData(data);
		Date date = DateUtils.toDate(obj);
		if(date != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return c;
		}
		return null;
	}
}
