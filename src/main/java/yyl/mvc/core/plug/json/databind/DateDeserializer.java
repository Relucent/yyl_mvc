package yyl.mvc.core.plug.json.databind;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 日期反序列化
 */

public class DateDeserializer extends JsonDeserializer<Date> {

	private final static String[] tryDatePatterns;
	static {
		List<String> trys = new ArrayList<String>();
		trys.add("yyyy-MM-dd'T'HH:mm:ss");
		trys.add("yyyy-MM-dd HH:mm:ss");
		trys.add("yyyy-MM-dd HH:mm");
		trys.add("yyyy-MM-dd HH");
		trys.add("yyyy-MM-dd");
		trys.add("yyyy-MM");
		trys.add("yyyy");
		trys.add("MM/dd/yyyy");
		tryDatePatterns = trys.toArray(new String[trys.size()]);
	}

	@Override
	public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException,
			JsonProcessingException {
		try {
			String text = parser.getText();
			return DateUtils.parseDate(text, tryDatePatterns);
		} catch (Exception e) {
			return null;
		}
	}
}
