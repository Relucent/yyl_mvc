package yyl.mvc.plug.expection;

/**
 * 异常工具类
 * @author YYL
 */
public class ExceptionHelper {

    /**
     * 创建通用异常
     * @param cause 源异常
     * @return 通用异常
     */
    public static GeneralException propagate(Throwable cause) {
        if (cause instanceof GeneralException) {
            return (GeneralException) cause;
        }
        return new GeneralException(ErrorCode.UNKNOWN, "#", cause);
    }

    /**
     * 创建未捕获异常
     * @param cause
     * @param cause 源异常
     * @return 未捕获异常
     */
    public static GeneralException propagate(String message, Throwable cause) {
        return new GeneralException(ErrorCode.UNKNOWN, message, cause);
    }

    /**
     * 创建提示类异常
     * @param message 异常信息
     * @return 提示类异常
     */
    public static GeneralException prompt(String message) {
        return new GeneralException(ErrorCode.PROMPT, message);
    }

}
