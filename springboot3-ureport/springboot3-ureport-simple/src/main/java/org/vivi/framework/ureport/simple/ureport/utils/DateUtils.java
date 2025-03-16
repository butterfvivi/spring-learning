package org.vivi.framework.ureport.simple.ureport.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import org.vivi.framework.ureport.simple.ureport.exception.ReportComputeException;

public class DateUtils {

	public static Date toDate(Object obj) {
		if (obj == null || StringUtils.isBlank(obj.toString())) {
			return null;
		}
		if (obj instanceof Date) {
			return (Date) obj;
		}
		if (obj instanceof LocalDate) {
			LocalDate localDate = (LocalDate) obj;
			return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		}
		if (obj instanceof LocalDateTime) {
			LocalDateTime localDateTime = (LocalDateTime) obj;
			return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		}
		if (obj instanceof String) {
			return parseDate(String.valueOf(obj));
		}
		return null;
	}
	
	public static Date parseDate(Object obj, String format) {
		if (obj == null || StringUtils.isBlank(obj.toString()) || StringUtils.isBlank(format)) {
			return null;
		}
		if (obj instanceof Date) {
			return (Date) obj;
		}
		if (obj instanceof LocalDate) {
			LocalDate localDate = (LocalDate) obj;
			return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		}
		if (obj instanceof LocalDateTime) {
			LocalDateTime localDateTime = (LocalDateTime) obj;
			return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		}
		if (obj instanceof String) {
			return parseDate(String.valueOf(obj), format);
		}
		return null;
	}
	
	public static Date parseDate(String value, String format) {
		try {
			if (format.contains("H") || format.contains("h")) {
				LocalDateTime localDateTime = LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format));
				return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
			} else {
				LocalDate localDate = LocalDate.parse(value, DateTimeFormatter.ofPattern(format));
				return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			}
		} catch (Exception e) {
			throw new ReportComputeException("Can not convert " + value + " to Date. by format " + format);
		}
	}

	public static Date parseDate(String value) {
		String format = null;
		if(value.matches("\\d{4}-\\d{2}-\\d{2}")) {
			format = "yyyy-MM-dd";
		} else if(value.matches("\\d{4}/\\d{2}/\\d{2}")) {
			format = "yyyy/MM/dd";
		} else if(value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
			format = "yyyy-MM-dd HH:mm:ss";
		} else if(value.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")) {
			format = "yyyy-MM-dd'T'HH:mm:ss";
		}
		if(format != null) {
			return parseDate(value, format);
		}
		throw new ReportComputeException("Can not convert " + value + " to Date.");
	}

	public static void main(String[] args) {
		String value = "2010-02-03T10:00:00";
		System.out.println( parseDate(value));
	}
}
