package yyl.mvc.plug.spring.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import yyl.mvc.util.convert.ConvertUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class EnumConverterFactory implements ConverterFactory<String, Enum> {

	@Override
	public <T extends Enum> Converter<String, T> getConverter(Class<T> toType) {
		return new EnumConverter(toType);
	}

	private static final class EnumConverter<T extends Enum> implements Converter<String, T> {
		private final Class<T> targetType;

		EnumConverter(Class<T> targetType) {
			this.targetType = targetType;
		}

		public T convert(String source) {
			return (T) ConvertUtil.toEnum(source, this.targetType);
		}
	}
}
