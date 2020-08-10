package yyl.mvc.common.logging.jdk;

import yyl.mvc.common.logging.Logger;
import yyl.mvc.common.logging.LoggerFactory;

/**
 * JdkLoggerFactory
 */
public class JdkLoggerFactory implements LoggerFactory {

	public Logger getLogger(Class<?> clazz) {
		return new JdkLogger(clazz);
	}

	public Logger getLogger(String name) {
		return new JdkLogger(name);
	}
}
