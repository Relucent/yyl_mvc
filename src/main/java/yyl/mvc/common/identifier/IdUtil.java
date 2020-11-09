package yyl.mvc.common.identifier;

import java.util.UUID;

/**
 * ID生成器<br>
 */
public class IdUtil {

    // ===================================Methods=============================================
    /**
     * 获得标准的UUID[长度36，格式XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXX(8-4-4-4-12) ]
     * @return UUID
     */
    public static String uuid36() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获得UUID(长度32)
     * @return UUID
     */
    public static String uuid32() {
        return UUID32.randomUUID().toString();
    }

    /**
     * 获得8位短ID生成器(百万级别以下数据量,发生重复的几率很低,可以作为ID使用,但是建议进行查重校验)
     * @return 8位短ID生
     */
    public static String id8() {
        StringBuilder buffer = new StringBuilder();
        String uuid = uuid32();
        for (int i = 0; i < 8; i++) {
            buffer.append(ALPHA_DIGITS[Integer.parseInt(uuid.substring(i * 4, i * 4 + 4), 16) % 36]);
        }
        return buffer.toString();
    }

    // ===================================Fields==============================================
    private final static char[] ALPHA_DIGITS = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890").toCharArray();

    // ===================================Constructors========================================
    protected IdUtil() {
    }
}
