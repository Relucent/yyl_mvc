package yyl.mvc.common.codec;

import yyl.mvc.common.codec.MorseUtil.MarkOption;

public class MorseUtilTest {
    public static void main(String[] args) {
        MarkOption option = new MarkOption().setDit('-').setDah('+').setSpace('|');
        String code = MorseUtil.encode("关关雎鸠,在河之洲。窈窕淑女,君子好逑。", option);
        System.out.println(code);
        String text = MorseUtil.decode(code, option);
        System.out.println(text);
    }
}
