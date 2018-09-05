package yyl.mvc.plug.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class ProxyServletConfig implements ServletConfig {
	private final String servletName;
	private final ServletContext servletContext;
	private final Map<String, String> initParameters;

	public ProxyServletConfig(String servletName, ServletContext servletContext, Map<String, String> initParameters) {
		this.servletName = servletName;
		this.servletContext = servletContext;
		this.initParameters = initParameters;
	}

	public String getServletName() {
		return servletName;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String getInitParameter(String name) {
		return initParameters.get(name);
	}

	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(initParameters.keySet());
	}
}