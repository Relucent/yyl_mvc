package yyl.mvc.common.crypto;

import yyl.mvc.common.crypto.symmetric.Aes;

public class AesTestDemo {
    public static void main(String[] args) {
        try {
            byte[] key = "0123456789ABCDEF".getBytes();
            String encryptString = "8a2b01005143b9e001514404e7120004";
            Aes en = Aes.create(key);
            encryptString = en.encryptHex(encryptString);
            System.out.println("AES加密后为：" + encryptString);
            System.out.println("AES解密后为：" + en.decryptHexString(encryptString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
