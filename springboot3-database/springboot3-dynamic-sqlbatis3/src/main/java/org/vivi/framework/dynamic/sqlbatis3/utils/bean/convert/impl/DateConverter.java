package org.vivi.framework.dynamic.sqlbatis3.utils.bean.convert.impl;

import org.vivi.framework.dynamic.sqlbatis3.exception.ParseException;
import org.vivi.framework.dynamic.sqlbatis3.utils.TimeUtil;
import org.vivi.framework.dynamic.sqlbatis3.utils.UtilAll;
import org.vivi.framework.dynamic.sqlbatis3.utils.bean.convert.TypeConversionException;
import org.vivi.framework.dynamic.sqlbatis3.utils.bean.convert.TypeConvertCommon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateConverter extends TypeConvertCommon<Date> {

	@Override
	public Date convert(final Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Date) {
			return (Date) value;
		}
		if (value instanceof Calendar) {
			return new Date(((Calendar) value).getTimeInMillis());
		}
		if (value instanceof LocalDateTime) {
			return TimeUtil.toDate((LocalDateTime) value);
		}
		if (value instanceof LocalDate) {
			return TimeUtil.toDate((LocalDate) value);
		}
		if (value instanceof Number) {
			return new Date(((Number) value).longValue());
		}
		final String stringValue = value.toString().trim();
		if(UtilAll.UString.containsOnlyDigits(stringValue)){
			try {
				long milliseconds = Long.parseLong(stringValue);
				return new Date(milliseconds);
			} catch (NumberFormatException nfex) {
				throw new TypeConversionException(value, nfex);
			}
		}
	    Date result;
		try {
			result=	UtilAll.UDate.parseDate(stringValue,
					"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd","yyyy-MM-ddTHH:mm:ssZ");
		} catch (ParseException e) {
			throw new TypeConversionException(value, e);
		}
		return result;
	}

}