package yyl.mvc.common.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import yyl.mvc.common.collection.Mapx;
import yyl.mvc.common.json.JsonUtil;
import yyl.mvc.common.logging.Logger;

/**
 * 控制器工具类(主要用于支持AJAX)<br>
 * @author _yyl
 */
public class ControllerUtil {

	// ===================================Fields==============================================
	/** 记录日志的对象. */
	private static final Logger LOGGER = Logger.getLogger(ControllerUtil.class);

	// ===================================Methods=============================================
	/**
	 * 从request读取数据,并将数据封装成MAP格式
	 * @param request HTTP请求
	 * @return 请求的参数
	 */
	public static Mapx parameters(HttpServletRequest request) {
		Mapx params = new Mapx();
		for (Enumeration<String> en = request.getParameterNames(); en.hasMoreElements();) {
			String name = en.nextElement();
			String value = request.getParameter(name);
			params.put(name, value);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("params:" + params);
		}
		return params;
	}

	/**
	 * 从request body读取数据
	 * @param request HTTP请求
	 * @return request_body 数据
	 */
	public static String streamAsString(HttpServletRequest request) {
		InputStream input = null;
		String text = null;
		try {
			text = IOUtils.toString(input = request.getInputStream(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(input);
		}
		return text;
	}

	/**
	 * 返回数据
	 * @param src 返回的数据
	 * @param response HTTP响应
	 */
	public static void write(Object src, HttpServletResponse response) {
		try {
			if (src instanceof Throwable) {
				src = wrapException((Throwable) src);
			}
			String json = JsonUtil.encode(src);
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			// response.setContentType("text/html; charset=UTF-8");
			response.setContentType("application/json; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.addHeader("Access-Control-Allow-Origin", "*");
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("json:" + json);
			}
			try {
				response.getWriter().print(json);
			} catch (IOException e) {
				LOGGER.debug("!", e);
			}
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
	}

	/**
	 * 返回JSONP数据
	 * @param src 返回的数据
	 * @param request HTTP请求
	 * @param response HTTP响应
	 */
	public static void writeJsonp(Object src, HttpServletRequest request, HttpServletResponse response) {
		String callback = StringUtils.EMPTY;
		if ("GET".equalsIgnoreCase(request.getMethod())) {
			callback = StringUtils.defaultString(request.getParameter("callback"));// JSONP
		}
		try {
			if (src instanceof Throwable) {
				src = wrapException((Throwable) src);
			}
			String json = JsonUtil.encode(src);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("callback:" + callback + " json:" + json);
			}
			response.setHeader("Pragma", "no-cache"); // HTTP/1.0
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/javascript;charset=UTF-8");
			PrintWriter writer = response.getWriter();
			writer.append(callback);
			writer.append("(");
			writer.append(json);
			writer.append(")");
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
	}

	/**
	 * 包装异常
	 * @param e 异常信息
	 * @return 包装后的结果
	 */
	protected static Mapx wrapException(Throwable e) {
		Mapx error = new Mapx();
		String message = e.getMessage();
		if (message == null) {
			message = e.toString();
		}
		error.put("message", message);
		error.put("@failure", Boolean.TRUE);// failure|success
		return error;
	}
}
