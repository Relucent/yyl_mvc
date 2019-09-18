package yyl.mvc.plugin.expection;

/**
 * 提示类异常
 * @author YYL
 */
@SuppressWarnings("serial")
public class GeneralException extends RuntimeException {
    
    private final ErrorType type;
    private final String message;

    public GeneralException(ErrorType type, String message) {
        super(message);
        this.type = type;  this.message = message;
    }

    public GeneralException(ErrorType type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;  this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ErrorType getType() {
        return type;
    }
}

