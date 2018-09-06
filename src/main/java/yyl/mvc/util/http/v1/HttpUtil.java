package yyl.mvc.util.http.v1;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

/**
 * HTTP工具类.
 * @author YaoYiLang
 * @version 2014-12-06 09:30
 */
public class HttpUtil {

    // =================================Fields=================================================
    // =================================Constants==============================================
    public static final String GET = "GET";
    public static final String PUT = "PUT";
    public static final String HEAD = "HEAD";
    public static final String POST = "POST";
    public static final String PATCH = "PATCH";
    public static final String DELETE = "DELETE";

    public static final String UTF_8 = "UTF-8";
    public static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");// UTF-8 字符集

    public static final int DEFAULT_CONNECT_TIMEOUT = 15000;// 连接超时(单位毫秒)
    public static final int DEFAULT_READ_TIMEOUT = 30000;// 读取超时(单位毫秒)

    // =================================Methods================================================
    /**
     * 发送GET请求
     * @param url
     * @return 请求的结果
     */
    public static Response get(String url) {
        return get(url, null, null);
    }

    /**
     * 发送GET请求
     * @param url
     * @param queryParams URL参数
     * @return 请求的结果
     */
    public static Response get(String url, Map<String, String> queryParams) {
        return get(url, queryParams, null);
    }

    /**
     * 发送GET请求
     * @param url
     * @param queryParams URL参数
     * @param headers HTTP头数据
     * @return 请求的结果
     */
    public static Response get(String url, Map<String, String> queryParams, Map<String, String> headers) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParams), GET, headers);
            conn.connect();
            return getHttpResponse(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(conn);
        }
    }

    /**
     * 发送POST请求
     * @param url
     * @param data 提交数据
     * @return 请求的结果
     */
    public static Response post(String url, byte[] data) {
        return post(url, null, data, null);
    }

    /**
     * 发送POST请求
     * @param url
     * @param queryParams URL参数
     * @param data 提交数据
     * @return 请求的结果
     */
    public static Response post(String url, Map<String, String> queryParams, byte[] data) {
        return post(url, queryParams, data, null);
    }

    /**
     * 发送POST请求
     * @param url
     * @param data 提交数据
     * @param headers HTTP头数据
     * @return 请求的结果
     */
    public static Response post(String url, byte[] data, Map<String, String> headers) {
        return post(url, null, data, headers);
    }

    /**
     * 发送POST请求
     * @param url
     * @param queryParams URL参数
     * @param data 提交数据
     * @param headers HTTP头数据
     * @return 请求的结果
     */
    public static Response post(String url, Map<String, String> queryParams, byte[] data, Map<String, String> headers) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParams), POST, headers);
            conn.connect();
            OutputStream out = null;
            try {
                out = conn.getOutputStream();
                out.write(data == null ? new byte[0] : data);
                out.flush();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            return getHttpResponse(conn);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeQuietly(conn);
        }
    }

    /**
     * 获得一个HTTP连接
     * @param url URL
     * @param method 请求的方法
     * @throws IOException
     */
    public static HttpURLConnection getHttpConnection(String url, String method) throws IOException {
        return getHttpConnection(url, method, null);
    }

    /**
     * 获得一个HTTP连接
     * @param url URL
     * @param method 请求的方法
     * @param headers HTTP头数据
     */
    public static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers)
            throws IOException {
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setSSLSocketFactory(TrustAnyTrustManager.getInsecureSSLSocketFactory());
            ((HttpsURLConnection) conn).setHostnameVerifier(TrustAnyHostnameVerifier.getInsecureVerifier());
        }
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        conn.setReadTimeout(DEFAULT_READ_TIMEOUT);

        // if (headers == null || !headers.containsKey("Content-Type")) {
        // conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;
        // charset=UTF-8");
        // }
        // if (headers == null || !headers.containsKey("User-Agent")) {
        // conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64)
        // AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        // }

        if (headers != null && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return conn;
    }

    /**
     * 设置HTTP请求头
     * @param conn HTTP连接
     * @param headers HTTP请求头
     */
    public static void setRequestHeaders(HttpURLConnection conn, Map<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String name = entry.getKey();
            List<String> values = entry.getValue();
            for (String value : values) {
                conn.setRequestProperty(name, value);
            }
        }
    }

    /**
     * 写入请求主体数据
     * @param conn HTTP连接
     * @param body 请求主体数据
     * @throws IOException 发生IO异常时候
     */
    public static void setRequestBody(HttpURLConnection conn, byte[] body) throws IOException {
        OutputStream out = null;
        try {
            out = conn.getOutputStream();
            out.write(body == null ? new byte[0] : body);
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 构建带条件的URL字符串
     * @param url URL地址
     * @param query 查询条件
     */
    private static String buildUrlWithQueryString(String url, Map<String, String> query) {
        if (query == null || query.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        boolean first;
        if (url.indexOf("?") == -1) {
            first = true;
            sb.append("?");
        } else {
            first = false;
        }
        for (Entry<String, String> entry : query.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            String key = entry.getKey();
            String value = entry.getValue();
            if (value != null && !value.isEmpty()) try {
                value = URLEncoder.encode(value, UTF_8);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            sb.append(key).append("=").append(value);
        }
        return sb.toString();
    }

    /**
     * 关闭连接
     * @param conn HTTP连接
     */
    public static void closeQuietly(HttpURLConnection conn) {
        if (conn != null) {
            try {
                conn.disconnect();
            } catch (Exception e) {}
        }
    }

    /**
     * 静默方式关闭连接(忽略异常)
     * @param closeable 可以关闭的对象
     */
    private static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {}
    }

    /**
     * 读取HTTP响应数据
     * @param conn HTTP连接
     * @return HTTP响应数据
     */
    public static Response getHttpResponse(HttpURLConnection conn) {
        Response.Builder builder = new Response.Builder();
        try {
            builder.code(conn.getResponseCode());
            builder.message(conn.getResponseMessage());
            for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
                String name = entry.getKey();
                for (String value : entry.getValue()) {
                    builder.addHeader(name, value);
                }
            }
            InputStream input = null;
            try {
                builder.body(toByteArray(input = conn.getInputStream()));
            } finally {
                closeQuietly(input);
            }
        } catch (Exception e) {
            try {
                builder.code(conn.getResponseCode());
                builder.message(conn.getResponseMessage());
                InputStream input = null;
                try {
                    builder.body(toByteArray(input = conn.getErrorStream()));
                } finally {
                    closeQuietly(input);
                }
            } catch (Exception t) {
                throw new RuntimeException(t);
            }
        }
        return builder.build();
    }

    /**
     * 转换输入流为字节数组
     * @param input 输入流
     * @return 输入流的内容(字节数组)
     * @throws IOException
     */
    private static byte[] toByteArray(InputStream input) throws IOException {
        if (input == null) {
            return null;
        } else {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        }
    }

    // header("Proxy-Authorization", "Basic " + Base64.encodeBase64String((proxy.username() + ":" +
    // proxy.password()).getBytes()));
}
