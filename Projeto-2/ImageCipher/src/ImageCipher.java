import exception.InvalidPPMImageException;
import model.ppm.PPMImage;
import util.PPMUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by thales on 13/05/17.
 */
public class ImageCipher {
    public static void main(String[] args) {
        File file = new File("/home/thales/MO421A/Projeto-2/projeto/sample.ppm");

        try {
            PPMImage image = PPMUtils.readPPM(file);

            image.setPath("./test.ppm");
            PPMUtils.writePPM(image);

            PPMImage imageF = PPMUtils.readPPM(new File("./test.ppm"));

            System.out.println(image.getContent());
            System.out.println(imageF.getContent());
            System.out.println(imageF.getContent().equals(image.getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidPPMImageException e) {
            e.printStackTrace();
        }
    }
}
