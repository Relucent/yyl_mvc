package yyl.mvc.plug.spring.convert;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import yyl.mvc.util.convert.ConvertUtil;

public class DateConverter implements Converter<String, Date> {

	public Date convert(String text) {
		return ConvertUtil.toDate(text, null);
	}

}
