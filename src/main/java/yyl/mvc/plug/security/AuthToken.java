package yyl.mvc.plug.security;

import java.io.Serializable;

/**
 * 认证令牌
 * @author _yyl
 */
@SuppressWarnings("serial")
public class AuthToken implements Serializable {

    // ==============================Fields========================================
    /** 账号 */
    private String account;
    /** 密码 */
    private String password;

    // ==============================PropertyAccessors=============================
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
