package yyl.mvc.core.plug.spring.view;

import org.springframework.web.servlet.view.UrlBasedViewResolver;

/** HTML视图解析器 */
public class HtmlViewResolver extends UrlBasedViewResolver {

	// ==============================Fields===========================================
	//...

	// ==============================Constructors=====================================
	public HtmlViewResolver() {
		super();
		setPrefix("");
		setSuffix(".html");
		setOrder(2147483647);
		setCache(false);
		setViewClass(requiredViewClass());
	}

	// ==============================Methods==========================================
	@Override
	protected Class<?> requiredViewClass() {
		return HtmlView.class;
	}
}
