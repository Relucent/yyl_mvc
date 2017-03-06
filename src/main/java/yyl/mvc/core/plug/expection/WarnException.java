package yyl.mvc.core.plug.expection;

/**
 * 警告类异常
 */
@SuppressWarnings("serial")
public class WarnException extends RuntimeException {
	public WarnException(String message) {
		super(message);
	}
}
