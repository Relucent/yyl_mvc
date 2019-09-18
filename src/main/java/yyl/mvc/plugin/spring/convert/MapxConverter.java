package yyl.mvc.plugin.spring.convert;

import org.springframework.core.convert.converter.Converter;

import yyl.mvc.common.collect.Mapx;
import yyl.mvc.common.json.JsonUtil;

public class MapxConverter implements Converter<String, Mapx> {
    public Mapx convert(String text) {
        return JsonUtil.toMap(text);
    }
}
