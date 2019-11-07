package model.key;

/**
 * Created by thales on 13/05/17.
 */
public class VigenereKey {
    private char[] key;

    public VigenereKey(char[] key) {
        this.key = key;
    }

    public char[] getKey() {
        return key;
    }

    public void setKey(char[] key) {
        this.key = key;
    }
}
