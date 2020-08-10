package yyl.mvc.common.logging;

/**
 * 日志工具，支持6个级别日志<br>
 * 1. TRACE<br>
 * 2. DEBUG<br>
 * 3. INFO<br>
 * 4. WARN<br>
 * 5. ERROR<br>
 * 6. FATAL<br>
 */
public abstract class Logger {

	public static Logger getLogger(Class<?> clazz) {
		return LoggerManager.getLogger(clazz);
	}

	public static Logger getLogger(String name) {
		return LoggerManager.getLogger(name);
	}

	public abstract boolean isTraceEnabled();

	public abstract boolean isDebugEnabled();

	public abstract boolean isInfoEnabled();

	public abstract boolean isWarnEnabled();

	public abstract boolean isErrorEnabled();

	public abstract boolean isFatalEnabled();

	public abstract void trace(String message);

	public abstract void debug(String message);

	public abstract void info(String message);

	public abstract void warn(String message);

	public abstract void error(String message);

	public abstract void fatal(String message);

	public abstract void trace(String message, Throwable throwable);

	public abstract void debug(String message, Throwable throwable);

	public abstract void info(String message, Throwable throwable);

	public abstract void warn(String message, Throwable throwable);

	public abstract void error(String message, Throwable throwable);

	public abstract void fatal(String message, Throwable throwable);

	public void trace(String format, Object... args) {
		if (isTraceEnabled()) {
			LoggerMessageTuple tuple = LoggerMessageFormatter.arrayFormat(format, args);
			String message = tuple.getMessage();
			Throwable throwable = tuple.getThrowable();
			if (throwable != null) {
				trace(message, throwable);
			} else {
				trace(message);
			}
		}
	}

	public void debug(String format, Object... args) {
		if (isDebugEnabled()) {
			LoggerMessageTuple tuple = LoggerMessageFormatter.arrayFormat(format, args);
			String message = tuple.getMessage();
			Throwable throwable = tuple.getThrowable();
			if (throwable != null) {
				debug(message, throwable);
			} else {
				debug(message);
			}
		}
	}

	public void info(String format, Object... args) {
		if (isInfoEnabled()) {
			LoggerMessageTuple tuple = LoggerMessageFormatter.arrayFormat(format, args);
			String message = tuple.getMessage();
			Throwable throwable = tuple.getThrowable();
			if (throwable != null) {
				info(message, throwable);
			} else {
				info(message);
			}
		}
	}

	public void warn(String format, Object... args) {
		if (isWarnEnabled()) {
			LoggerMessageTuple tuple = LoggerMessageFormatter.arrayFormat(format, args);
			String message = tuple.getMessage();
			Throwable throwable = tuple.getThrowable();
			if (throwable != null) {
				warn(message, throwable);
			} else {
				warn(message);
			}
		}
	}

	public void error(String format, Object... args) {
		if (isErrorEnabled()) {
			LoggerMessageTuple tuple = LoggerMessageFormatter.arrayFormat(format, args);
			String message = tuple.getMessage();
			Throwable throwable = tuple.getThrowable();
			if (throwable != null) {
				error(message, throwable);
			} else {
				error(message);
			}
		}
	}

	public void fatal(String format, Object... args) {
		if (isFatalEnabled()) {
			LoggerMessageTuple tuple = LoggerMessageFormatter.arrayFormat(format, args);
			String message = tuple.getMessage();
			Throwable throwable = tuple.getThrowable();
			if (throwable != null) {
				fatal(message, throwable);
			} else {
				fatal(message);
			}
		}
	}
}
