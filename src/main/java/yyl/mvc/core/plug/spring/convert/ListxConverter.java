package yyl.mvc.core.plug.spring.convert;

import org.springframework.core.convert.converter.Converter;

import yyl.mvc.core.util.collect.Listx;
import yyl.mvc.core.util.json.JsonUtil;

public class ListxConverter implements Converter<String, Listx> {

	public Listx convert(String text) {
		return JsonUtil.toListx(text);
	}

}
