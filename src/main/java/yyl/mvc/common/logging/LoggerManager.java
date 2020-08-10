package yyl.mvc.common.logging;

import yyl.mvc.common.logging.jdk.JdkLoggerFactory;
import yyl.mvc.common.logging.log4j.Log4jLoggerFactory;
import yyl.mvc.common.logging.slf4j.Slf4jLoggerFactory;

/**
 * 日志工具管理<br>
 */
public class LoggerManager {

	private static LoggerFactory DEFAULT_LOGGER_FACTORY = null;

	static {
		if (DEFAULT_LOGGER_FACTORY == null) {
			try {
				Class.forName("org.slf4j.Logger");
				DEFAULT_LOGGER_FACTORY = new Slf4jLoggerFactory();
			} catch (Throwable e) {
				/* Ignore */
			}
		}
		if (DEFAULT_LOGGER_FACTORY == null) {
			try {
				Class.forName("org.apache.log4j.Logger");
				DEFAULT_LOGGER_FACTORY = new Log4jLoggerFactory();
			} catch (Throwable e) {
				/* Ignore */
			}
		}
		if (DEFAULT_LOGGER_FACTORY == null) {
			DEFAULT_LOGGER_FACTORY = new JdkLoggerFactory();
		}
	}

	public static void setDefaultLogFactory(LoggerFactory defaultLogFactory) {
		if (defaultLogFactory == null) {
			throw new IllegalArgumentException("defaultLogFactory can not be null.");
		}
		DEFAULT_LOGGER_FACTORY = defaultLogFactory;
	}

	public static LoggerFactory getDefaultLogFactory() {
		return DEFAULT_LOGGER_FACTORY;
	}

	static Logger getLogger(Class<?> clazz) {
		return DEFAULT_LOGGER_FACTORY.getLogger(clazz);
	}

	static Logger getLogger(String name) {
		return DEFAULT_LOGGER_FACTORY.getLogger(name);
	}
}
