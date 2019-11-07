package util;

import exception.InvalidPPMImageException;
import model.ppm.PPMImage;

import java.io.*;

/**
 * Created by thales on 14/05/17.
 */
public class PPMUtils {
    private static final String INIT_HEADER = "P6";

    public static PPMImage readPPM(File ppmFile) throws IOException, InvalidPPMImageException {
        PPMImage ppmImage = new PPMImage();
        int pvMax, c;

        ppmImage.setPath(ppmFile.getPath());

        BufferedReader ppmReader = new BufferedReader(new FileReader(ppmFile));

        String[] headers = ppmReader.readLine().split(" ");

        if(headers.length != 4
            || !INIT_HEADER.equals(headers[0])
            || (pvMax = Integer.parseInt(headers[3])) != 255) {
            throw new InvalidPPMImageException();
        }

        ppmImage.setNx(Integer.parseInt(headers[1]));
        ppmImage.setNy(Integer.parseInt(headers[2]));
        ppmImage.setPvmax(pvMax);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((c = ppmReader.read()) != -1) {
            byte b = (byte) c;
            output.write(b);
        }

        ppmImage.setContent(output.toByteArray());

        ppmReader.close();

        return ppmImage;
    }

    public static void writePPM(PPMImage ppmImage) throws IOException {
        PrintWriter ppmWriter = new PrintWriter(ppmImage.getPath(), "UTF-8");
        FileOutputStream output = new FileOutputStream(ppmImage.getPath(), true);

        StringBuilder sb = new StringBuilder();

        sb.append(INIT_HEADER);
        sb.append(" ");
        sb.append(String.valueOf(ppmImage.getNx()));
        sb.append(" ");
        sb.append(String.valueOf(ppmImage.getNy()));
        sb.append(" ");
        sb.append(String.valueOf(ppmImage.getPvmax()));
        sb.append(System.getProperty("line.separator"));

        ppmWriter.write(sb.toString());
        output.write(ppmImage.getContent());
        ppmWriter.close();
        output.close();
    }
}
