package org.vivi.framework.ureport.simple.common.utils;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.vivi.framework.ureport.simple.ureport.export.ProducerEnum;

public final class StringUtils  extends org.apache.commons.lang3.StringUtils {

	private StringUtils(){
		
	}
	
	/**
	 * 生成UUID
	 * @return
	 */
	public static String getUUID() {
	    String uuid = UUID.randomUUID().toString().trim().replaceAll("-", EMPTY);
	    return uuid;
	}
	
	/**
	 * 对象转字符串
	 * @return
	 */
	public static String toString(Object obj) {
		if(obj == null) {
			return null;
		}
		return obj.toString();
	}
	
	/**
	 * 获取当前时间格式字符串
	 * @return
	 */
	public static String randomFileName() {
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSS");
		String format = dt.format(LocalDateTime.now());
		return format + String.format("%03d", (int) (Math.random() * (100) + 0));
	}
	
	
	public static String decode(String value) {
		if (value == null) {
			return value;
		}
		try {
			return URLDecoder.decode(value, "utf-8");
		} catch (Exception ex) {
			return value;
		}
	}
	
	public static String getFileSuffix(ProducerEnum type) {
		if (type == ProducerEnum.EXCEL) {
			return ".xlsx";
		}
		if (type == ProducerEnum.WORD) {
			return ".docx";
		}
		if (type == ProducerEnum.PDF) {
			return ".pdf";
		}
		return null;
	}
}
