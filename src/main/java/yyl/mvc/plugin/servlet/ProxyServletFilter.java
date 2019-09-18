package yyl.mvc.plugin.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * 代理_Servlet过滤器<br>
 * @author YYL
 * @version 2014-06-30 1.0
 * @see javax.servlet.Filter
 */
public class ProxyServletFilter extends ProxyFilter {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@SuppressWarnings("unchecked")
	private Map<UrlPatternMatcher, ProxyServlet> servletMap = Collections.EMPTY_MAP;

	/**
	 * 运行过滤器
	 * @param request 请求
	 * @param response 应答
	 * @param filterChain 过滤器链
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String contextPath = req.getContextPath();
		String requestUri = req.getRequestURI();
		String path = requestUri.substring(contextPath.length());
		logger.debug("path : {}", path);
		for (Map.Entry<UrlPatternMatcher, ProxyServlet> entry : servletMap.entrySet()) {
			UrlPatternMatcher urlPatternMatcher = entry.getKey();
			// 如果符合redirect规则，进行跳转
			if (urlPatternMatcher.shouldRedirect(path)) {
				String redirectUrl = contextPath + path + "/";
				logger.debug("redirect to : {}", path);
				res.sendRedirect(redirectUrl);
				return;
			}
			// 如果是/根，也不做拦截
			if (path.equals("/")) {
				break;
			}
			// 如果是JSP页面不做拦截
			if (path.endsWith(".jsp")) {
				break;
			}
			if (urlPatternMatcher.matches(path)) {
				logger.debug("{} match {}", urlPatternMatcher, path);
				Servlet servlet = entry.getValue();
				servlet.service(request, response);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * 初始化过滤器
	 * @param filterConfig 过滤器配置信息
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		for (Map.Entry<UrlPatternMatcher, ProxyServlet> entry : servletMap.entrySet()) {
			ProxyServlet servlet = entry.getValue();
			servlet.invokeInit(filterConfig.getServletContext());
		}
	}

	/**
	 * 销毁过滤器
	 */
	public void destroy() {
		for (Map.Entry<UrlPatternMatcher, ProxyServlet> entry : servletMap.entrySet()) {
			Servlet servlet = entry.getValue();
			servlet.destroy();
		}
	}

	/**
	 * 设置需要代理的_Servlet
	 * @param urlPatternMap 代理的Servlet
	 */
	public void setServletMap(Map<String, ProxyServlet> urlPatternMap) {
		servletMap = Maps.newHashMap();
		for (Map.Entry<String, ProxyServlet> entry : urlPatternMap.entrySet()) {
			UrlPatternMatcher urlPatternMatcher = UrlPatternMatcher.create(entry.getKey());
			servletMap.put(urlPatternMatcher, entry.getValue());
		}
	}
}
