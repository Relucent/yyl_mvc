package yyl.mvc.util.json;

import com.fasterxml.jackson.core.type.TypeReference;

import yyl.mvc.plug.json.JacksonUtil;
import yyl.mvc.util.collect.Listx;
import yyl.mvc.util.collect.Mapx;

/**
 * JSON解析工具类<br>
 */
public class JsonUtil {

	// ===================================Fields==============================================
	// ...

	// ===================================Methods=============================================
	/**
	 * 将JAVA对象编码为JSON字符串
	 * @param src JAVA对象
	 * @return 对象的JSON字符串
	 */
	public static <T> String encode(T src) {
		return JacksonUtil.encode(src);
	}

	/**
	 * 将JSON字符串解码为JAVA对象
	 * @param json 对象的JSON字符串
	 * @param type JAVA对象类型
	 * @return JSON对应的JAVA对象，如果无法解析将返回NULL.
	 */
	public static <T> T decode(String json, Class<T> type) {
		return decode(json, type, null);
	}

	/**
	 * 将JSON字符串，解码为JAVA对象
	 * @param json JSON字符串
	 * @param type JAVA对象类型
	 * @param _default 默认值
	 * @return JSON对应的JAVA对象，如果无法解析将返回默认值.
	 */
	public static <T> T decode(String json, Class<T> type, T _default) {
		return JacksonUtil.decode(json, type, _default);
	}

	/**
	 * 将JSON字符串，解码为JAVA对象(该方法依赖于JACKSON类库)
	 * @param json JSON字符串
	 * @param token 类型标记
	 * @return JSON对应的JAVA对象，如果无法解析将返回NULL.
	 */
	public static <T> T decode(String json, TypeReference<T> token) {
		return JacksonUtil.decode(json, token);
	}

	/**
	 * 将JSON转换为MAP对象
	 * @param json JSON字符串
	 * @return MAP对象,如果类型不匹配或者转换出现异常则返回null.
	 */
	public static Mapx toMapx(String json) {
		return JacksonUtil.toMapx(json);
	}

	/**
	 * 将JSON转换为LIST对象
	 * @param json JSON字符串
	 * @return LIST对象,如果类型不匹配或者转换出现异常则返回null.
	 */
	public static Listx toListx(String json) {
		return JacksonUtil.toListx(json);
	}

}