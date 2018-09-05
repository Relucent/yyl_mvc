package yyl.mvc.plug.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import yyl.mvc.plug.json.databind.DatePowerDeserializer;
import yyl.mvc.plug.json.databind.DatePowerSerializer;
import yyl.mvc.plug.json.databind.ListxDeserializer;
import yyl.mvc.plug.json.databind.MapxDeserializer;
import yyl.mvc.plug.json.databind.StringUnicodeSerializer;
import yyl.mvc.util.collect.Listx;
import yyl.mvc.util.collect.Mapx;

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

        SimpleModule module = new SimpleModule();

        // _Unicode编码非ASCII字符
        module.addSerializer(String.class, StringUnicodeSerializer.INSTANCE);

        // 日期序列化与反序列化
        module.addSerializer(Date.class, DatePowerSerializer.INSTANCE);
        module.addDeserializer(Date.class, DatePowerDeserializer.INSTANCE);

        // 扩展集合类的反序列化
        module.addDeserializer(Mapx.class, new MapxDeserializer());
        module.addDeserializer(Listx.class, new ListxDeserializer());
        this.registerModule(module);
    }
}
