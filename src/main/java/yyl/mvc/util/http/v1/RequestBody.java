package yyl.mvc.util.http.v1;

import java.io.IOException;
import java.io.OutputStream;

public class RequestBody {
	// ==============================Fields========================================
	/** Body_Content */
	private final byte[] content;

	// ==============================Constructors===================================
	/** 构造函数 */
	protected RequestBody(Builder builder) {
		this.content = builder.content;
	}

	// ==============================Methods==========================================

	public void writeTo(OutputStream out) throws IOException {
		out.write(content);
		out.flush();
	}

	@Override
	public String toString() {
		return "RequestBody{length=" + content.length + "}";
	}

	// =================================InnerClasses===========================================
	public static class Builder {

		private byte[] content;

		public Builder() {
			content = new byte[0];
		}

		public Builder content(byte[] content) {
			if (content == null) {
				throw new NullPointerException("content == null");
			}
			this.content = content;
			return this;
		}

		public RequestBody build() {
			return new RequestBody(this);
		}
	}
}
