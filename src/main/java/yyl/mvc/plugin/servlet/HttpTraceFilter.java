package yyl.mvc.plugin.servlet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.databind.ObjectMapper;


// @WebFilter(urlPatterns = "/*", filterName = "traceIdFilter")
public class HttpTraceFilter extends OncePerRequestFilter implements Ordered {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected static final String NEED_TRACE_PATH_PREFIX = "/api";
    protected static final String IGNORE_CONTENT_TYPE = "multipart/form-data";

    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 8;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!isRequestValid(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
            status = response.getStatus();
        } finally {
            String path = request.getRequestURI();
            if (path.startsWith(NEED_TRACE_PATH_PREFIX)
                    && !Objects.equals(IGNORE_CONTENT_TYPE, request.getContentType())) {

                // 1. 璁板綍鏃ュ織
                HttpTraceLog traceLog = new HttpTraceLog();
                traceLog.path = path;
                traceLog.method = request.getMethod();
                long latency = System.currentTimeMillis() - startTime;
                traceLog.timeTaken = latency;
                traceLog.time = LocalDateTime.now().toString();
                traceLog.parameterMap = toJson(request.getParameterMap());
                traceLog.status = status;
                traceLog.requestBody = getRequestBody(request);
                traceLog.responseBody = getResponseBody(response);

                logger.info("HTTP TRACE:" + toJson(traceLog));
            }
            updateResponse(response);
        }
    }

    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    private String getRequestBody(HttpServletRequest request) {
        String requestBody = "";
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            try {
                requestBody = IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            } catch (IOException e) {
                // NOOP
            }
        }
        return requestBody;
    }

    private String getResponseBody(HttpServletResponse response) {
        String responseBody = "";
        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            try {
                responseBody = IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            } catch (IOException e) {
                // NOOP
            }
        }
        return responseBody;
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }

    private String toJson(Object o) throws IOException {
        return new ObjectMapper().writeValueAsString(o);
    }

    protected static class HttpTraceLog {
        public String path;
        public String parameterMap;
        public String method;
        public Long timeTaken;
        public String time;
        public Integer status;
        public String requestBody;
        public String responseBody;
    }
}
