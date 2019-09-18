package yyl.mvc.plugin.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * _Servlet代理，可以通过该代理，使用Spring托管_Servlet<br>
 * @author YYL
 * @version 2014-04-08 1.0
 * @see javax.servlet.Servlet
 */
public class ProxyServlet implements Servlet {
	// ===================================Fields==============================================
	/** 记录日志的对象. */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private final String name;
	private final Servlet servlet;
	private final Map<String, String> initParams;
	private final boolean enable;

	// ===================================Constructors==========================================
	public ProxyServlet(String name, Servlet servlet) {
		this(name, servlet, true);
	}

	public ProxyServlet(String name, Servlet servlet, Map<String, String> initParams) {
		this(name, servlet, initParams, true);
	}

	public ProxyServlet(String name, Servlet servlet, boolean enable) {
		this(name, servlet, Collections.<String, String> emptyMap(), enable);
	}

	public ProxyServlet(String name, Servlet servlet, Map<String, String> initParams, boolean enable) {
		this.name = name;
		this.servlet = servlet;
		this.initParams = initParams;
		this.enable = enable;
	}

	// ===================================Methods=============================================
	/**
	 * 执行_Servlet
	 * @param context Servlet上下文
	 */
	protected void invokeInit(ServletContext context) throws ServletException {
		if (enable) {
			init(new ProxyServletConfig(name, context, initParams));
		}
	}

	/**
	 * 初始化Servlet
	 * @param config Servlet配置信息
	 */
	public void init(ServletConfig config) throws ServletException {
		if (enable) {
			servlet.init(new ProxyServletConfig(name, config.getServletContext(), initParams));
		}
	}

	/**
	 * 执行Servlet
	 * @param req 请求
	 * @param res 响应
	 */
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		logger.debug(name);
		if (enable) {
			servlet.service(req, res);
		} else {
			logger.debug("skip");
			((HttpServletResponse) res).sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	/**
	 * 获得Servlet配置
	 * @return Servlet配置
	 */
	public ServletConfig getServletConfig() {
		return servlet.getServletConfig();
	}

	/**
	 * 获得Servlet信息
	 * @return Servlet信息
	 */
	public String getServletInfo() {
		return name;
	}

	/**
	 * 銷毀Servlet
	 */
	public void destroy() {
		if (enable) {
			servlet.destroy();
		}
	}
}
