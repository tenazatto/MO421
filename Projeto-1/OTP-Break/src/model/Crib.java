package model;

/**
 * Created by thales on 04/04/17.
 */
public class Crib {
    private String word;
    private int chrPosition;
    private String cribValue;

    public Crib() {
    }

    public Crib(String word, int chrPosition, String cribValue) {
        this.word = word;
        this.chrPosition = chrPosition;
        this.cribValue = cribValue;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getChrPosition() {
        return chrPosition;
    }

    public void setChrPosition(int chrPosition) {
        this.chrPosition = chrPosition;
    }

    public String getCribValue() {
        return cribValue;
    }

    public void setCribValue(String cribValue) {
        this.cribValue = cribValue;
    }
}
