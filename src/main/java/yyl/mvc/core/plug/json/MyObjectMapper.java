package yyl.mvc.core.plug.json;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import yyl.mvc.core.plug.json.databind.DateDeserializer;
import yyl.mvc.core.plug.json.databind.DateSerializer;
import yyl.mvc.core.plug.json.databind.ListxDeserializer;
import yyl.mvc.core.plug.json.databind.MapxDeserializer;
import yyl.mvc.core.plug.json.databind.StringUnicodeSerializer;
import yyl.mvc.core.util.collect.Listx;
import yyl.mvc.core.util.collect.Mapx;

/**
 * Jackson_ObjectMapper的自定义扩展
 */
public class MyObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	public static final ObjectMapper INSTANCE = new MyObjectMapper();

	public MyObjectMapper() {

		// 当找不到对应的序列化器时 忽略此字段
		this.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

		// 支持结束
		this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		//
		this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		// 使Jackson JSON支持Unicode编码非ASCII字符
		SimpleModule module = new SimpleModule();
		module.addSerializer(String.class, new StringUnicodeSerializer());
		module.addSerializer(Date.class, new DateSerializer());

		module.addDeserializer(Date.class, new DateDeserializer());
		module.addDeserializer(Mapx.class, new MapxDeserializer());
		module.addDeserializer(Listx.class, new ListxDeserializer());
		this.registerModule(module);
	}
}
