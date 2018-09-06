package yyl.mvc.util.json.v1;

import java.io.StringReader;

import yyl.mvc.util.collect.Listx;
import yyl.mvc.util.collect.Mapx;

/**
 * JSON编码工具类，将JAVA对象编码为JSON字符串。
 * @author YaoYiLang
 * @version 1.0
 */
public class JsonDecoder {

	/**
	 * 将Java对象解析为JAVA对象
	 * @param json JSON字符串
	 * @return JAVA对象
	 */
	@SuppressWarnings("unchecked")
	protected <T> T decode(String json) {
		return (T) new JsonTokener(new StringReader("[" + json + "]")).nextList().get(0);
	}

	/**
	 * 将Java对象解析为MAP对象
	 * @param json JSON字符串
	 * @return MAP对象,如果解析失败返回null
	 */
	public Mapx toMap(String json) {
		try {
			return (Mapx) decode(json);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将Java对象解析为LIST对象
	 * @param json LIST字符串
	 * @return LIST对象,如果解析失败返回null
	 */
	public Listx toList(String json) {
		try {
			return (Listx) decode(json);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}