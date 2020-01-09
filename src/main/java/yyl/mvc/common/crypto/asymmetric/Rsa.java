package yyl.mvc.common.crypto.asymmetric;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA加密算法<br>
 * RSA算法密钥长度范围为 96 bits ~ 1024 bits (12 bytes ~ 128 bytes)<br>
 * 如果要加解密较长的数据，会采用分段加解密的方式<br>
 */
public class Rsa extends AsymmetricCrypto {

    // =================================Constructors===========================================
    /**
     * 构造函数，使用随机生成的密钥对(私钥和公钥)
     */
    protected Rsa() {
        super(AsymmetricAlgorithm.RSA);
    }

    /**
     * 构造函数<br>
     * 私钥和公钥同时为null时，使用随机的密钥对(私钥和公钥)。 私钥和公钥可以只传入一个，只用来做加密或者解密其中一种操作.<br>
     * @param privateKey 私钥
     * @param publicKey 公钥
     */
    protected Rsa(PrivateKey privateKey, PublicKey publicKey) {
        super(AsymmetricAlgorithm.RSA, privateKey, publicKey);
    }

    // =================================CreateMethods==========================================
    /**
     * 创建RSA实例，使用随机生成的密钥对(私钥和公钥)。
     * @return RSA实例
     */
    public static Rsa create() {
        return new Rsa();
    }

    /**
     * 创建RSA实例，使用指定的密钥对(私钥和公钥)。
     * @param privateKey 私钥 {@link RSAPrivateKey}
     * @param publicKey 公钥{@link RSAPublicKey}
     * @return RSA实例
     */
    public static Rsa create(PrivateKey privateKey, PublicKey publicKey) {
        return new Rsa(privateKey, publicKey);
    }
}
