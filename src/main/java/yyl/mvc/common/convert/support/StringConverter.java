package yyl.mvc.common.convert.support;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import yyl.mvc.common.convert.Converter;

/**
 * 字符串类型转换器
 * @author YYL
 */
public class StringConverter implements Converter<String> {

	public static final StringConverter INSTANCE = new StringConverter();
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	public String convert(Object source, Class<? extends String> toType, String vDefault) {
		String result = vDefault;
		try {
			if (source == null) {
				/* do not handle */
			} else if (source instanceof Date) {
				result = new SimpleDateFormat(DATETIME_FORMAT).format((Date) source);
			} else {
				return String.valueOf(source);
			}
		} catch (Exception e) {
			// Ignore//
		}
		return result;
	}

	@Override
	public boolean support(Class<? extends String> type) {
		return String.class.isAssignableFrom(type);
	}

	public String convertString(String string) {
		if (StringUtils.isNotEmpty(string)) {
			return string;
		}
		return "";
	}

	//public Integer 
}
