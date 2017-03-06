package yyl.mvc.core.plug.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Servlet封装处理<br>
 * @author YYL
 */
@SuppressWarnings("serial")
public class SpringDispatcherServlet extends DispatcherServlet {
	/**
	 * 用于往ServletWebRequest里放response对像
	 * @param request
	 * @param response
	 * @param previousAttributes
	 * @return ServletRequestAttributes
	 */
	protected ServletRequestAttributes buildRequestAttributes(HttpServletRequest request, HttpServletResponse response,
			RequestAttributes previousAttributes) {
		if (previousAttributes == null || previousAttributes instanceof ServletRequestAttributes) {
			return new ServletWebRequest(request, response);// ServletRequestAttributes
		} else {
			return null;
		}
	}

	/**
	 * 调用父类的处理
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.service(request, response);
	}
}
