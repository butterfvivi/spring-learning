package org.vivi.framework.dynamic.sqlbatis3.utils;

import org.vivi.framework.dynamic.sqlbatis3.utils.bean.convert.Converter;
import org.vivi.framework.dynamic.sqlbatis3.utils.model.BaseField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单方式(效率高)实现类型转换,细节部分会不如ConvertUtils :ConvertUtils一次类型转换需要69ms 有点代价过高
 * 不过多个也大概是这个时间?
 */
public class ConvertUtil {
	private static Logger log = LoggerFactory.getLogger(ConvertUtil.class);
	private static final String NULL_STR = "NULL";
	private static final Map<String, List<BaseField>> fieldMap = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")

//	public static  List<BaseField> getBeanInfo(Class<?> clazz){
//		String entityName = clazz.getName();
//		if(!fieldMap.containsKey(entityName)){
//			initField(clazz);
//		}
//		return fieldMap.get(entityName);
//	}
	private static void getFieldList(Class<?> clazz, List<Field> fieldList) {
		fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
		if(null!=clazz.getSuperclass()){
			getFieldList(clazz.getSuperclass(), fieldList);
		}
	}

//	private static synchronized   void initField(Class<?> clazz) {
//		if(ClazzUtils.isBaseType(clazz)){
//			return;
//		}
//		String entityName=clazz.getName();
//		log.debug("initField {}",entityName);
//		List<Field> fieldList = new ArrayList<>();
//		getFieldList(clazz,fieldList);
//		List<BaseField> entityFieldList = new ArrayList<>();
//		Map<String,Integer> fieldHasMap = new HashMap<>();
//		MethodAccessData methodAccessData = BeanUtil.methodAccessData(clazz);
//		int i=0;
//		for (Field field : fieldList) {
//			Class<?> type = field.getType();
//			boolean isNormalField =BeanUtil.containsProperty(methodAccessData,field.getName(),"set")
//					&&ClazzUtils.isBaseType(type);
//			String fieldName = field.getName();
//			if(!isNormalField||fieldHasMap.containsKey(fieldName)){
//				continue;
//			}
//			BaseField entityField = new BaseField();
//			entityField.setFieldName(fieldName);
//			entityField.setType(type);
//			entityFieldList.add(entityField);
//			fieldHasMap.put(fieldName, i);
//			i++;
//		}
//		fieldMap.put(clazz.getName(), entityFieldList);
//	}

	private static Object toNull(Object value) {
		if (null == value) {
			return null;
		}
		if (value instanceof String) {
			value = value.toString().trim();
			if (NULL_STR.equals(value)) {
				return null;
			}
			if ("".equals(value)) {
				return null;
			}
		}
		return value;
	}

	public static Integer toInteger(Object value) {
		value = toNull(value);
		return Converter.get().toInteger(value);
	}

	public static String toString(Object value) {
		if (null == value) {
			return null;
		}
		if (value instanceof byte[]) {
			return new String((byte[]) value, Charset.defaultCharset());
		}
		return Converter.get().toString(value);
	}

	public static Long toLong(Object value) {
		value = toNull(value);
		return Converter.get().toLong(value);
	}

	public static Boolean toBoolean(Object value) {
		value = toNull(value);
		return Converter.get().toBoolean(value);
	}

	public static Double toDouble(Object value) {
		value = toNull(value);
		return Converter.get().toDouble(value);
	}

	public static Float toFloat(Object value) {
		value = toNull(value);
		return Converter.get().toFloat(value);
	}

	public static Short toShort(Object value) {
		value = toNull(value);
		return Converter.get().toShort(value);
	}
	public static Number toNumber(Object value) {
		value = toNull(value);
		return Converter.get().toNumber(value);
	}
	public static Byte toByte(Object value) {
		value = toNull(value);
		return Converter.get().toByte(value);
	}
	public static byte[] toBytes(Object value) {
		value = toNull(value);
		return Converter.get().toBytes(value);
	}

	public static Character toCharacter(Object value) {
		value = toNull(value);
		return Converter.get().toCharacter(value);
	}

	public static BigInteger toBigInteger(Object value) {
		value = toNull(value);
		return Converter.get().toBigInteger(value);
	}

	public static BigDecimal toBigDecimal(Object value) {
		value = toNull(value);
		return Converter.get().toBigDecimal(value);
	}

	public static LocalDateTime toLocalDateTime(Object value) {
		value = toNull(value);
		return Converter.get().toLocalDateTime(value);
	}

	public static LocalDate toLocalDate(Object value) {
		value = toNull(value);
		return Converter.get().toLocalDate(value);
	}

	public static LocalTime toLocalTime(Object value) {
		value = toNull(value);
		return Converter.get().toLocalTime(value);
	}

	public static Date toDate(Object value) {
		value = toNull(value);
		return Converter.get().toDate(value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T toObject(Object value, Class<?> targetType) {
		if (null == value) {
			return null;
		}
		if (value.getClass().isAssignableFrom(targetType)) {
			return (T) value;
		}
		return toObjectDetail(value, targetType);
	}

	@SuppressWarnings("unchecked")
	public static <T> T toObjectDetail(Object value, Class<?> targetType) {
		if (null == value) {
			return null;
		}

		if (targetType.isAssignableFrom(String.class)) {
			return (T) ConvertUtil.toString(value);
		}
		if (targetType.isAssignableFrom(Integer.class) || targetType.isAssignableFrom(int.class)) {
			return (T) ConvertUtil.toInteger(value);
		}
		if (targetType.isAssignableFrom(Long.class) || targetType.isAssignableFrom(long.class)) {
			return (T) ConvertUtil.toLong(value);
		}
		if (targetType.isAssignableFrom(BigDecimal.class)) {
			return (T) ConvertUtil.toBigDecimal(value);
		}
		if (targetType.isAssignableFrom(Boolean.class) || targetType.isAssignableFrom(boolean.class)) {
			return (T) ConvertUtil.toBoolean(value);
		}
		if (targetType.isAssignableFrom(Float.class) || targetType.isAssignableFrom(float.class)) {
			return (T) ConvertUtil.toFloat(value);
		}
		if (targetType.isAssignableFrom(Double.class) || targetType.isAssignableFrom(double.class)) {
			return (T) ConvertUtil.toDouble(value);
		}
		if (targetType.isAssignableFrom(Short.class) || targetType.isAssignableFrom(short.class)) {
			return (T) ConvertUtil.toShort(value);
		}
		if (targetType.isAssignableFrom(Byte.class) || targetType.isAssignableFrom(byte.class)) {
			return (T) ConvertUtil.toByte(value);
		}
		if (targetType.isAssignableFrom(Character.class) || targetType.isAssignableFrom(char.class)) {
			return (T) ConvertUtil.toCharacter(value);
		}
		if (targetType.isAssignableFrom(BigInteger.class)) {
			return (T) ConvertUtil.toBigInteger(value);
		}
		if (targetType.isAssignableFrom(Date.class)) {
			return (T) ConvertUtil.toDate(value);
		}
		if (targetType.isAssignableFrom(LocalDateTime.class)) {
			return (T) ConvertUtil.toLocalDateTime(value);
		}
		if (targetType.isAssignableFrom(LocalDate.class)) {
			return (T) ConvertUtil.toLocalDate(value);
		}
		if (targetType.isAssignableFrom(LocalTime.class)) {
			return (T) ConvertUtil.toLocalTime(value);
		}
		if (targetType.isAssignableFrom(byte[].class)) {
			return (T) ConvertUtil.toBytes(value);
		}
		if (targetType.isEnum()) {
			Class type = targetType;
			return (T) Enum.valueOf(type, value.toString());
			// ClazzUtils.cast(targetType, ) ;
		}
		return (T) value;
	}

}
