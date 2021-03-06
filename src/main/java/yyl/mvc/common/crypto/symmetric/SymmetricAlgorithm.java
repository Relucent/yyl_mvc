package yyl.mvc.common.crypto.symmetric;

/**
 * 对称算法类型<br>
 * @see <a href="https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyGenerator">KeyGenerator</a>
 */
public enum SymmetricAlgorithm {
    /** 默认的AES加密方式：AES/CBC/PKCS5Padding */
    AES("AES"),
    /** ARCFOUR 安全性比较低 */
    ARCFOUR("ARCFOUR"),
    /** Blowfish */
    Blowfish("Blowfish"),
    /** 默认的DES加密方式：DES/ECB/PKCS5Padding */
    DES("DES"),
    /** 3DES算法，默认实现为：DESede/CBC/PKCS5Padding */
    DESede("DESede"),
    /** RC2 私钥块加密算法 */
    RC2("RC2"),
    /** 综合性的对称加密算法 MD5 + DES */
    PBEWithMD5AndDES("PBEWithMD5AndDES"),
    /** 综合性的对称加密算法 SHA1 + DESede */
    PBEWithSHA1AndDESede("PBEWithSHA1AndDESede"),
    /** 综合性的对称加密算法 SHA1 + RC2_40 */
    PBEWithSHA1AndRC2_40("PBEWithSHA1AndRC2_40");

    /** 算法字符串表示 */
    private final String value;

    /**
     * 构造函数
     * @param value 算法字符串表示(区分大小写)
     */
    private SymmetricAlgorithm(String value) {
        this.value = value;
    }

    /**
     * 获取算法字符串表示
     * @return 算法字符串表示
     */
    public String getValue() {
        return this.value;
    }
}
