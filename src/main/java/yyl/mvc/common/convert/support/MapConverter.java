package yyl.mvc.common.convert.support;

import java.util.Map;

import yyl.mvc.common.collect.Mapx;
import yyl.mvc.common.convert.Converter;

/**
 * MAP映射表类型转换器
 * @author YYL
 */
public class MapConverter implements Converter<Mapx> {

    public static final MapConverter INSTANCE = new MapConverter();

    @SuppressWarnings("rawtypes")
    @Override
    public Mapx convert(Object source, Class<? extends Mapx> toType, Mapx vDefault) {
        try {
            if (source instanceof Map) {
                Mapx result = new Mapx();
                for (Object o : (((Map) source).entrySet())) {
                    Map.Entry entry = (Map.Entry) o;
                    result.put(String.valueOf(entry.getKey()), entry.getValue());
                }
                return result;
            }
        } catch (Exception e) {
            // Ignore//
        }
        return vDefault;
    }

    @Override
    public boolean support(Class<? extends Mapx> type) {
        return Mapx.class.isAssignableFrom(type);
    }
}
