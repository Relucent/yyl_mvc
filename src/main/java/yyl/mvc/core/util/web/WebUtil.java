package yyl.mvc.core.util.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;

/**
 * WEB工具类
 * @author _yyl
 */
public class WebUtil {

	/**
	 * 获得SessionId
	 * @param request HTTP请求
	 * @return SessionId
	 */
	public static String getSessionId(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session == null ? null : session.getId();
	}

	/**
	 * 获得请求访问的URI
	 * @param request HTTP请求
	 * @return 请求的URI
	 */
	public static String getPathWithinApplication(HttpServletRequest request) {
		String contextPath = getContextPath(request);
		String requestUri = getRequestUri(request);
		if (requestUri.toLowerCase().startsWith(contextPath.toLowerCase())) {
			String path = requestUri.substring(contextPath.length());
			return path.isEmpty() ? "/" : path;
		}
		return requestUri;
	}

	/**
	 * 获得请求的URI(统一资源标识符)
	 * @param request HTTP请求
	 * @return 请求的URI
	 */
	public static String getRequestUri(HttpServletRequest request) {
		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
		if (uri == null) {
			uri = request.getRequestURI();
		}
		return normalize(decodeAndCleanUriString(request, uri));
	}

	/**
	 * 获得请求的上下文路径
	 * @param request HTTP请求
	 * @return 请求的上下文路径
	 */
	public static String getContextPath(HttpServletRequest request) {
		String contextPath = (String) request.getAttribute("javax.servlet.include.context_path");
		if (contextPath == null) {
			contextPath = request.getContextPath();
		}
		if ("/".equals(contextPath)) {
			contextPath = "";
		}
		return decodeRequestString(request, contextPath);
	}

	@SuppressWarnings("deprecation")
	public static String decodeRequestString(HttpServletRequest request, String source) {
		String enc = determineEncoding(request);
		try {
			return URLDecoder.decode(source, enc);
		} catch (UnsupportedEncodingException ex) {
			return URLDecoder.decode(source);
		}
	}

	/**
	 * 规范化路径
	 * @param path 路径
	 * @return 规范化的路径
	 */
	public static String normalize(String path) {
		return normalize(path, true);
	}

	/**
	 * 获得内容描述
	 * @param path 文件名(或者文件路径)
	 * @param request HTTP请求
	 * @return 内容描述
	 */
	public static String getContentDispositionFilename(String path, HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT").toLowerCase();
		String filename = FilenameUtils.getName(path);
		try {
			// firefox
			if (userAgent.indexOf("firefox") >= 0) {
				filename = "\"" + new String(filename.getBytes("UTF-8"), "ISO8859-1") + "\"";
			}
			// msie|safari
			else {
				filename = URLEncoder.encode(filename, "UTF-8");
				filename = filename.replace("+", "%20");
			}
		} catch (UnsupportedEncodingException e) {
		}
		return filename;
	}

	/**
	 * 判断是否AJAX请求
	 * @param request HTTP请求
	 * @return 如果是AJAX请求返回true,如果不是则返回false.
	 */
	public static boolean isAjax(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");// XMLHttpRequest
		return "XMLHttpRequest".equals(header) ? true : false;
	}

	/**
	 * 规范化路径
	 * @param path 路径
	 * @param replaceBackSlash 是否替换反斜杠(\)
	 * @return 规范化的路径
	 */
	private static String normalize(String path, boolean replaceBackSlash) {
		if (path == null) {
			return null;
		}
		String normalized = path;
		if ((replaceBackSlash) && (normalized.indexOf('\\') >= 0)) {
			normalized = normalized.replace('\\', '/');
		}
		if (normalized.equals("/.")) {
			return "/";
		}
		if (!(normalized.startsWith("/"))) {
			normalized = "/" + normalized;
		}
		while (true) {
			int index = normalized.indexOf("//");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + normalized.substring(index + 1);
		}
		while (true) {
			int index = normalized.indexOf("/./");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + normalized.substring(index + 2);
		}
		while (true) {
			int index = normalized.indexOf("/../");
			if (index < 0)
				break;
			if (index == 0)
				return null;
			int index2 = normalized.lastIndexOf(47, index - 1);
			normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
		}
		return normalized;
	}

	/**
	 * 获得HTTP请求的编码格式
	 * @param request HTTP请求
	 * @return 请求的编码格式
	 */
	private static String determineEncoding(HttpServletRequest request) {
		String enc = request.getCharacterEncoding();
		if (enc == null) {
			enc = "ISO-8859-1";
		}
		return enc;
	}

	/**
	 * 解码和清理URI字符串
	 * @param request HTTP请求
	 * @param uri URI字符串
	 * @return 处理后的URI字符串
	 */
	private static String decodeAndCleanUriString(HttpServletRequest request, String uri) {
		uri = decodeRequestString(request, uri);
		int semicolonIndex = uri.indexOf(';');
		return ((semicolonIndex != -1) ? uri.substring(0, semicolonIndex) : uri);
	}
}
