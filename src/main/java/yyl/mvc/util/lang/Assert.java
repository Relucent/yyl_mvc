package yyl.mvc.util.lang;

public class Assert {

	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isFalse(boolean expression) {
		isFalse(expression, "[Assertion failed] - this expression must be false");
	}

	public static void isFalse(boolean expression, String message) {
		if (expression) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(String value) {
		notEmpty(value, "[Assertion failed] - this string is required; it must not be empty");
	}

	public static void notEmpty(String value, String message) {
		if (value == null || value.length() == 0) {
			throw new IllegalArgumentException(message);
		}
	}
}
