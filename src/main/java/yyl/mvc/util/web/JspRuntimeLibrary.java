package yyl.mvc.util.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;

public class JspRuntimeLibrary {

	public static Throwable getThrowable(ServletRequest request) {
		Throwable error = (Throwable) request.getAttribute("javax.servlet.error.exception");
		if (error == null) {
			error = (Throwable) request.getAttribute("javax.servlet.jsp.jspException");
			if (error != null) {
				request.setAttribute("javax.servlet.error.exception", error);
			}
		}
		return error;
	}

	public static String getContextRelativePath(ServletRequest request, String relativePath) {
		if (relativePath.startsWith("/"))
			return relativePath;
		if (!(request instanceof HttpServletRequest))
			return relativePath;
		HttpServletRequest hrequest = (HttpServletRequest) request;
		String uri = (String) request.getAttribute("javax.servlet.include.servlet_path");
		if (uri != null) {
			String pathInfo = (String) request.getAttribute("javax.servlet.include.path_info");
			if ((pathInfo == null) && (uri.lastIndexOf('/') >= 0))
				uri = uri.substring(0, uri.lastIndexOf('/'));
		} else {
			uri = hrequest.getServletPath();
			if (uri.lastIndexOf('/') >= 0)
				uri = uri.substring(0, uri.lastIndexOf('/'));
		}
		return new StringBuilder().append(uri).append('/').append(relativePath).toString();
	}

	/**
	 * JspRuntimeLibrary.include(request, response, "include.jsp", out, false);<br>
	 * 
	 * <pre>
	 * <jsp:include page="include.jsp" />
	 * </pre>
	 */
	public static void include(ServletRequest request, ServletResponse response, String relativePath, JspWriter out, boolean flush)
			throws IOException, ServletException {
		if ((flush) && (!(out instanceof BodyContent))) {
			out.flush();
		}
		String resourcePath = getContextRelativePath(request, relativePath);
		RequestDispatcher rd = request.getRequestDispatcher(resourcePath);
		rd.include(request, new ServletResponseWrapperInclude(response, out));
	}

	private static class ServletResponseWrapperInclude extends HttpServletResponseWrapper {
		private PrintWriter printWriter;
		private JspWriter jspWriter;

		public ServletResponseWrapperInclude(ServletResponse response, JspWriter jspWriter) {
			super((HttpServletResponse) response);
			this.printWriter = new PrintWriter(jspWriter);
			this.jspWriter = jspWriter;
		}

		public PrintWriter getWriter() throws IOException {
			return this.printWriter;
		}

		public ServletOutputStream getOutputStream() throws IOException {
			throw new IllegalStateException();
		}

		public void resetBuffer() {
			try {
				this.jspWriter.clearBuffer();
			} catch (IOException ioe) {
			}
		}
	}
}
