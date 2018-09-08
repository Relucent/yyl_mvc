package yyl.mvc.plug.expection;

/**
 * 异常工具类
 * @author YYL
 */
public class ExceptionHelper {

    /**
     * 传递异常
     * @param cause 源异常
     * @return 异常
     */
    public static GeneralException propagate(Throwable cause) {
        if (cause instanceof GeneralException) {
            return (GeneralException) cause;
        }
        return new GeneralException(ErrorType.DEFAULT, "#", cause);
    }

    /**
     * 传递异常
     * @param message 异常信息
     * @param cause 源异常
     * @return 异常
     */
    public static GeneralException propagate(String message, Throwable cause) {
        return new GeneralException(ErrorType.DEFAULT, message, cause);
    }

    /**
     * 创建提示类异常
     * @param message 异常信息
     * @return 提示类异常
     */
    public static GeneralException prompt(String message) {
        return new GeneralException(ErrorType.PROMPT, message);
    }
    
    /**
     * 创建异常
     * @param type 异常类型
     * @param message 异常信息
     * @return 异常
     */
    public static GeneralException error(ErrorType type, String message) {
        return new GeneralException(type, message);
    }

}
