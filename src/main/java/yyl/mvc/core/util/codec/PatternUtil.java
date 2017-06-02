package yyl.mvc.core.util.codec;

public class PatternUtil {

	public static String escape(String keyword) {
		StringBuilder buffer = new StringBuilder();
		for (char c : keyword.toCharArray()) {
			switch (c) {
			case '\\':
			case '$':
			case '(':
			case ')':
			case '*':
			case '+':
			case '.':
			case '[':
			case ']':
			case '?':
			case '^':
			case '{':
			case '}':
			case '|':
				buffer.append("\\");
			default:
				buffer.append(c);
			}
		}
		return buffer.toString();
	}
}
