package model.ppm;

/**
 * Created by thales on 14/05/17.
 */
public class PPMImage {
    private int nx;
    private int ny;
    private int pvmax;
    private String path;
    private byte[] content;

    public PPMImage() {
    }

    public PPMImage(int nx, int ny, int pvmax, String path, byte[] content) {
        this.nx = nx;
        this.ny = ny;
        this.pvmax = pvmax;
        this.path = path;
        this.content = content;
    }

    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }

    public int getNy() {
        return ny;
    }

    public void setNy(int ny) {
        this.ny = ny;
    }

    public int getPvmax() {
        return pvmax;
    }

    public void setPvmax(int pvmax) {
        this.pvmax = pvmax;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
