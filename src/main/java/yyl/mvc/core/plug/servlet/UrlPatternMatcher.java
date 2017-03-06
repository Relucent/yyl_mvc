package yyl.mvc.core.plug.servlet;

/**
 * URL规则匹配器<br>
 * @author YYL
 */
abstract class UrlPatternMatcher {

	/** 默认规则匹配器 */
	public static final UrlPatternMatcher DEFAULT_MATCHER = new AllPassUrlPatternMatcher();

	private String urlPattern;

	/**
	 * 创建匹配规则
	 * @param urlPattern 匹配规则字符串
	 * @return URL规则匹配器
	 */
	public static UrlPatternMatcher create(String urlPattern) {
		UrlPatternMatcher urlPatternMatcher;
		if (urlPattern.equals("/*") || urlPattern.equals("/")) {
			urlPatternMatcher = DEFAULT_MATCHER;
		} else if (urlPattern.startsWith("*")) {
			String suffix = urlPattern.substring(1);
			urlPatternMatcher = new SuffixUrlPatternMatcher(suffix);
		} else if (urlPattern.endsWith("*")) {
			String prefix = urlPattern.substring(0, urlPattern.length() - 1);
			urlPatternMatcher = new PrefixUrlPatternMatcher(prefix);
		} else {
			urlPatternMatcher = new EqualsUrlPatternMatcher(urlPattern);
		}
		urlPatternMatcher.setUrlPattern(urlPattern);
		return urlPatternMatcher;
	}

	/**
	 * 判断URL是否匹配
	 * @param url URL
	 * @return 如果匹配返回true，否則返回false
	 */
	public abstract boolean matches(String url);

	/**
	 * URL是否是响应跳转
	 * @param url URL
	 * @return 如果是响应跳转返回true，否則返回false
	 */
	public boolean shouldRedirect(String url) {
		return false;
	}

	/**
	 * 获得匹配的表达式
	 * @return 匹配的表达式
	 */
	public String getUrlPattern() {
		return urlPattern;
	}

	/**
	 * 设置匹配的的表达式
	 * @param urlPattern 匹配的表达式
	 */
	void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	/**
	 * 后缀匹配的URL规则匹配器
	 */
	static class SuffixUrlPatternMatcher extends UrlPatternMatcher {
		private String suffix;

		public SuffixUrlPatternMatcher(String suffix) {
			this.suffix = suffix;
		}

		public boolean matches(String url) {
			return url.endsWith(suffix);
		}
	}

	/**
	 * 词头匹配的URL规则匹配器
	 */
	static class PrefixUrlPatternMatcher extends UrlPatternMatcher {
		private String withoutSlash;
		private String prefix;

		public PrefixUrlPatternMatcher(String prefix) {
			this.prefix = prefix;

			if (prefix.endsWith("/")) {
				withoutSlash = prefix.substring(0, prefix.length() - 1);
			} else {
				withoutSlash = prefix;
			}
		}

		public boolean matches(String url) {
			return shouldRedirect(url) || url.startsWith(prefix);
		}

		public boolean shouldRedirect(String url) {
			return url.equals(withoutSlash);
		}
	}

	/**
	 * 相等匹配的URL规则匹配器
	 */
	static class EqualsUrlPatternMatcher extends UrlPatternMatcher {
		private String urlPattern;

		public EqualsUrlPatternMatcher(String urlPattern) {
			this.urlPattern = urlPattern;
		}

		public boolean matches(String url) {
			return url.equals(urlPattern);
		}
	}

	/**
	 * 完全通过的URL规则匹配器
	 */
	static class AllPassUrlPatternMatcher extends UrlPatternMatcher {
		public boolean matches(String url) {
			return true;
		}
	}
}
