package yyl.mvc.core.plug.json.databind;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 日期序列化
 */
public class DateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException,
			JsonProcessingException {
		String text = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
		gen.writeString(text);
	}

}
