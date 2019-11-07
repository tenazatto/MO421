package model;

/**
 * Created by thales on 04/04/17.
 */
public class PlainTexts {
    private char[] plainText1;
    private char[] plainText2;

    public PlainTexts(int lengthPT1, int lengthPT2){
        plainText1 = new char[lengthPT1];
        plainText2 = new char[lengthPT2];
    }

    public String getPlainText1() {
        return String.valueOf(plainText1);
    }

    public void setPlainText1(String plainText1) {
        this.plainText1 = plainText1.toCharArray();
    }

    public char getCharPlainText1(int position) {
        return this.plainText1[position];
    }

    public void setCharPlainText1(char charPlainText1, int position) {
        this.plainText1[position] = charPlainText1;
    }

    public void setStringPlainText1(String strPlainText1, int position) {
        setStringPlainText1(strPlainText1, position, position + strPlainText1.length());
    }

    public void setStringPlainText1(String strPlainText1, int initPos, int finalPos) {
        setCharsPlainText1(strPlainText1.toCharArray(), initPos, finalPos);
    }

    public void setCharsPlainText1(char[] charsPlainText1, int initPos, int finalPos) {
        for (int i = 0; i <= finalPos - initPos; i++) {
            this.plainText1[initPos + i] = charsPlainText1[i];
        }
    }

    public String getPlainText2() {
        return String.valueOf(plainText2);
    }

    public void setPlainText2(String plainText2) {
        this.plainText2 = plainText2.toCharArray();
    }

    public char getCharPlainText2(int position) {
        return this.plainText2[position];
    }

    public void setCharPlainText2(char charPlainText2, int position) {
        this.plainText2[position] = charPlainText2;
    }

    public void setStringPlainText2(String strPlainText2, int position) {
        setStringPlainText2(strPlainText2, position, position + strPlainText2.length());
    }

    public void setStringPlainText2(String strPlainText2, int initPos, int finalPos) {
        setCharsPlainText2(strPlainText2.toCharArray(), initPos, finalPos);
    }

    public void setCharsPlainText2(char[] charsPlainText2, int initPos, int finalPos) {
        for (int i = 0; i <= finalPos - initPos; i++) {
            this.plainText2[initPos + i] = charsPlainText2[i];
        }
    }
}
