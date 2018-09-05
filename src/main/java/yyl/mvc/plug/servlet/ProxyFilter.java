package yyl.mvc.plug.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * _Filter代理，可以通过该代理，使用Spring托管_Filter<br>
 * @author YYL
 * @version 2014-04-08 1.0
 * @see javax.servlet.Filter
 */
public class ProxyFilter implements Filter {

	private String name;
	private Filter filter;
	private UrlPatternMatcher urlPatternMatcher = UrlPatternMatcher.DEFAULT_MATCHER;
	@SuppressWarnings("unchecked")
	private Map<String, String> initParameters = Collections.EMPTY_MAP;
	private boolean enable = true;
	private List<UrlPatternMatcher> excludes = new ArrayList<UrlPatternMatcher>();

	/**
	 * 初始化过滤器
	 * @param config 过滤器配置信息
	 */
	public void init(FilterConfig config) throws ServletException {
		if (enable) {
			ProxyFilterConfig proxyFilterConfig = new ProxyFilterConfig(config.getServletContext());
			proxyFilterConfig.setFilterName(name);
			proxyFilterConfig.setInitParameters(initParameters);
			filter.init(proxyFilterConfig);
		}
	}

	/**
	 * 运行过滤器
	 * @param request 请求
	 * @param response 应答
	 * @param chain 过滤器链
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (enable) {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			String contextPath = req.getContextPath();
			String requestUri = req.getRequestURI();
			String path = requestUri.substring(contextPath.length());

			// 如果在黑名单中，直接略过
			if (isExcluded(path)) {
				chain.doFilter(request, response);

				return;
			}

			// 如果符合redirect规则，进行跳转
			if (urlPatternMatcher.shouldRedirect(path)) {
				res.sendRedirect(contextPath + path + "/");

				return;
			}

			// 如果都没问题，才会继续进行判断
			if (urlPatternMatcher.matches(path)) {
				filter.doFilter(request, response, chain);
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * 销毁过滤器
	 */
	public void destroy() {
		if (enable) {
			filter.destroy();
		}
	}

	/**
	 * 是否是排除路径
	 * @param path 路径
	 */
	protected boolean isExcluded(String path) {
		for (UrlPatternMatcher exclude : excludes) {
			if (exclude.matches(path)) {
				return true;
			}
		}
		return false;
	}

	// ~ ==================================================
	/**
	 * 获得过滤器名称
	 * @return 过滤器名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置过滤器名称
	 * @param name 过滤器名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置代理的过滤器
	 * @param filter 代理的过滤器
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	/**
	 * 设置过URL
	 * @param urlPattern URL
	 */
	public void setUrlPattern(String urlPattern) {
		this.urlPatternMatcher = UrlPatternMatcher.create(urlPattern);
	}

	/**
	 * 设置初始化参数
	 * @param initParameters 初始化参数
	 */
	public void setInitParameters(Map<String, String> initParameters) {
		this.initParameters = initParameters;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * 设置排除匹配
	 * @param excludePatterns the excludePatterns to set
	 */
	public void setExcludePatterns(List<String> excludePatterns) {
		for (String excludePattern : excludePatterns) {
			excludes.add(UrlPatternMatcher.create(excludePattern));
		}
	}
}
