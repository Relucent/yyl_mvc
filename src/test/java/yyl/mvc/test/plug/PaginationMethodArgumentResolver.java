package yyl.mvc.test.plug;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import yyl.mvc.core.util.convert.ConvertUtil;
import yyl.mvc.core.util.page.Pagination;

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
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        int start = ConvertUtil.toInteger(webRequest.getParameter(START_KEY), 0);
        int limit = ConvertUtil.toInteger(webRequest.getParameter(LIMIT_KEY), DEFAULT_LIMIT);
        return new Pagination(start, limit);
    }

}
