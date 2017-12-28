package yyl.mvc.core.util.db;

/**
 * SQL 字符转译
 */

public class SqlUtil {

	/**
	 * 针对特殊字符转义(只支持MySql)
	 * @param value SQL内容字符串
	 * @return 转义后的字符串
	 */
	public static String escapeMySqlValue(String value) {
		if (value == null) {
			return null;
		}
		StringBuilder buffer = new StringBuilder(value.length() * 2);
		for (char c : value.toCharArray()) {
			if (c == '\\' || c == '%' || c == '_') {
				buffer.append('\\');
			}
			buffer.append(c);
		}
		return buffer.toString();
	}

	/**
	 * 构造参数占位符
	 * @param count 参数数
	 * @return
	 */
	public static String buildParamMasks(int count) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < count; i++) {
			if (i == count - 1) {
				buffer.append("?");
			} else {
				buffer.append("?,");
			}
		}
		return buffer.toString();
	}
}
