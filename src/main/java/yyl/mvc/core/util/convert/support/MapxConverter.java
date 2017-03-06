package yyl.mvc.core.util.convert.support;

import java.util.Map;

import yyl.mvc.core.util.collect.Mapx;
import yyl.mvc.core.util.collect.impl.HashMapx;
import yyl.mvc.core.util.convert.Converter;

/**
 * MAP映射表类型转换器
 * @author YYL
 */
public class MapxConverter implements Converter<Mapx> {

	public static final MapxConverter INSTANCE = new MapxConverter();

	@SuppressWarnings("rawtypes")
	@Override
	public Mapx convert(Object source, Class<? extends Mapx> toType, Mapx vDefault) {
		try {
			if (source instanceof Map) {
				Mapx result = new HashMapx();
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
