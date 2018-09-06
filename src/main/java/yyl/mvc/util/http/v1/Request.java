package yyl.mvc.util.http.v1;

public class Request {
	// ==============================Fields========================================
	/** Http_Url */
	private final String url;
	/** Http_Header */
	private final Headers headers;
	/** Http_Body */
	private final RequestBody body;
	/** Http_Method */
	private final String method;// GET|POST

	// ==============================Constructors===================================
	/** 构造函数 */
	protected Request(Builder builder) {
		this.url = builder.url;
		this.method = builder.method;
		this.headers = builder.headers.build();
		this.body = builder.body;
	}

	// ==============================Methods==========================================
	public String url() {
		return url;
	}

	public String header(String name) {
		return headers.get(name);
	}

	public Headers headers() {
		return this.headers;
	}

	public String method() {
		return this.method;
	}

	protected RequestBody body() {
		return this.body;
	}

	public Builder newBuilder() {
		return new Builder(this);
	}

	@Override
	public String toString() {
		return "Request{method=" + method + ", url=" + url + "}";
	}

	// =================================InnerClasses===========================================
	public static class Builder {
		private String url;
		private String method;
		private Headers.Builder headers;
		private RequestBody body;

		public Builder() {
			this.method = HttpUtil.GET;
			this.headers = new Headers.Builder();
		}

		private Builder(Request request) {
			this.url = request.url;
			this.method = request.method;
			this.headers = request.headers.newBuilder();
			this.body = request.body;
		}

		public Builder url(String url) {
			if (url == null) {
				throw new NullPointerException("url == null");
			}
			this.url = url;
			return this;
		}

		public Builder header(String name, String value) {
			headers.set(name, value);
			return this;
		}

		public Builder addHeader(String name, String value) {
			headers.add(name, value);
			return this;
		}

		public Builder removeHeader(String name) {
			headers.remove(name);
			return this;
		}

		public Builder get() {
			return method(HttpUtil.GET, null);
		}

		public Builder put(RequestBody body) {
			return method(HttpUtil.PUT, body);
		}

		public Builder head() {
			return method(HttpUtil.HEAD, null);
		}

		public Builder post(RequestBody body) {
			return method(HttpUtil.POST, body);
		}

		public Builder patch(RequestBody body) {
			return method(HttpUtil.PATCH, body);
		}

		public Builder delete(RequestBody body) {
			return method(HttpUtil.DELETE, body);
		}

		public Builder delete() {
			return delete(new RequestBody.Builder().build());
		}

		public Builder method(String method, RequestBody body) {
			if (method == null) {
				throw new NullPointerException("method == null");
			}
			if (method.length() == 0) {
				throw new IllegalArgumentException("method.length() == 0");
			}
			this.method = method;
			this.body = body;
			return this;
		}

		public Request build() {
			if (url == null) {
				throw new IllegalStateException("url == null");
			}
			return new Request(this);
		}
	}
}
