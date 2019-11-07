package cipher;

import model.key.TEAKey;

/**
 * Created by thales on 13/05/17.
 */
public class TEACipher {
    private static final int DELTA = 0x9E3779B9;

    public void encrypt(int[] v, TEAKey k) {

        int y = v[0], z = v[1],
                sum = 0,
                n = 32;
        int k0 = k.getKey(0)
                , k1 = k.getKey(1)
                , k2 = k.getKey(2)
                , k3 = k.getKey(3);

        while (n-- > 0) {
            sum += DELTA;
            y += ((z << 4) + k0) ^ (z + sum) ^ ((z >> 5) + k1);
            z += ((y << 4) + k2) ^ (y + sum) ^ ((y >> 5) + k3);
        }
        v[0] = y;
        v[1] = z;
    }

    public void decrypt(int[] v, TEAKey k) {
        int y = v[0], z = v[1]
                , sum = DELTA<<5
                , n = 32;
        int k0 = k.getKey(0)
                , k1 = k.getKey(1)
                , k2 = k.getKey(2)
                , k3 = k.getKey(3);
        while (n-- > 0) {
            z -= ((y << 4) + k2) ^ (y + sum) ^ ((y >> 5) + k3);
            y -= ((z << 4) + k0) ^ (z + sum) ^ ((z >> 5) + k1);
            sum -= DELTA;
        }
        v[0] = y;
        v[1] = z;
    }
}
