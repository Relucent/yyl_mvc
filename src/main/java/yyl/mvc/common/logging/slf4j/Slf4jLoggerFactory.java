package yyl.mvc.common.logging.slf4j;

import org.slf4j.spi.LocationAwareLogger;

import yyl.mvc.common.logging.Logger;
import yyl.mvc.common.logging.LoggerFactory;

/**
 * Slf4jLoggerFactory
 */
public class Slf4jLoggerFactory implements LoggerFactory {

	@Override
	public Logger getLogger(Class<?> clazz) {
		return wrap(org.slf4j.LoggerFactory.getLogger(clazz));
	}

	@Override
	public Logger getLogger(String name) {
		return wrap(org.slf4j.LoggerFactory.getLogger(name));
	}

	private Logger wrap(org.slf4j.Logger logger) {
		return logger instanceof LocationAwareLogger ? new Slf4jLocationAwareLogger((LocationAwareLogger) logger) : new Slf4jLogger(logger);
	}
}
