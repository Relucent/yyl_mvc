package yyl.mvc.util.http.v1;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class HttpClient {
	// =================================Fields=================================================
	private final Proxy proxy;//代理
	private final int connectTimeout;// 连接超时(单位毫秒)
	private final int readTimeout;// 读取超时(单位毫秒)
	private final boolean followRedirects;//是否应该自动执行 HTTP重定向(响应代码为 3xx的请求)
	private final HostnameVerifier hostnameVerifier;
	private final SSLSocketFactory sslSocketFactory;
	private final Interceptor[] interceptors;

	// =================================Constructors===========================================
	protected HttpClient(Builder builder) {
		this.proxy = builder.proxy;
		this.connectTimeout = builder.connectTimeout;
		this.readTimeout = builder.readTimeout;
		this.followRedirects = builder.followRedirects;
		this.hostnameVerifier = builder.hostnameVerifier;
		this.sslSocketFactory = builder.sslSocketFactory;
		this.interceptors = builder.interceptors.toArray(new Interceptor[builder.interceptors.size()]);
	}

	// =================================Methods================================================
	public Proxy proxy() {
		return proxy;
	}

	public int connectTimeoutMillis() {
		return connectTimeout;
	}

	public int readTimeoutMillis() {
		return readTimeout;
	}

	public SSLSocketFactory sslSocketFactory() {
		return sslSocketFactory;
	}

	public HostnameVerifier hostnameVerifier() {
		return hostnameVerifier;
	}

	/**
	 * 执行请求
	 * @param request HTTP请求
	 * @return HTTP响应
	 */
	public Response execute(Request request) {
		try {
			return interceptors.length == 0 ? doExecute(request) : new InterceptorChain(0, request).proceed(request);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private Response doExecute(Request request) throws IOException {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(request.url());
			conn = (HttpURLConnection) (proxy == null ? url.openConnection() : url.openConnection(proxy));
			conn.setRequestMethod(request.method());
			if (conn instanceof HttpsURLConnection) {
				((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory());
				((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier());
			}
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setDoInput(true);// Allow Input
			conn.setDoOutput(true);// Allow Output
			conn.setInstanceFollowRedirects(followRedirects);
			HttpUtil.setRequestHeaders(conn, request.headers().toMultimap());
			conn.connect();
			if (request.body() != null) {
				OutputStream out = null;
				try {
					request.body().writeTo(out = conn.getOutputStream());
				} finally {
					if (out != null) {
						out.close();
					}
				}
			}
			return HttpUtil.getHttpResponse(conn);
		} finally {
			HttpUtil.closeQuietly(conn);
		}
	}

	// =================================InnerClasses===========================================
	public static class Builder {
		private Proxy proxy;
		private int connectTimeout = 15000;// 连接超时(单位毫秒)
		private int readTimeout = 30000;// 读取超时(单位毫秒)
		private boolean followRedirects = false;
		private HostnameVerifier hostnameVerifier;
		private SSLSocketFactory sslSocketFactory;
		private List<Interceptor> interceptors;

		public Builder() {
			hostnameVerifier = TrustAnyHostnameVerifier.getInsecureVerifier();
			sslSocketFactory = TrustAnyTrustManager.getInsecureSSLSocketFactory();
			interceptors = new ArrayList<Interceptor>();
		}

		public Builder proxy(Proxy proxy) {
			this.proxy = proxy;
			return this;
		}

		public Builder connectTimeout(long timeout, TimeUnit unit) {
			this.connectTimeout = checkAndToMillis(timeout, unit);
			return this;
		}

		public Builder readTimeout(long timeout, TimeUnit unit) {
			this.readTimeout = checkAndToMillis(timeout, unit);
			return this;
		}

		public Builder followRedirects(boolean followRedirects) {
			this.followRedirects = followRedirects;
			return this;
		}

		public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
			if (hostnameVerifier == null) {
				throw new NullPointerException("hostnameVerifier == null");
			}
			this.hostnameVerifier = hostnameVerifier;
			return this;
		}

		public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
			if (sslSocketFactory == null) {
				throw new NullPointerException("sslSocketFactory == null");
			}
			this.sslSocketFactory = sslSocketFactory;
			return this;
		}

		public Builder addInterceptor(Interceptor interceptor) {
			if (interceptor == null) {
				throw new NullPointerException("interceptor == null");
			}
			this.interceptors.add(interceptor);
			return this;
		}

		private static int checkAndToMillis(long timeout, TimeUnit unit) {
			if (timeout < 0L)
				throw new IllegalArgumentException("timeout < 0");
			if (unit == null)
				throw new NullPointerException("unit == null");
			long millis = unit.toMillis(timeout);
			if (millis > Integer.MAX_VALUE/* 2147483647L */)
				throw new IllegalArgumentException("Timeout too large.");
			if ((millis == 0L) && (timeout > 0L))
				throw new IllegalArgumentException("Timeout too small.");
			return (int) millis;
		}

		public HttpClient build() {
			return new HttpClient(this);
		}
	}

	/**
	 * 过滤器链
	 */
	private final class InterceptorChain implements Interceptor.Chain {
		private final int index;
		private final Request request;

		InterceptorChain(int index, Request request) {
			this.index = index;
			this.request = request;
		}

		@Override
		public Request request() {
			return request;
		}

		@Override
		public Response proceed(Request request) throws IOException {
			if (index < interceptors.length) {
				InterceptorChain chain = new InterceptorChain(index + 1, request);
				Response response = interceptors[index].intercept(chain);
				if (response == null) {
					throw new NullPointerException(interceptors[index] + " returned null");
				}
				return response;
			}
			return doExecute(request);
		}
	}
}
