package yyl.mvc.plugin.spring.convert;

import org.springframework.core.convert.converter.Converter;

import yyl.mvc.common.collect.Listx;
import yyl.mvc.common.json.JsonUtil;

public class ListxConverter implements Converter<String, Listx> {
    public Listx convert(String text) {
        return JsonUtil.toList(text);
    }
}
