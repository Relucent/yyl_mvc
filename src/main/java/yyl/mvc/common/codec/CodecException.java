package yyl.mvc.common.codec;

/**
 * 编码解码异常
 */
@SuppressWarnings("serial")
public class CodecException extends RuntimeException {

    public CodecException(Throwable e) {
        super(e.toString(), e);
    }

    public CodecException(String message) {
        super(message);
    }

    public CodecException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
