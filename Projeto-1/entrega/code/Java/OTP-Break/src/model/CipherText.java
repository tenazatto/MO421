package model;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by thales on 04/04/17.
 */
public class CipherText {
    private byte[] cipherText;

    public CipherText(URI cipherPath) {
        try {
            this.cipherText = Files.readAllBytes(Paths.get(cipherPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getCipherText() {
        return cipherText;
    }

    public void setCipherText(byte[] cipherText) {
        this.cipherText = cipherText;
    }
}
