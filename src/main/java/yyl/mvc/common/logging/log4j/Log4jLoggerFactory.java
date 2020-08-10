
package yyl.mvc.common.logging.log4j;

import yyl.mvc.common.logging.Logger;
import yyl.mvc.common.logging.LoggerFactory;

/**
 * Log4jLoggerFactory
 */
public class Log4jLoggerFactory implements LoggerFactory {

	@Override
	public Logger getLogger(Class<?> clazz) {
		return new Log4jLogger(org.apache.log4j.Logger.getLogger(clazz));
	}

	@Override
	public Logger getLogger(String name) {
		return new Log4jLogger(org.apache.log4j.Logger.getLogger(name));
	}
}
