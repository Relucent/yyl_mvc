package yyl.mvc.common.logging.slf4j;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import yyl.mvc.common.logging.Logger;

/**
 * Slf4jLocationAwareLogger
 */
public class Slf4jLocationAwareLogger extends Logger {

	private static final Object[] NULL_ARGS = new Object[0];
	private static final String CALLER_FQCN = Slf4jLocationAwareLogger.class.getName();

	private final LocationAwareLogger logger;

	Slf4jLocationAwareLogger(LocationAwareLogger log) {
		this.logger = log;
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public boolean isFatalEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void trace(String message) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.TRACE_INT, message, NULL_ARGS, null);
	}

	@Override
	public void debug(String message) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.DEBUG_INT, message, NULL_ARGS, null);
	}

	@Override
	public void info(String message) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.INFO_INT, message, NULL_ARGS, null);
	}

	@Override
	public void warn(String message) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.WARN_INT, message, NULL_ARGS, null);
	}

	@Override
	public void error(String message) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.ERROR_INT, message, NULL_ARGS, null);
	}

	@Override
	public void fatal(String message) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.ERROR_INT, message, NULL_ARGS, null);
	}

	@Override
	public void trace(String message, Throwable throwable) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.TRACE_INT, message, NULL_ARGS, throwable);
	}

	@Override
	public void debug(String message, Throwable throwable) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.DEBUG_INT, message, NULL_ARGS, throwable);
	}

	@Override
	public void info(String message, Throwable throwable) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.INFO_INT, message, NULL_ARGS, throwable);
	}

	@Override
	public void warn(String message, Throwable throwable) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.WARN_INT, message, NULL_ARGS, throwable);
	}

	@Override
	public void error(String message, Throwable throwable) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.ERROR_INT, message, NULL_ARGS, throwable);
	}

	@Override
	public void fatal(String message, Throwable throwable) {
		logger.log(null, CALLER_FQCN, LocationAwareLogger.ERROR_INT, message, NULL_ARGS, throwable);
	}

	@Override
	public void trace(String format, Object... args) {
		if (isTraceEnabled()) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, args);
			logger.log(null, CALLER_FQCN, LocationAwareLogger.TRACE_INT, ft.getMessage(), NULL_ARGS, ft.getThrowable());
		}
	}

	@Override
	public void debug(String format, Object... args) {
		if (isDebugEnabled()) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, args);
			logger.log(null, CALLER_FQCN, LocationAwareLogger.DEBUG_INT, ft.getMessage(), NULL_ARGS, ft.getThrowable());
		}
	}

	@Override
	public void info(String format, Object... args) {
		if (isInfoEnabled()) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, args);
			logger.log(null, CALLER_FQCN, LocationAwareLogger.INFO_INT, ft.getMessage(), NULL_ARGS, ft.getThrowable());
		}
	}

	@Override
	public void warn(String format, Object... args) {
		if (isWarnEnabled()) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, args);
			logger.log(null, CALLER_FQCN, LocationAwareLogger.WARN_INT, ft.getMessage(), NULL_ARGS, ft.getThrowable());
		}
	}

	@Override
	public void error(String format, Object... args) {
		if (isErrorEnabled()) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, args);
			logger.log(null, CALLER_FQCN, LocationAwareLogger.ERROR_INT, ft.getMessage(), NULL_ARGS, ft.getThrowable());
		}
	}

	@Override
	public void fatal(String format, Object... args) {
		if (isErrorEnabled()) {
			FormattingTuple ft = MessageFormatter.arrayFormat(format, args);
			logger.log(null, CALLER_FQCN, LocationAwareLogger.ERROR_INT, ft.getMessage(), NULL_ARGS, ft.getThrowable());
		}
	}
}
