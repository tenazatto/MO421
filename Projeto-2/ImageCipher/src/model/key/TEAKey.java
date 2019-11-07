package model.key;

/**
 * Created by thales on 13/05/17.
 */
public class TEAKey {
    private int[] key;

    public TEAKey(int[] key) {
        this.key = key;
    }

    public int[] getKey() {
        return key;
    }

    public int getKey(int index) {
        return key[index];
    }

    public void setKey(int[] key) {
        this.key = key;
    }
}
