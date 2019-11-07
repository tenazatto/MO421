package util;

/**
 * Created by thales on 13/05/17.
 */
public class MathUtils {
    public static int gcd(int x, int y) {
        return (y == 0) ? x : gcd(y, x % y);
    }

    public static boolean coprimality(int x, int y) {
        return (gcd(x, y) == 1);
    }

    public static int modInverse(int a, int m) {
        a %= m;
        for(int x = 1; x < m; x++) {
            if((a*x) % m == 1) return x;
        }

        return -1;
    }
}
