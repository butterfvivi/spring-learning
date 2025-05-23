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
package org.vivi.framework.ureport.demo.core.expression.function.date;

import org.vivi.framework.ureport.demo.core.build.Context;
import org.vivi.framework.ureport.demo.core.expression.model.data.ExpressionData;
import org.vivi.framework.ureport.demo.core.model.Cell;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
public class DayFunction extends CalendarFunction {
    @Override
    public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
        Calendar c = buildCalendar(dataList);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    @Override
    public String name() {
        return "day";
    }
}
