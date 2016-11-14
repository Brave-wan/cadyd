package org.wcy.common.utils.aesutils;

import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static final String AES = "AES-CBC";
    private static final int HASH_ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static char[] humanPassphrase = {'P', 'e', 'r', ' ', 'v', 'a', 'l', 'l',
            'u', 'm', ' ', 'd', 'u', 'c', 'e', 's', ' ', 'L', 'a', 'b', 'a',
            'n', 't'};
    private static final String TYPE = "AES";
    private static byte[] salt = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD,
            0xE, 0xF};

    private static final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";

    private static byte[] iv = {0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC,
            0xD, 91};

    public static String encrypt(String key, String plaintext) {
        try {
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(key);
            PBEKeySpec myKeyspec = new PBEKeySpec(humanPassphrase, salt,
                    HASH_ITERATIONS, KEY_LENGTH);
            SecretKey sk = keyfactory.generateSecret(myKeyspec);
            byte[] skAsByteArray = sk.getEncoded();
            SecretKeySpec skforAES = new SecretKeySpec(skAsByteArray, TYPE);
            IvParameterSpec IV = new IvParameterSpec(iv);
            byte[] sendBytes = plaintext.getBytes("UTF8");
            byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, sendBytes);
            String base64_ciphertext = Base64Encoder.encode(ciphertext);
            return base64_ciphertext;
        } catch (Exception e) {
            Log.e(AES, e.getMessage());
            return null;
        }
    }

    public static String decrypt(String key, String plaintext) {
        try {
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(key);
            PBEKeySpec myKeyspec = new PBEKeySpec(humanPassphrase, salt,
                    HASH_ITERATIONS, KEY_LENGTH);
            SecretKey sk = keyfactory.generateSecret(myKeyspec);
            byte[] skAsByteArray = sk.getEncoded();
            SecretKeySpec skforAES = new SecretKeySpec(skAsByteArray, TYPE);
            IvParameterSpec IV = new IvParameterSpec(iv);
            byte[] s = Base64Decoder.decodeToBytes(plaintext);
            String decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, IV, s));
            return decrypted;
        } catch (Exception e) {
            Log.e(AES, e.getMessage());
            return null;
        }
    }

    private static byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                                  byte[] msg) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.ENCRYPT_MODE, sk, IV);
            return c.doFinal(msg);
        } catch (Exception nsae) {
            Log.e("AESdemo", "no cipher getinstance support for " + cmp);
        }
        return null;
    }

    private static byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV,
                                  byte[] ciphertext) {
        try {
            Cipher c = Cipher.getInstance(cmp);
            c.init(Cipher.DECRYPT_MODE, sk, IV);
            return c.doFinal(ciphertext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 两次解密 解密过程： 1.同加密1-4步 2.将加密后的字符串反纺成byte[]数组 3.将加密内容解密
     *
     * @param encodeRules
     * @param content
     */
    public static String AESDoubleDncode(String encodeRules, String content) {
        return decrypt(encodeRules, content);
    }

    /**
     * 两次加密 1.构造密钥生成器 2.根据ecnodeRules规则初始化密钥生成器 3.产生密钥 4.创建和初始化密码器 5.内容加密
     * 6.返回字符串
     *
     * @param encodeRules 密钥
     * @param content     内容
     */
    public static String AESDoubleEncode(String encodeRules, String content) {
        return encrypt(encodeRules, encrypt(encodeRules, content));
    }
}