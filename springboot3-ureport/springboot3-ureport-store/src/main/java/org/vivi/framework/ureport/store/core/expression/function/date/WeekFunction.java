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
package org.vivi.framework.ureport.store.core.expression.function.date;

import org.vivi.framework.ureport.store.core.build.Context;
import org.vivi.framework.ureport.store.core.exception.ReportComputeException;
import org.vivi.framework.ureport.store.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.store.core.model.Cell;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年1月22日
 */
@Component
public class WeekFunction extends CalendarFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) throws ReportComputeException {
		Calendar c = buildCalendar(dataList);
		int weekDay=c.get(Calendar.DAY_OF_WEEK);
		boolean isFirstSunday = (c.getFirstDayOfWeek() == Calendar.SUNDAY);
		if(isFirstSunday){
			weekDay = weekDay - 1;
			if(weekDay == 0){
				weekDay = 7;
			}
		}
		switch(weekDay){
		case 1:
			return "星期一";
		case 2:
			return "星期二";
		case 3:
			return "星期三";
		case 4:
			return "星期四";
		case 5:
			return "星期五";
		case 6:
			return "星期六";
		case 7:
			return "星期日";
		}
		throw new ReportComputeException("Unknow week day :"+weekDay);
	}

	@Override
	public String name() {
		return "week";
	}
}
