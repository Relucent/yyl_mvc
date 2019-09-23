package yyl.mvc.common.codec;

public class MorseCoderTest {
    public static void main(String[] args) {
        MorseCoder morse = new MorseCoder();
        String code = morse.encode("关关雎鸠，在河之洲");
        System.out.println(code);
        String text = morse.decode(code);
        System.out.println(text);
    }
}
