package yyl.mvc.plugin.spring.view;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/** HTML视图 */
public class HtmlView extends AbstractUrlBasedView {

	// ==============================Fields===========================================
	private Resource resource;

	// ==============================Methods==========================================
	@Override
	protected void renderMergedOutputModel(Map<String, Object> paramMap, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		InputStream input = null;
		try {
			IOUtils.copy(input = resource.getInputStream(), response.getOutputStream());
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		return super.checkResource(locale) && resource.isReadable();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		this.resource = getApplicationContext().getResource(getUrl());
	}
}
