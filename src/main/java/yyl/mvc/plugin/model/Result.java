package yyl.mvc.plugin.model;

import java.io.Serializable;

/**
 * 结果对象
 * @author YYL
 */
@SuppressWarnings("serial")
public class Result<T> implements Serializable {

    // ==============================Fields========================================
    private Boolean success;
    private String message;
    private T data;

    // ==============================Constructors=====================================
    public Result(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static Result<?> success() {
        return new Result<>(Boolean.TRUE, "OK", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(Boolean.TRUE, "OK", data);
    }

    public static Result<?> error() {
        return new Result<>(Boolean.FALSE, "ERROR", null);
    }

    public static Result<?> error(String message) {
        return new Result<>(Boolean.FALSE, message, null);
    }

    // ==============================Methods==========================================
    @Override
    public String toString() {
        return "Result [success=" + success + ", message=" + message + ", data=" + data + "]";
    }

    // ==============================PropertyAccessors================================
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
