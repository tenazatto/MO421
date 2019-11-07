package model.key;

import util.MathUtils;

/**
 * Created by thales on 13/05/17.
 */
public class AffineKey {
    private int a;
    private int b;

    public AffineKey(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public boolean isValidKey(int m) {
        return MathUtils.coprimality(a, m);
    }
}
