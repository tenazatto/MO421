package cipher;

import exception.InvalidKeyException;
import model.key.AffineKey;
import util.MathUtils;

/**
 * Created by thales on 13/05/17.
 */
public class AffineCipher {
    public static byte encryptByte (AffineKey key, int m, byte x) throws InvalidKeyException {
        if (!key.isValidKey(m)){
            throw new InvalidKeyException();
        }

        return (byte)((key.getA() * ((int)x) + key.getB()) % m);
    }

    public static byte decryptByte (AffineKey key, int m, byte x) throws InvalidKeyException {
        if (!key.isValidKey(m)){
            throw new InvalidKeyException();
        }

        return (byte)(MathUtils.modInverse(key.getA(), m) * (((int)x) - key.getB()) % m);
    }

    public static byte[] encrypt (AffineKey key, int m, byte[] ba) throws InvalidKeyException {
        int len = ba.length;
        byte[] nba = new byte[len];

        for (int i = 0; i < len; i++) {
            byte nb = encryptByte(key, m, ba[i]);

            nba[i] = nb;
        }

        return nba;
    }

    public static byte[] decrypt (AffineKey key, int m, byte[] ba) throws InvalidKeyException {
        int len = ba.length;
        byte[] vba = new byte[len];

        for (int i = 0; i < len; i++) {
            byte vb = decryptByte(key, m, ba[i]);

            vba[i] = vb;
        }

        return vba;
    }
}
