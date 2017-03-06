package yyl.mvc.core.plug.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认处理_Servlet
 */
@SuppressWarnings("serial")
public class DefaultServlet extends HttpServlet {
	// ==============================Fields===========================================
	private ServletContext context;
	private String defaultServletName;

	// ==============================Constructors=====================================
	/**
	 * 构造函数
	 */
	public DefaultServlet() {
	}

	// ==============================Methods==========================================
	/**
	 * POST请求处理
	 * @param request HTTP请求
	 * @param response HTTP响应
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)//
			throws ServletException, IOException {
		handleDefaultRequest(request, response);
	}

	/**
	 * GET请求处理
	 * @param request HTTP请求
	 * @param response HTTP响应
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)//
			throws ServletException, IOException {
		handleDefaultRequest(request, response);
	}

	// ==============================ToolMethods======================================
	/**
	 * 执行默认处理
	 * @param request HTTP请求
	 * @param response HTTP响应
	 */
	protected void handleDefaultRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = context.getNamedDispatcher(defaultServletName);
		if (rd == null) {
			throw new IllegalStateException(
					"A RequestDispatcher could not be located for the default servlet '" + defaultServletName + "'");
		}
		rd.forward(request, response);
	}

	// ==============================OverrideMethods==================================
	/**
	 * 初始化(此处主要用来获得项目路径)
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		context = getServletContext();

		if (context.getNamedDispatcher("default") != null) {
			defaultServletName = "default";
		} else if (context.getNamedDispatcher("_ah_default") != null) {
			defaultServletName = "_ah_default";
		} else if (context.getNamedDispatcher("resin-file") != null) {
			defaultServletName = "resin-file";
		} else if (context.getNamedDispatcher("FileServlet") != null) {
			defaultServletName = "FileServlet";
		} else if (context.getNamedDispatcher("SimpleFileServlet") != null) {
			defaultServletName = "SimpleFileServlet";
		} else {
			throw new IllegalStateException("Unable to locate the default servlet for serving static content.");
		}
		String webPath = context.getRealPath("");
		if (context.getServletContextName() != null) {
			System.out.println("##### Initialization Web App(" + context.getServletContextName() + ") Environment #####");
		}
		System.out.println("web.path=" + webPath);
	}
}
