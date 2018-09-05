package yyl.mvc.core.plug.expection;

/**
 * 提示类异常
 * @author YYL
 */
@SuppressWarnings("serial")
public class GeneralException extends RuntimeException {
    
    private final ErrorCode code;
    private final String message;

    public GeneralException(ErrorCode code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public GeneralException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getCode() {
        return code;
    }
}

