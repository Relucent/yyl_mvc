package yyl.mvc.core.util.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类<br>
 * @author _yyl
 */
public class CookieUtil {

	/**
	 * 设置HttpOnly Cookie
	 * @param response HTTP响应
	 * @param cookie Cookie对象
	 */
	public static void addHttpOnlyCookie(HttpServletResponse response, Cookie cookie) {
		String name = cookie.getName();// Cookie名称
		String value = cookie.getValue();// Cookie值
		int maxAge = cookie.getMaxAge();// 最大生存时间(毫秒,0代表删除,-1代表与浏览器会话一致)
		String path = cookie.getPath();// 路径
		String domain = cookie.getDomain();// 域
		boolean isSecure = cookie.getSecure();// 是否为安全协议信息

		StringBuilder buffer = new StringBuilder();

		buffer.append(name + "=" + value + ";");

		if (maxAge == 0) {
			buffer.append("Expires=Thu Jan 01 08:00:00 CST 1970;");
		} else if (maxAge > 0) {
			buffer.append("Max-Age=" + cookie.getMaxAge() + ";");
		}
		if (domain != null) {
			buffer.append("domain=" + domain + ";");
		}
		if (path != null) {
			buffer.append("path=" + path + ";");
		}
		if (isSecure) {
			buffer.append("secure;");
		}
		buffer.append("HTTPOnly;");

		response.addHeader("Set-Cookie", buffer.toString());
	}

	/**
	 * 根据名称获得对应的Cookie值
	 */
	public static String getCookieValue(String name, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 设置Cookie
	 */
	public static void setCookie(String name, String value, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(-1);// 关闭浏览器就失效
		cookie.setPath("/");// 跟路径
		cookie.setHttpOnly(true);// 设置HttpOnly
		response.addCookie(cookie);
	}

	/**
	 * 删除Cookie
	 */
	public static void removeCookie(String name, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);// 失效(时效为0)
		cookie.setPath("/");// 跟路径
		cookie.setHttpOnly(true);// 设置HttpOnly
		response.addCookie(cookie);
	}
}
