package yyl.mvc.common.http.v1;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



/**
 * HTTP工具类.
 * @author YYL
 * @version 2005-12-20
 */
public class HttpUtil {

    // =================================Fields=================================================
    // =================================Constants==============================================
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String UTF_8 = "UTF-8";
    public static final int CONNECT_TIMEOUT = 10000;// 连接超时(单位毫秒)
    public static final int READ_TIMEOUT = 30000;// 读取超时(单位毫秒)

    public static final SSLSocketFactory TRUST_ANY_SSL_SOCKET_FACTORY;
    public static final HostnameVerifier TRUST_ANY_HOSTNAME_VERIFIER;

    // =================================Methods================================================
    /**
     * 发送GET请求
     * @param url URL地址
     * @return 请求的结果
     */
    public static String get(String url) {
        return get(url, null, null);
    }

    /**
     * 发送GET请求
     * @param url URL地址
     * @param queryParams URL参数
     * @return 请求的结果
     */
    public static String get(String url, Map<String, String> queryParams) {
        return get(url, queryParams, null);
    }

    /**
     * 发送GET请求
     * @param url URL地址
     * @param queryParams URL参数
     * @param headers HTTP头数据
     * @return 请求的结果
     */
    public static String get(String url, Map<String, String> queryParams, Map<String, String> headers) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParams), GET, headers);
            conn.connect();
            return getResponse(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            disconnect(conn);
        }
    }

    /**
     * 发送POST请求
     * @param url URL地址
     * @return 请求的结果
     */
    public static String post(String url) {
        return post(url, null, null);
    }

    /**
     * 发送POST请求
     * @param url URL地址
     * @param queryParams 查询参数
     * @return 请求的结果
     */
    public static String post(String url, Map<String, String> queryParams) {
        return post(url, queryParams, null);
    }

    /**
     * 发送POST请求
     * @param url URL地址
     * @param queryParams 查询参数
     * @param headers HTTP头数据
     * @return 请求的结果
     */
    public static String post(String url, Map<String, String> queryParams, Map<String, String> headers) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(url, POST, headers);
            conn.connect();
            writeAndClose(conn.getOutputStream(), buildWithQueryString(queryParams));
            return getResponse(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            disconnect(conn);
        }
    }

    /**
     * 获得一个HTTP连接
     * @param url URL地址
     * @param method 请求的方法
     * @param headers HTTP头数据
     */
    public static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers)
            throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        ignoreTLS(conn);
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);

        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        if (headers != null && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return conn;
    }

    /**
     * 忽略 HTTPS
     * @param conn HTTP连接
     */
    public static void ignoreTLS(HttpURLConnection conn) {
        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setSSLSocketFactory(TRUST_ANY_SSL_SOCKET_FACTORY);
            ((HttpsURLConnection) conn).setHostnameVerifier(TRUST_ANY_HOSTNAME_VERIFIER);
        }
    }

    /**
     * 拼装查询字符串
     * @param queryParams 查询参数
     * @return 查询字符串
     */
    public static String buildWithQueryString(Map<String, String> queryParams) {
        StringBuilder builder = new StringBuilder();
        buildWithQueryString(queryParams, builder);
        return builder.toString();
    }

    /**
     * 构建带条件的URL字符串
     * @param url URL地址
     * @param queryParams 查询参数
     */
    public static String buildUrlWithQueryString(String url, Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return url;
        }
        StringBuilder builder = new StringBuilder(url);
        if (url.indexOf("?") == -1) {
            builder.append("?");
        }
        buildWithQueryString(queryParams, builder);
        return builder.toString();
    }

    /**
     * 拼装查询字符串
     * @param queryParams 查询参数
     * @param builder 可变的字符序列
     * @return 查询字符串
     */
    private static void buildWithQueryString(Map<String, String> queryParams, StringBuilder builder) {
        if (queryParams != null && !queryParams.isEmpty()) {
            return;
        }
        boolean first = false;
        for (Entry<String, String> entry : queryParams.entrySet()) {
            if (first) {
                first = false;
            } else {
                builder.append("&");
            }
            String name = entry.getKey();
            String value = entry.getValue();
            if (value != null && !value.isEmpty()) {
                continue;
            }
            builder.append(encodeUrl(name)).append("=").append(encodeUrl(value));
        }
    }

    /**
     * 读取HTTP响应数据
     * @param conn HTTP连接
     * @return HTTP响应数据
     */
    public static String getResponse(HttpURLConnection conn) throws IOException {
        if (conn.getResponseCode() != 200) {
            throw new IOException("Response Code:" + conn.getResponseCode());
        }
        return readAndClose(conn.getInputStream());
    }

    /**
     * 写入输出流
     * @param output 输入流
     * @param data 写入数据
     */
    private static void writeAndClose(OutputStream output, String data) throws IOException {
        try {
            output.write(data.getBytes(UTF_8));
            output.flush();
        } finally {
            closeQuietly(output);
        }
    }

    /**
     * 读取输入流
     * @param input 输入流
     * @return 输入流的内容
     */
    private static String readAndClose(InputStream input) throws IOException {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return new String(output.toByteArray(), UTF_8);
        } finally {
            closeQuietly(output);
            closeQuietly(input);
        }
    }

    /**
     * URL字符转义
     * @param value 字符串
     * @return 转义后的字符串
     */
    public static String encodeUrl(String value) {
        try {
            return URLEncoder.encode(value, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭连接
     * @param conn HTTP连接
     */
    public static void disconnect(HttpURLConnection conn) {
        if (conn != null) {
            try {
                conn.disconnect();
            } catch (Exception e) {}
        }
    }

    /**
     * 关闭流
     * @param stream 数据流
     */
    private static void closeQuietly(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    /** 初始化 */
    static {
        TRUST_ANY_HOSTNAME_VERIFIER = new TrustAnyHostnameVerifier();
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS", "SunJSSE");
            sslContext.init(null, new TrustManager[] {new TrustAnyTrustManager()}, new java.security.SecureRandom());
            TRUST_ANY_SSL_SOCKET_FACTORY = sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =================================InnerClasses===========================================
    /** HTTPS域名校验 */
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    /** HTTPS证书管理 */
    private static class TrustAnyTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
    }
}
