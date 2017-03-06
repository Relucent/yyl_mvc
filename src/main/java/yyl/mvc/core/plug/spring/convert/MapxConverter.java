package yyl.mvc.core.plug.spring.convert;

import org.springframework.core.convert.converter.Converter;

import yyl.mvc.core.util.collect.Mapx;
import yyl.mvc.core.util.json.JsonUtil;

public class MapxConverter implements Converter<String, Mapx> {

	public Mapx convert(String text) {
		return JsonUtil.toMapx(text);
	}
}
