package yyl.mvc.plug.spring.convert;

import org.springframework.core.convert.converter.Converter;

import yyl.mvc.util.collect.Mapx;
import yyl.mvc.util.json.JsonUtil;

public class MapxConverter implements Converter<String, Mapx> {

	public Mapx convert(String text) {
		return JsonUtil.toMapx(text);
	}
}
