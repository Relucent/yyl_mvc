package yyl.mvc.core.plug.spring.context;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Environment 环境变量，主要用于获取spring的托管的Bean<br>
 */
@Deprecated
public class SpringEnvironment {

	/**
	 * 获得Spring托管的Bean<br>
	 * @param name Bean名称
	 * @return Bean实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSpringBean(String name) {
		return (T) getApplicationContext().getBean(name);
	}

	/**
	 * 获得Spring托管的Bean<br>
	 * @param type Bean类型
	 * @return Bean实例
	 */
	public static <T> T getSpringBean(Class<T> type) {
		return (T) getApplicationContext().getBean(type);
	}

	/**
	 * 获得Spring应用上下文<br>
	 * @return Spring应用上下文
	 */
	public static ApplicationContext getApplicationContext() {
		return ContextLoader.getCurrentWebApplicationContext();
	}

	/**
	 * 获得 HTTP请求对象
	 * @return HTTP请求对象
	 */
	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获得主机名称
	 * @return 主机名称
	 */
	protected String getHostName() {
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
