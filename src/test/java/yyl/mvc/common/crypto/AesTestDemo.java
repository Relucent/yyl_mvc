package yyl.mvc.common.crypto;

public class AesTestDemo {
    public static void main(String[] args) {
        try {
            String key = "1";
            String encryptString = "8a2b01005143b9e001514404e7120004";
            Aes en = new Aes(key);
            encryptString = en.encrypt(encryptString);
            System.out.println("AES加密后为：" + encryptString);
            System.out.println("AES解密后为：" + en.decrypt(encryptString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
