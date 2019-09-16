package yyl.mvc.plug.spring.convert;

import org.springframework.core.convert.converter.Converter;

import yyl.mvc.util.collect.Listx;
import yyl.mvc.util.json.JsonUtil;

public class ListxConverter implements Converter<String, Listx> {
    public Listx convert(String text) {
        return JsonUtil.toList(text);
    }
}
