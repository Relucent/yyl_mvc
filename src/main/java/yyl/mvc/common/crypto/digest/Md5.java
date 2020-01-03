package yyl.mvc.common.crypto.digest;

/**
 * MD5算法 (非线程安全)<br>
 */
public class Md5 extends Digester {

    /**
     * 创建MD5实例
     * @return MD5
     */
    public static Md5 create() {
        return new Md5();
    }

    /**
     * 构造函数
     */
    public Md5() {
        this((byte[]) null);
    }

    /**
     * 构造函数
     * @param salt 盐值
     */
    public Md5(byte[] salt) {
        this(salt, 0, 1);
    }

    /**
     * 构造函数
     * @param salt 盐值
     * @param digestCount 摘要次数，当此值小于等于1,默认为1。
     */
    public Md5(byte[] salt, int digestCount) {
        this(salt, 0, digestCount);
    }

    /**
     * 构造函数
     * @param salt 盐值
     * @param saltPosition 加盐位置，既将盐值字符串放置在数据的index数，默认0
     * @param digestCount 摘要次数，当此值小于等于1,默认为1。
     */
    public Md5(byte[] salt, int saltPosition, int digestCount) {
        super(DigestAlgorithm.MD5);
        this.salt = salt;
        this.saltPosition = saltPosition;
        this.digestCount = digestCount;
    }
}
