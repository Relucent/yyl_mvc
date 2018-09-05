package yyl.mvc.util.convert.support;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yyl.mvc.util.convert.Converter;

/**
 * 日期类型转换器
 * @author YYL
 * @version 2012-12-11
 * @see Converter
 */
public class DateConverter implements Converter<Date> {

	public static final DateConverter INSTANCE = new DateConverter();

	private static final Pattern DATE_PATTERN = Pattern.compile("^new Date\\((\\d+)\\)$");
	private static final Pattern DIGITS_PATTERN = Pattern.compile("^-?\\d+$");

	public Date convert(Object source, Class<? extends Date> toType, Date vDefault) {
		try {
			if (source == null) {
				return vDefault;
			}
			if (source instanceof Date) {
				return new Date(((Date) source).getTime());
			}
			if (source instanceof Calendar) {
				return ((Calendar) source).getTime();
			}
			if (source instanceof String) {
				String value = (String) source;
				Matcher digitsMatcher = DIGITS_PATTERN.matcher(value);
				if (digitsMatcher.matches()) {
					return new java.util.Date(Long.parseLong(value));
				}
				Matcher dateMatcher = DATE_PATTERN.matcher(value);
				if (dateMatcher.matches()) {
					if (dateMatcher.find()) {
						String msel = dateMatcher.group(1);
						return new java.util.Date(Long.parseLong(msel));
					}
				}
				return parseDate((String) source);
			}
		} catch (Exception e) {
			// Ignore//
		}
		return vDefault;
	}

	@Override
	public boolean support(Class<? extends Date> type) {
		return Date.class.isAssignableFrom(type);
	}

	private static Date parseDate(String str) {
		SimpleDateFormat parser = new SimpleDateFormat("", Locale.ENGLISH);
		parser.setLenient(true);
		ParsePosition pos = new ParsePosition(0);
		for (int i = 0; i < tryDatePatterns.length; i++) {
			String pattern = tryDatePatterns[i];
			if (pattern.endsWith("ZZ")) {
				pattern = pattern.substring(0, pattern.length() - 1);
			}
			parser.applyPattern(pattern);
			pos.setIndex(0);
			String str2 = str;
			if (pattern.endsWith("ZZ")) {
				for (int signIdx = indexOfSignChars(str2, 0); signIdx >= 0; signIdx = indexOfSignChars(str2, ++signIdx)) {
					str2 = reformatTimezone(str2, signIdx);
				}

			}
			Date date = parser.parse(str2, pos);
			if (date != null && pos.getIndex() == str2.length())
				return date;
		}
		return null;
	}

	private static int indexOfSignChars(String str, int startPos) {
		int idx = indexOf(str, '+', startPos);
		if (idx < 0) {
			idx = indexOf(str, '-', startPos);
		}
		return idx;
	}

	private static int indexOf(String str, char searchChar, int startPos) {
		if (str == null || str.length() == 0) {
			return -1;
		} else {
			return str.indexOf(searchChar, startPos);
		}
	}

	private static String reformatTimezone(String str, int signIdx) {
		String str2 = str;
		if (signIdx >= 0 //
				&& signIdx + 5 < str.length() //
				&& Character.isDigit(str.charAt(signIdx + 1)) //
				&& Character.isDigit(str.charAt(signIdx + 2))//
				&& str.charAt(signIdx + 3) == ':' //
				&& Character.isDigit(str.charAt(signIdx + 4)) //
				&& Character.isDigit(str.charAt(signIdx + 5))) {
			str2 = str.substring(0, signIdx + 3) + str.substring(signIdx + 4);
		}
		return str2;
	}

	private final static String[] tryDatePatterns;
	static {
		List<String> trys = new ArrayList<String>();
		trys.add("yyyy-MM-dd'T'HH:mm:ss");
		trys.add("yyyy-MM-dd HH:mm:ss");
		trys.add("yyyy-MM-dd HH:mm");
		trys.add("yyyy-MM-dd HH");
		trys.add("yyyy-MM-dd");
		trys.add("yyyy-MM");
		trys.add("yyyy");
		trys.add("MM/dd/yyyy");
		trys.add("EEE MMM dd HH:mm:ss zzz yyyy");
		tryDatePatterns = trys.toArray(new String[trys.size()]);
	}
}
