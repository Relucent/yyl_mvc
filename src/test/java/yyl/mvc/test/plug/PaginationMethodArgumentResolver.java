package yyl.mvc.test.plug;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import yyl.mvc.core.util.collect.Mapx;
import yyl.mvc.core.util.collect.Mapxs;
import yyl.mvc.core.util.convert.ConvertUtil;
import yyl.mvc.core.util.page.Pagination;
import yyl.mvc.core.util.page.impl.SimplePagination;

/**
 * 分页视图适配器<br>
 */
public class PaginationMethodArgumentResolver implements HandlerMethodArgumentResolver {

	// ==============================Fields===========================================
	private static final String START_KEY = ":start";
	private static final String LIMIT_KEY = ":limit";
	private static final int DEFAULT_LIMIT = 10;

	// ==============================Methods==========================================

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return Pagination.class.equals(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {

		int start = ConvertUtil.toInteger(webRequest.getParameter(START_KEY), 0);
		int limit = ConvertUtil.toInteger(webRequest.getParameter(LIMIT_KEY), DEFAULT_LIMIT);

		Mapx filters = Mapxs.newMapx();
		for (Map.Entry<String, String[]> entry : webRequest.getParameterMap().entrySet()) {
			String name = entry.getKey();
			String[] values = entry.getValue();
			if (StringUtils.equals(name, START_KEY) || StringUtils.equals(name, START_KEY)) {
				continue;
			}
			if (ArrayUtils.isNotEmpty(values)) {
				filters.put(name, values[0]);
			}
		}

		return new SimplePagination(start, limit, filters);
	}

}
