package yyl.mvc.plugin.jackson;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import yyl.mvc.common.collection.Listx;
import yyl.mvc.common.collection.Mapx;
import yyl.mvc.plugin.jackson.databind.TreeNodeConverts;

/**
 * Jackson_工具类
 * @author YYL
 * @version 2014-10-27 21:11:11
 */
public class JacksonUtil {

	//===================================Fields==============================================
	public static final ObjectMapper MAPPER = MyObjectMapper.INSTANCE;
	private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtil.class);

	//===================================Methods=============================================
	/**
	 * 将JAVA对象编码为JSON字符串
	 * @param src JAVA对象
	 * @return 对象的JSON字符串
	 */
	public static <T> String encode(T src) {
		try {
			return MAPPER.writeValueAsString(src);
		} catch (JsonProcessingException e) {
			LOGGER.error("!", e);
			return null;
		}
	}

	/**
	 * 将JSON转换为Mapx类型
	 * @param json JSON字符串
	 * @return Mapx对象,如果类型不匹配或者转换出现异常则返回null.
	 */
	public static Mapx toMapx(String json) {
		try {
			if (StringUtils.isNotEmpty(json)) {
				JsonNode node = MAPPER.readTree(json);
				if (node instanceof ObjectNode) {
					return TreeNodeConverts.toMapx((ObjectNode) node);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("!", e);
		}
		return null;
	}

	/**
	 * 将JSON转换为Listx类型
	 * @param json JSON字符串
	 * @return Listx对象,如果类型不匹配或者转换出现异常则返回null.
	 */
	public static Listx toListx(String json) {
		try {
			JsonNode node = MAPPER.readTree(json);
			if (node instanceof ArrayNode) {
				return TreeNodeConverts.toListx((ArrayNode) node);
			}
		} catch (Exception e) {
			LOGGER.error("!", e);
		}
		return null;
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
		try {
			return MAPPER.readValue(json, type);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			return _default;
		}
	}

	/**
	 * 将JSON字符串，解码为JAVA对象
	 * @param json JSON字符串
	 * @param token 类型标记
	 * @return JSON对应的JAVA对象，如果无法解析将返回NULL.
	 */
	public static <T> T decode(String json, TypeReference<T> token) {
		try {
			return MAPPER.readValue(json, token);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			return null;
		}
	}
}
