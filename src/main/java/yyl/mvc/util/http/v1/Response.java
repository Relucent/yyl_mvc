package yyl.mvc.util.http.v1;

/**
 * HTTP响应结果
 */
public class Response {
	// ==============================Fields========================================
	/** Response_Code */
	private final int code;
	/** Response_Message */
	private final String message;
	/** Response_Headers */
	private final Headers headers;
	/** Response_Body */
	private final ResponseBody body;

	// ==============================Constructors=====================================
	/** 构造函数 */
	protected Response(Builder builder) {
		this.code = builder.code;
		this.message = builder.message;
		this.headers = builder.headers.build();
		this.body = builder.body;
	}

	// ==============================Methods==========================================
	/**
	 * 获得响应状态码
	 * @return 响应状态码
	 */
	public int code() {
		return code;
	}

	/**
	 * 获得HTTP响应信息
	 * @return 响应信息
	 */
	public String message() {
		return message;
	}

	/**
	 * 获得响应头信息
	 * @param name 头信息NAME
	 * @return 头信息
	 */
	public String header(String name) {
		return headers.get(name);
	}

	/**
	 * 获得响应头信息
	 * @return 头信息
	 */
	public Headers headers() {
		return headers;
	}

	/**
	 * 获得响应的内容
	 * @return 响应的内容
	 */
	public ResponseBody body() {
		return body;
	}

	/**
	 * 是否成功的响应
	 * @return 是否是成功的响应
	 */
	public boolean isSuccessful() {
		return ((this.code >= 200) && (this.code < 300));
	}

	/**
	 * 是否重定向
	 * @return 是否重定向
	 */
	public boolean isRedirect() {
		switch (this.code) {
		case 300:
		case 301:
		case 302:
		case 303:
		case 307:
		case 308:
			return true;
		case 304:
		case 305:
		case 306:
		default:
			return false;
		}
	}

	public Builder newBuilder() {
		return new Builder(this);
	}

	/** 对象的字符串 */
	@Override
	public String toString() {
		return "Response{ code=" + code + ", message=" + message + ", contentLength=" + body.contentLength() + "}";
	}

	// =================================InnerClasses===========================================
	public static class Builder {
		private int code = -1;
		private String message;
		private Headers.Builder headers;
		private ResponseBody body;

		public Builder() {
			this.headers = new Headers.Builder();
		}

		public Builder(Response response) {
			this.code = response.code;
			this.message = response.message;
			this.headers = response.headers.newBuilder();
			this.body = response.body;
		}

		public Builder code(int code) {
			this.code = code;
			return this;
		}

		public Builder message(String message) {
			this.message = message;
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

		public Builder body(ResponseBody body) {
			this.body = body;
			return this;
		}

		public Builder body(byte[] body) {
			this.body = new ResponseBody.Builder().content(body).build();
			return this;
		}

		public Response build() {
			if (code < 0)
				throw new IllegalStateException("code < 0: " + code);
			return new Response(this);
		}
	}
}