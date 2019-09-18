package yyl.mvc.plugin.spring.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 上下文信息持有者
 * @author YYL
 */
public class WebContextHolder {

    /**
     * 获得HTTP会话
     * @return HTTP会话
     */
    public static HttpSession getSession () {
        HttpServletRequest request = getRequest();
        return request != null ? request .getSession() : null;
    }
    
    /**
     * 获得HTTP请求
     * @return HTTP请求
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 获得 ServletRequestAttributes
     * @return ServletRequestAttributes
     */
    private static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
}
