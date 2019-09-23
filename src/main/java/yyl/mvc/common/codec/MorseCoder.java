package yyl.mvc.common.codec;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * 莫尔斯电码的编码和解码实现<br>
 * 莫尔斯电码（又译为摩斯密码，Morse code）是一种时通时断的信号代码，通过不同的排列顺序来表达不同的英文字母、数字和标点符号。<br>
 * 摩尔斯电码由三种类型的信号组成，分别为：短信号（滴）、长信号（嗒）和分隔符。三种信号通常习惯使用“.”、“-”、“/”表示。<br>
 * 摩尔斯电码有一个密码表，用来映射密码。密码表如下：（注意：字母都会转换为大写，0 为短信号，1 为长信号。）<br>
 * 参考：https://github.com/hustcc/xmorse <br>
 */
public class MorseCoder {

    /** 默认短标记 */
    public static final char DEFAULT_DIT = '.';
    /** 默认长标记 */
    public static final char DEFAULT_DAH = '-';
    /** 默认分隔符(点和划之间的停顿) */
    public static final char DEFAULT_SPACE = '/';

    /** 字符到莫尔斯电码的映射 */
    private static final Map<Character, String> standard = new HashMap<>();
    /** 莫尔斯电码到字符的映射 */
    private static final Map<String, Character> standardReverse = new HashMap<>();

    static {
        // Map of Morse code patterns to supported characters.
        Map<Character, String> standard = new HashMap<>();
        // Letters
        standard.put('A', "01"); // A
        standard.put('B', "1000"); // B
        standard.put('C', "1010"); // C
        standard.put('D', "100"); // D
        standard.put('E', "0"); // E
        standard.put('F', "0010"); // F
        standard.put('G', "110"); // G
        standard.put('H', "0000"); // H
        standard.put('I', "00"); // I
        standard.put('J', "0111"); // J
        standard.put('K', "101"); // K
        standard.put('L', "0100"); // L
        standard.put('M', "11"); // M
        standard.put('N', "10"); // N
        standard.put('O', "111"); // O
        standard.put('P', "0110"); // P
        standard.put('Q', "1101"); // Q
        standard.put('R', "010"); // R
        standard.put('S', "000"); // S
        standard.put('T', "1"); // T
        standard.put('U', "001"); // U
        standard.put('V', "0001"); // V
        standard.put('W', "011"); // W
        standard.put('X', "1001"); // X
        standard.put('Y', "1011"); // Y
        standard.put('Z', "1100"); // Z
        // Numbers
        standard.put('0', "11111"); // 0
        standard.put('1', "01111"); // 1
        standard.put('2', "00111"); // 2
        standard.put('3', "00011"); // 3
        standard.put('4', "00001"); // 4
        standard.put('5', "00000"); // 5
        standard.put('6', "10000"); // 6
        standard.put('7', "11000"); // 7
        standard.put('8', "11100"); // 8
        standard.put('9', "11110"); // 9
        // Punctuation
        standard.put('.', "010101"); // Full stop
        standard.put(',', "110011"); // Comma
        standard.put('?', "001100"); // Question mark
        standard.put('\'', "011110"); // Apostrophe
        standard.put('!', "101011"); // Exclamation mark
        standard.put('/', "10010"); // Slash
        standard.put('(', "10110"); // Left parenthesis
        standard.put(')', "101101"); // Right parenthesis
        standard.put('&', "01000"); // Ampersand
        standard.put(':', "111000"); // Colon
        standard.put(';', "101010"); // Semicolon
        standard.put('=', "10001"); // Equal sign
        standard.put('+', "01010"); // Plus sign
        standard.put('-', "100001"); // Hyphen1minus
        standard.put('_', "001101"); // Low line
        standard.put('"', "010010"); // Quotation mark
        standard.put('$', "0001001"); // Dollar sign

        // 计算反向的字典
        Map<String, Character> standardReverse = new HashMap<>();
        for (Map.Entry<Character, String> entry : standard.entrySet()) {
            standardReverse.put(entry.getValue(), entry.getKey());
        }
    }
    /** 短标记或点 */
    private final char dit; // short mark or dot
    /** 长标记或短划线 */
    private final char dah; // longer mark or dash
    /** 分隔符(点和划之间的停顿) */
    private final char space;

    /**
     * 构造函数
     */
    public MorseCoder() {
        this(DEFAULT_DIT, DEFAULT_DAH, DEFAULT_SPACE);
    }

    /**
     * 构造函数
     * @param dit 段标记符号
     * @param dah 长标记符号
     * @param space 分隔符(点和划之间的停顿)
     */
    public MorseCoder(char dit, char dah, char space) {
        this.dit = dit;
        this.dah = dah;
        this.space = space;
    }

    /**
     * 莫尔斯电码编码
     * @param text 输入文本
     * @return 莫尔斯电码
     */
    public String encode(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text can not be null");
        }
        // 字符串转大写
        text = text.toUpperCase();
        // 获得字符数组
        char[] chars = text.toCharArray();

        // 开始构建莫尔斯电码
        StringBuilder buffer = new StringBuilder();
        boolean first = true;
        // 遍历字符数组
        for (char ch : chars) {
            String tick = standard.get(ch);
            // 没有对应映射，说明是非标准的字符，使用 UNICODE
            if (tick == null) {
                tick = encodeUnicodeMorseCode(ch);
            }
            if (first) {
                first = false;
            } else {
                buffer.append(space);
            }
            buffer.append(tick);
        }

        // 莫尔斯电码
        return normalize(buffer);
    }

    /**
     * 莫尔斯电码解码
     * @param code 莫尔斯电码
     * @return 解码后的文本
     */
    public String decode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("morse code can not be null");
        }
        // 标准化
        code = standardize(code);
        // 构建解码文本
        StringBuilder buffer = new StringBuilder();
        // 解析莫尔斯电码
        StringTokenizer tokenizer = new StringTokenizer(code, String.valueOf(space));
        while (tokenizer.hasMoreTokens()) {
            String tick = tokenizer.nextToken();
            Character ch = standardReverse.get(tick);
            if (ch == null) {
                buffer.append(decodeUnicodeMorseCode(tick));
            } else {
                buffer.append(ch.charValue());
            }
        }
        return buffer.toString();
    }

    /**
     * UNICODE字符转莫尔斯电码
     * @param ch UNICODE字符
     * @return 莫尔斯电码
     */
    private String encodeUnicodeMorseCode(char ch) {
        return Integer.toBinaryString(ch);
    }

    /**
     * 莫尔斯电码转UNICODE字符
     * @param word 莫尔斯电码
     * @return UNICODE字符
     */
    private char decodeUnicodeMorseCode(String code) {
        return (char) Integer.parseInt(code, 2);
    }

    private String normalize(CharSequence code) {
        char[] chars = new char[code.length()];
        for (int i = 0; i < chars.length; i++) {
            char ch = code.charAt(i);
            if (DEFAULT_DIT == ch) {
                ch = dit;
            } else if (DEFAULT_DAH == ch) {
                ch = dah;
            } else if (DEFAULT_SPACE == ch) {
                ch = space;
            }
            chars[i] = ch;
        }
        return new String(chars);
    }

    private String standardize(CharSequence code) {
        char[] chars = new char[code.length()];
        for (int i = 0; i < chars.length; i++) {
            char ch = code.charAt(i);
            if (DEFAULT_DIT == ch) {
                ch = dit;
            } else if (DEFAULT_DAH == ch) {
                ch = dah;
            } else if (DEFAULT_SPACE == ch) {
                ch = space;
            }
            chars[i] = ch;
        }
        return new String(chars);
    }
}
