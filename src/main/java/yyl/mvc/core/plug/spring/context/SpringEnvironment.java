package yyl.mvc.core.plug.spring.context;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * Environment 环境变量，主要用于获取spring的托管的Bean<br>
 */
public class SpringEnvironment {

	/**
	 * 获得Spring托管的Bean<br>
	 * @param name Bean名称
	 * @return Bean实例
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T getSpringBean(String name) {
		return (T) getApplicationContext().getBean(name);
	}

	/**
	 * 获得Spring托管的Bean<br>
	 * @param type Bean类型
	 * @return Bean实例
	 */
	public final static <T> T getSpringBean(Class<T> type) {
		return (T) getApplicationContext().getBean(type);
	}

	/**
	 * 获得Spring应用上下文<br>
	 * @return Spring应用上下文
	 */
	public final static ApplicationContext getApplicationContext() {
		return ContextLoader.getCurrentWebApplicationContext();
	}

	/**
	 * 获得主机名称
	 * @return 主机名称
	 */
	protected static String getHostName() {
		if (System.getenv("COMPUTERNAME") != null) {
			return System.getenv("COMPUTERNAME");
		} else {
			try {
				return (InetAddress.getLocalHost()).getHostName();
			} catch (UnknownHostException uhe) {
				String host = uhe.getMessage();
				if (host != null) {
					int colon = host.indexOf(':');
					if (colon > 0) {
						return host.substring(0, colon);
					}
				}
				return "unknown-host";
			}
		}
	}
}
