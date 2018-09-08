package yyl.mvc.plug.expection;

/**
 * 异常类型
 */
public enum ErrorType {
    /** 未知 */
    UNKNOWN,
    /** 提示 */
    PROMPT,
    /** 告警 */
    WARN,
    /** 默认 */
    DEFAULT,
    /** 业务异常 */
    BUSINESS,
    /** 访问频繁 */
    EXCESSIVE,
    /** 无效账号 */
    DISABLED_ACCOUNT,
    /** 锁定账户 */
    LOCKED_ACCOUNT,
    /** 并发访问 */
    CONCURRENT_ACCESS,
    /** 证书无效 */
    INCORRECT_CREDENTIALS,
    /** 证书过期 */
    EXPIRED_CREDENTIALS,
}
