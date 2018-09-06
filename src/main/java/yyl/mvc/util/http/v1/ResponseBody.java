package yyl.mvc.util.http.v1;

public class ResponseBody {
    // ==============================Fields========================================
    /** Body_Content */
    private final byte[] content;

    // ==============================Constructors===================================
    /** 构造函数 */
    protected ResponseBody(Builder builder) {
        this.content = builder.content;
    }

    // ==============================Methods==========================================
    public long contentLength() {
        return content.length;
    }

    public byte[] bytes() {
        return content;
    }

    /**
     * 获得响应的字符串内容(使用UTF-8字符集)
     * @return 响应的内容(字符串形式)
     */
    public String string() {
        return new String(content, HttpUtil.UTF_8_CHARSET);
    }

    @Override
    public String toString() {
        return "ResponseBody{length=" + content.length + "}";
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

        public ResponseBody build() {
            return new ResponseBody(this);
        }
    }
}
