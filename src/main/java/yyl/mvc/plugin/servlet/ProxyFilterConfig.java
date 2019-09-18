package yyl.mvc.plugin.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * 代理过滤器配置信息<br>
 * @author YYL
 * @version 2014-04-08 1.0
 */
class ProxyFilterConfig implements FilterConfig {
	private String filterName;
	private ServletContext servletContext;

	@SuppressWarnings("unchecked")
	private Map<String, String> initParameters = Collections.EMPTY_MAP;

	/**
	 * 构造函数
	 * @param servletContext _Servlet上下文
	 */
	public ProxyFilterConfig(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * 获得过滤器名称
	 * @return 过滤器名称
	 */
	public String getFilterName() {
		return filterName;
	}

	/**
	 * 获得_Servlet上下文
	 * @return _Servlet上下文
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * 获得初始化参数
	 * @param name 参数名称
	 * @return 参数数值
	 */
	public String getInitParameter(String name) {
		return initParameters.get(name);
	}

	/**
	 * 获得初始化参数名称
	 * @return 参数名称迭代
	 */
	@Override
	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(initParameters.keySet());
	}

	// ==================================================
	/**
	 * 设置过滤器名称
	 * @param filterName 过滤器名称
	 */
	protected void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	/**
	 * 设置过滤器参数
	 * @param initParameters 过滤器参数
	 */
	public void setInitParameters(Map<String, String> initParameters) {
		this.initParameters = initParameters;
	}
}
