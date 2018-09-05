package yyl.mvc.util.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import yyl.mvc.util.collect.Listx;
import yyl.mvc.util.collect.Mapx;
import yyl.mvc.util.convert.support.BooleanConverter;
import yyl.mvc.util.convert.support.CharacterConverter;
import yyl.mvc.util.convert.support.DateConverter;
import yyl.mvc.util.convert.support.EnumConverter;
import yyl.mvc.util.convert.support.ListxConverter;
import yyl.mvc.util.convert.support.MapxConverter;
import yyl.mvc.util.convert.support.NumberConverter;
import yyl.mvc.util.convert.support.StringConverter;

/**
 * 类型转换工具类(Type Conversion) Standard Wrapped
 * @author YYL
 */
public class ConvertUtil {

	// ==============================Fields===========================================
	/** 转换器映射表 */
	private static final Map<Class<?>, Converter<?>> CONVERTERS = new ConcurrentHashMap<Class<?>, Converter<?>>();
	static {
		// 布尔/字符 类型
		CONVERTERS.put(Boolean.TYPE, BooleanConverter.INSTANCE);
		CONVERTERS.put(Character.TYPE, CharacterConverter.INSTANCE);
		//
		CONVERTERS.put(Boolean.class, BooleanConverter.INSTANCE);
		CONVERTERS.put(Character.class, CharacterConverter.INSTANCE);
		// 数值类型
		CONVERTERS.put(Byte.TYPE, NumberConverter.INSTANCE);
		CONVERTERS.put(Double.TYPE, NumberConverter.INSTANCE);
		CONVERTERS.put(Float.TYPE, NumberConverter.INSTANCE);
		CONVERTERS.put(Integer.TYPE, NumberConverter.INSTANCE);
		CONVERTERS.put(Long.TYPE, NumberConverter.INSTANCE);
		CONVERTERS.put(Short.TYPE, NumberConverter.INSTANCE);
		//
		CONVERTERS.put(Byte.class, NumberConverter.INSTANCE);
		CONVERTERS.put(Double.class, NumberConverter.INSTANCE);
		CONVERTERS.put(Float.class, NumberConverter.INSTANCE);
		CONVERTERS.put(Integer.class, NumberConverter.INSTANCE);
		CONVERTERS.put(Long.class, NumberConverter.INSTANCE);
		CONVERTERS.put(Short.class, NumberConverter.INSTANCE);
		// 枚举
		CONVERTERS.put(Enum.class, EnumConverter.INSTANCE);
		// 字符串/日期/集合 类型
		CONVERTERS.put(String.class, StringConverter.INSTANCE);
		CONVERTERS.put(Date.class, DateConverter.INSTANCE);
		CONVERTERS.put(Mapx.class, MapxConverter.INSTANCE);
		CONVERTERS.put(Listx.class, ListxConverter.INSTANCE);
	}

	/** 原始类型的封装类型列表* */
	private final static Collection<Class<?>> WRAPPED_TYPES = new ArrayList<Class<?>>();
	static {
		WRAPPED_TYPES.add(Boolean.class);
		WRAPPED_TYPES.add(Byte.class);
		WRAPPED_TYPES.add(Character.class);
		WRAPPED_TYPES.add(Double.class);
		WRAPPED_TYPES.add(Float.class);
		WRAPPED_TYPES.add(Integer.class);
		WRAPPED_TYPES.add(Long.class);
		WRAPPED_TYPES.add(Short.class);
	}

	// ==============================Methods==========================================
	/**
	 * 将对象转换为布尔类型
	 * @param value 对象转换
	 * @return 转换类型后的对象
	 */
	public static Boolean toBoolean(Object value) {
		return toBoolean(value, false);
	}

	/**
	 * 将对象转换为布尔类型
	 * @param value 对象转换
	 * @param _default 默认值
	 * @return 转换类型后的对象
	 */
	public static Boolean toBoolean(Object value, Boolean _default) {
		return convert(value, Boolean.class, _default);
	}

	/**
	 * 将对象转换为整形类型
	 * @param value 对象转换
	 * @return 转换类型后的对象
	 */
	public static Integer toInteger(Object value) {
		return toInteger(value, null);
	}

	/**
	 * 将对象转换为整形类型
	 * @param value 对象转换
	 * @param _default 默认值
	 * @return 转换类型后的对象
	 */
	public static Integer toInteger(Object value, Integer _default) {
		return convert(value, Integer.class, _default);
	}

	/**
	 * 将对象转换为长整形类型
	 * @param value 对象转换
	 * @return 转换类型后的对象
	 */
	public static Long toLong(Object value) {
		return toLong(value, null);
	}

	/**
	 * 将对象转换为长整形类型
	 * @param value 对象转换
	 * @param _default 默认值
	 * @return 转换类型后的对象
	 */
	public static Long toLong(Object value, Long _default) {
		return convert(value, Long.class, _default);
	}

	/**
	 * 将对象转换为浮点类型
	 * @param value 对象转换
	 * @return 转换类型后的对象
	 */
	public static Float toFloat(Object value) {
		return toFloat(value, null);
	}

	/**
	 * 将对象转换为浮点类型
	 * @param value 对象转换
	 * @param _default 默认值
	 * @return 转换类型后的对象
	 */
	public static Float toFloat(Object value, Float _default) {
		return convert(value, Float.class, _default);
	}

	/**
	 * 将对象转换为双字节类型
	 * @param value 对象转换
	 * @return 转换类型后的对象
	 */
	public static Double toDouble(Object value) {
		return toDouble(value, null);
	}

	/**
	 * 将对象转换为双字节类型
	 * @param value 对象转换
	 * @param _default 默认值
	 * @return 转换类型后的对象
	 */
	public static Double toDouble(Object value, Double _default) {
		return convert(value, Double.class, _default);
	}

	/**
	 * 将对象转换为字符串类型
	 * @param value 对象转换
	 * @return 转换类型后的对象
	 */
	public static String toString(Object value) {
		return toString(value, null);
	}

	/**
	 * 将对象转换为字符串类型
	 * @param value 对象转换
	 * @param _default 默认值
	 * @return 转换类型后的对象
	 */
	public static String toString(Object value, String _default) {
		return convert(value, String.class, _default);
	}

	/**
	 * 将对象转换为日期类型
	 * @param value 对象转换
	 * @return 转换类型后的对象
	 */
	public static Date toDate(Object value) {
		return toDate(value, null);
	}

	/**
	 * 将对象转换为日期类型
	 * @param value 对象转换
	 * @param _default 默认值
	 * @return 转换类型后的对象
	 */
	public static Date toDate(Object value, Date _default) {
		return convert(value, Date.class, _default);
	}

	/**
	 * 将对象转换为枚举类型
	 * @param value 对象转换
	 * @return 转换类型后的对象
	 */
	public static <T extends Enum<T>> T toEnum(Object value, Class<T> enumType) {
		return toEnum(value, enumType, null);
	}

	/**
	 * 将对象转换为枚举类型
	 * @param value 对象转换
	 * @param _default 默认值
	 * @return 转换类型后的对象
	 */
	public static <T extends Enum<T>> T toEnum(Object value, Class<T> enumType, T _default) {
		return convert(value, enumType, _default);
	}

	/**
	 * 将对象转换为集合类型
	 * @param value 对象转换
	 * @return 转换类型后的对象
	 */
	public static Listx toList(Object value) {
		return toList(value, null);
	}

	/**
	 * 将对象转换为集合类型
	 * @param value 对象转换
	 * @param _default 默认值
	 * @return 转换类型后的对象
	 */
	public static Listx toList(Object value, Listx _default) {
		return convert(value, Listx.class, _default);
	}

	/**
	 * 将对象转换映射表类型
	 * @param value 对象转换
	 * @return 转换类型后的对象
	 */
	public static Mapx toMap(Object value) {
		return toMap(value, null);
	}

	/**
	 * 将对象转换映射表类型
	 * @param value 对象转换
	 * @param _default 默认值
	 * @return 转换类型后的对象
	 */
	public static Mapx toMap(Object value, Mapx _default) {
		return convert(value, Mapx.class, _default);
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * 将对象转换为指定的类型
	 * @param obj 对象转换
	 * @param toType 转换的目标类型
	 * @return 转换类型后的对象(如果无法转换返回NULL)
	 */
	public static <T> T convert(Object obj, Class<T> toType) {
		return convert(obj, toType, null);
	}

	/**
	 * 将对象转换为指定的类型
	 * @param <T>
	 * @param obj 对象转换
	 * @param toType 转换的目标类型
	 * @param _default 默认值
	 * @return 转换类型后的对象(无法正确转换类型则返回默认值)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T convert(Object obj, Class<T> toType, T _default) {
		T result = _default;
		try {
			if (toType == null) {
				return _default;
			}
			Converter converter = findConverter(toType);
			if (converter == null) {
				return _default;
			}
			result = (T) converter.convert(obj, toType, _default);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> Converter<T> findConverter(Class<T> type) {
		if (type == null) {
			return null;
		}
		Converter converter = CONVERTERS.get(type);
		if (converter != null) {
			return converter;
		}
		for (Converter c : CONVERTERS.values()) {
			if (c.support(type)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * 判断类型是否是准类型
	 * @param clazz 对象类型
	 * @return 如果参数是标准类型返回TRUE，否则返回FLASE
	 */
	public static boolean isStandardType(Class<?> clazz) {
		// Primitive
		if (clazz.isPrimitive()) {
			return true;
		}
		// String
		if (String.class.equals(clazz)) {
			return true;
		}
		// Boolean
		if (Boolean.class.equals(clazz)) {
			return true;
		}
		// Character
		if (Character.class.equals(clazz)) {
			return true;
		}
		// AtomicInteger, AtomicLong, BigDecimal, BigInteger, Byte, Double,Float, Integer, Long, Short
		if (Number.class.isAssignableFrom(clazz)) {
			return true;
		}
		// Date
		if (Date.class.isAssignableFrom(clazz)) {
			return true;
		}
		// _Enum
		if (clazz.isEnum()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断类型是否是原始类型的封装类型
	 * @param clazz 对象类型
	 * @return 如果参数是原始类型的封装类型的封裝类型则返回TRUE，否则返回FLASE
	 */
	public static boolean isWrappedType(Class<?> clazz) {
		return WRAPPED_TYPES.contains(clazz);
	}
}
