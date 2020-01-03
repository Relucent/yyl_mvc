package yyl.mvc.common.crypto.digest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

import yyl.mvc.common.codec.HexUtil;
import yyl.mvc.common.constants.IoConstants;
import yyl.mvc.common.crypto.CryptoException;


/**
 * 摘要算法 (非线程安全)<br>
 */
public class Digester {

    // =================================Fields================================================
    /** 摘要算法的功能类 */
    protected final MessageDigest messageDigest;
    /** 盐值 */
    protected byte[] salt = null;
    /** 加盐位置，默认0 */
    protected int saltPosition = 0;
    /** 散列次数 */
    protected int digestCount = 0;

    // =================================Constructors===========================================
    /**
     * 构造函数
     * @param algorithm 算法
     */
    public Digester(DigestAlgorithm algorithm) {
        this(algorithm.getValue());
    }

    /**
     * 构造函数
     * @param algorithm 算法
     */
    protected Digester(String algorithm) {
        this(algorithm, null);
    }

    /**
     * 构造函数
     * @param algorithm 算法
     * @param provider 算法提供者，null表示JDK默认，可以引入第三方包(例如BouncyCastle)提供更多算法支持
     */
    public Digester(DigestAlgorithm algorithm, Provider provider) {
        this(algorithm.getValue(), provider);
    }

    /**
     * 构造函数
     * @param algorithm 算法
     * @param provider 算法提供者，null表示JDK默认，可以引入第三方包(例如BouncyCastle)提供更多算法支持
     */
    protected Digester(String algorithm, Provider provider) {
        try {
            if (null == provider) {
                messageDigest = MessageDigest.getInstance(algorithm);
            } else {
                messageDigest = MessageDigest.getInstance(algorithm, provider);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    // =================================Methods================================================
    /**
     * 设置加盐内容
     * @param salt 盐值
     * @return this
     */
    public Digester setSalt(byte[] salt) {
        this.salt = salt;
        return this;
    }

    /**
     * 设置加盐的位置<br>
     * @param saltPosition 加盐位置
     * @return this
     */
    public Digester setSaltPosition(int saltPosition) {
        this.saltPosition = saltPosition;
        return this;
    }

    /**
     * 设置重复计算摘要值次数
     * @param digestCount 摘要值次数
     * @return this
     */
    public Digester setDigestCount(int digestCount) {
        this.digestCount = digestCount;
        return this;
    }

    /**
     * 获得 {@link MessageDigest}
     * @return {@link MessageDigest}
     */
    public MessageDigest getMessageDigest() {
        return messageDigest;
    }

    /**
     * 获取散列长度，0表示不支持此方法
     * @return 散列长度，0表示不支持此方法
     */
    public int getDigestLength() {
        return this.messageDigest.getDigestLength();
    }

    /**
     * 生成数据的摘要
     * @param input 被摘要数据
     * @return 摘要字节数组
     */
    public byte[] digest(String input) {
        return digest(input, StandardCharsets.UTF_8);
    }

    /**
     * 生成摘要
     * @param input 被摘要数据
     * @param charsetName 编码
     * @return 摘要字节数组
     */
    public byte[] digest(String input, String charsetName) {
        return digest(input, Charset.forName(charsetName));
    }

    /**
     * 生成数据的摘要
     * @param input 被摘要数据
     * @param charset 摘要数据
     * @return 摘要字节数组
     */
    public byte[] digest(String input, Charset charset) {
        return digest(input.getBytes(charset));
    }

    /**
     * 生成数据的摘要，并转为16进制字符串
     * @param input 被摘要数据
     * @return 摘要16进制字符串
     */
    public String digestHex(String input) {
        return digestHex(input, StandardCharsets.UTF_8);
    }

    /**
     * 生成数据的摘要，并转为16进制字符串
     * @param input 被摘要数据
     * @param charsetName 编码
     * @return 摘要16进制字符串
     */
    public String digestHex(String input, String charsetName) {
        return digestHex(input, Charset.forName(charsetName));
    }

    /**
     * 生成数据的摘要，并转为16进制字符串
     * @param input 被摘要数据
     * @param charset 编码
     * @return 摘要
     */
    public String digestHex(String input, Charset charset) {
        byte[] data = input.getBytes(charset);
        return digestHex(data);
    }

    /**
     * 生成摘要，并转为16进制字符串<br>
     * @param input 被摘要数据
     * @return 摘要
     */
    public String digestHex(byte[] input) {
        byte[] hash = digest(input);
        return HexUtil.encodeHexString(hash);
    }

    /**
     * 生成摘要
     * @param input 输入字节数组
     * @return 摘要字节数组
     */
    public byte[] digest(byte[] input) {
        // 使用指定的字节数组更新摘要
        doUpdate(input);
        // 来完成哈希计算。
        byte[] hash = digestAndReset();
        // 重复计算
        return doRepeatDigest(hash);
    }

    /**
     * 生成摘要
     * @param input {@link InputStream} 输入数据流
     * @return 摘要字节数组
     * @throws IOException 出现IO异常时抛出
     */
    public byte[] digest(InputStream input) throws IOException {
        byte[] buffer = new byte[IoConstants.DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        // 加盐在开头
        if (salt != null && saltPosition <= 0) {
            messageDigest.update(salt);
        }
        while (IoConstants.EOF != (n = input.read(buffer))) {
            // 加盐在中间
            if (salt != null && count <= saltPosition && saltPosition < count + n) {
                int len = (int) (saltPosition - count);
                if (len != 0) {
                    messageDigest.update(buffer, 0, len);
                }
                messageDigest.update(salt);
                messageDigest.update(buffer, len, n);
            } else {
                messageDigest.update(buffer, 0, n);
            }
            count += n;
        }
        // 加盐在末尾
        if (salt != null && count < saltPosition) {
            messageDigest.update(salt);
        }
        return messageDigest.digest();
    }

    /**
     * 使用指定的字节数组更新摘要
     * @param input 字节数组
     */
    private void doUpdate(byte[] input) {
        // 无加盐
        if (salt == null) {
            messageDigest.update(input);
        }
        // 加盐在开头
        else if (saltPosition <= 0) {
            messageDigest.update(salt);
            messageDigest.update(input);
        }
        // 加盐在末尾
        else if (saltPosition >= input.length) {
            messageDigest.update(input);
            messageDigest.update(salt);
        }
        // 加盐在中间
        else {
            messageDigest.update(input, 0, saltPosition);
            messageDigest.update(salt);
            messageDigest.update(input, saltPosition, input.length - saltPosition);
        }
    }

    /**
     * 重复计算摘要，取决于{@link #digestCount} 值<br>
     * 每次计算摘要前都会重置{@link #messageDigest}
     * @param input 第一次的摘要数据
     * @return 摘要字节数组
     */
    private byte[] doRepeatDigest(byte[] input) {
        int count = Math.max(1, digestCount);
        for (int i = 1; i < count; i++) {
            messageDigest.update(input);
            input = digestAndReset();
        }
        return input;
    }

    /**
     * 来完成哈希计算，并重置摘要。
     * @return
     */
    private byte[] digestAndReset() {
        try {
            return messageDigest.digest();
        } finally {
            messageDigest.reset();
        }
    }
}
