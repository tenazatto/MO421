package model;

/**
 * Created by thales on 15/04/17.
 */
public class PartialPlainTexts {
    private String ppt1;
    private String ppt2;
    private int startPos;
    private int endPos;

    public PartialPlainTexts(String ppt1, String ppt2, int startPos, int endPos) {
        this.ppt1 = ppt1;
        this.ppt2 = ppt2;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public String getPpt1() {
        return ppt1;
    }

    public void setPpt1(String ppt1) {
        this.ppt1 = ppt1;
    }

    public String getPpt2() {
        return ppt2;
    }

    public void setPpt2(String ppt2) {
        this.ppt2 = ppt2;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }
}
