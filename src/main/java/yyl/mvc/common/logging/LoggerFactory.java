package yyl.mvc.common.logging;

/**
 * 日志工具工厂类（接口）<br>
 */
public interface LoggerFactory {

	Logger getLogger(Class<?> clazz);

	Logger getLogger(String name);
}
