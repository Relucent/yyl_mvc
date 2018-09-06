package yyl.mvc.util.json.v1;

import yyl.mvc.util.collect.Listx;
import yyl.mvc.util.collect.Mapx;

public class JsonUtil {

    private static JsonEncoder encoder = new JsonEncoder();
    private static JsonDecoder decoder = new JsonDecoder();

    /**
     * 将Java对象转化为JSON字符串
     * @param obj java对象
     * @return JSON字符串
     */
    public static String encode(Object object) {
        return encoder.encode(object);
    }

    /**
     * 将JSON字符串转化为Java对象
     * @param json JSON字符串
     * @return Java对象
     */
    public static Object decode(String json) {
        return decoder.decode(json);
    }

    /**
     * 将Java对象解析为MAP对象
     * @param json JSON字符串
     * @return MAP对象,如果解析失败返回null
     */
    public static Mapx toMap(String json) {
        return decoder.toMap(json);
    }

    /**
     * 将Java对象解析为LIST对象
     * @param json LIST字符串
     * @return LIST对象,如果解析失败返回null
     */
    public static Listx toList(String json) {
        return decoder.toList(json);
    }
}
