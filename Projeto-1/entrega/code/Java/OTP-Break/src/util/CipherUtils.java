package util;

import model.CipherText;
import model.Key;

import java.io.UnsupportedEncodingException;

/**
 * Created by thales on 04/04/17.
 */
public class CipherUtils {
    public static String xorCiphers(CipherText c1, CipherText c2) {
        byte[] cipher1 = c1.getCipherText();
        byte[] cipher2 = c2.getCipherText();
        int strXorLen = 0;

        if(cipher1.length < cipher2.length) {
            strXorLen = cipher1.length;
        } else {
            strXorLen = cipher2.length;
        }

        char[] strXor = new char[strXorLen];

        for(int i = 0; i < strXorLen; i++) {
            int xor = cipher1[i] ^ cipher2[i];
            strXor[i] = (char) xor;
        }

        return String.valueOf(strXor);
    }

    public static Key xorCipherWithPlainToFindKey(CipherText cipherText, String plainText) {
        byte[] cipher = cipherText.getCipherText();
        byte[] plain = new byte[0];
        try {
            plain = plainText.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int strXorLen = cipher.length;

        byte[] strXor = new byte[strXorLen];

        for(int i = 0; i < strXorLen; i++) {
            int xor = cipher[i] ^ plain[i];
            strXor[i] = (byte) xor;
        }

        Key key = new Key();
        key.setKey(strXor);

        return key;
    }

    public static String testDecrypt(CipherText cipherText, Key key) {
        byte[] cipher = cipherText.getCipherText();
        byte[] keyBytes = key.getKey();
        int strXorLen = cipher.length;

        char[] strXor = new char[strXorLen];

        for(int i = 0; i < strXorLen; i++) {
            int xor = cipher[i] ^ keyBytes[i];
            strXor[i] = (char) xor;
        }

        return String.valueOf(strXor);
    }
}
